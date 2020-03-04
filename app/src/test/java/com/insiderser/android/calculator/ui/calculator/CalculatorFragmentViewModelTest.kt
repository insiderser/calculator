/*
 * Copyright 2020 Oleksandr Bezushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.insiderser.android.calculator.ui.calculator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.calculator.data.HistoryRepository
import com.insiderser.android.calculator.db.ExpressionsHistoryDao
import com.insiderser.android.calculator.domain.history.AddExpressionToHistoryUseCase
import com.insiderser.android.calculator.domain.math.EvaluateExpressionUseCase
import com.insiderser.android.calculator.domain.math.LocalizeExpressionUseCase
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@OptIn(ObsoleteCoroutinesApi::class)
class CalculatorFragmentViewModelTest {

    @Rule
    @JvmField
    val executorRule = InstantTaskExecutorRule()

    @MockK
    private lateinit var localizeExpressionUseCase: LocalizeExpressionUseCase

    @RelaxedMockK
    private lateinit var historyDao: ExpressionsHistoryDao

    private lateinit var viewModel: CalculatorFragmentViewModel

    // 1 for expression & 1 for result
    private var latch = CountDownLatch(2)

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(mainThreadSurrogate)

        every { localizeExpressionUseCase.invoke(any<Double>()) } answers {
            arg<Double>(0).toString()
        }
        every { localizeExpressionUseCase.invoke(any<String>()) } returnsArgument 0

        viewModel = CalculatorFragmentViewModel(
            EvaluateExpressionUseCase(),
            localizeExpressionUseCase,
            AddExpressionToHistoryUseCase(
                HistoryRepository(historyDao),
                EvaluateExpressionUseCase()
            )
        )
        viewModel.expression.observeForever {
            latch.countDown()
        }
        viewModel.result.observeForever {
            latch.countDown()
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testExpressionResultFlow() {
        checkExpressionAndResult("", "")

        performActionAndCheckExpressionAndResult("5", "5.0") {
            viewModel.onArithmeticButtonClicked("5")
        }

        performActionAndCheckExpressionAndResult("5!", "120.0") {
            viewModel.onArithmeticButtonClicked("!")
        }

        performActionAndCheckExpressionAndResult("5!+", "") {
            viewModel.onArithmeticButtonClicked("+")
        }

        performActionAndCheckExpressionAndResult("5!", "120.0") {
            viewModel.onClearButtonClicked()
        }

        performActionAndCheckExpressionAndResult("5!*sin(pi/2)", "120.0") {
            latch = CountDownLatch(14)
            viewModel.onArithmeticButtonClicked("*")
            viewModel.onArithmeticButtonClicked("sin")
            viewModel.onArithmeticButtonClicked("(")
            viewModel.onArithmeticButtonClicked("pi")
            viewModel.onArithmeticButtonClicked("/")
            viewModel.onArithmeticButtonClicked("2")
            viewModel.onArithmeticButtonClicked(")")
        }

        performActionAndCheckExpressionAndResult("", "") {
            viewModel.onClearButtonLongClick()
        }

        performActionAndCheckExpressionAndResult("", "") {
            viewModel.onClearButtonClicked()
        }
    }

    private inline fun performActionAndCheckExpressionAndResult(
        expectedExpression: String,
        expectedResult: String,
        action: () -> Unit
    ) {
        latch = CountDownLatch(2)
        action()
        checkExpressionAndResult(expectedExpression, expectedResult)
    }

    private fun checkExpressionAndResult(
        expectedExpression: String,
        expectedResult: String
    ) {
        latch.await(500, TimeUnit.MILLISECONDS)
        assertThat(viewModel.expression.value).isEqualTo(expectedExpression)
        assertThat(viewModel.result.value).isEqualTo(expectedResult)
    }
}

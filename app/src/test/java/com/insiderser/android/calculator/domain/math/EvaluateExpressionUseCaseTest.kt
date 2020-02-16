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
package com.insiderser.android.calculator.domain.math

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.math.sqrt

class EvaluateExpressionUseCaseTest {

    private val evaluator = EvaluateExpressionUseCase()

    @Test
    fun givenExpression_evaluate_evaluatesExpression() = runBlocking {
        checkExpressionEvaluated("3", 3)
        checkExpressionEvaluated("+99999999", 99999999)
        checkExpressionEvaluated("-595", -595)
        checkExpressionEvaluated("0.595", 0.595)
        checkExpressionEvaluated("-59.5", -59.5)
        checkExpressionEvaluated("-0", 0.0)

        checkExpressionEvaluated("3+9+9(-5)", -33)
        checkExpressionEvaluated("3.5*10.4", 36.4)
        checkExpressionEvaluated("3.3/9.9", 0.333)
        checkEvaluateThrowsInvalidExpressionException("3/0")

        checkExpressionEvaluated("log1", 0)
        checkExpressionEvaluated("loge", 1)
        checkExpressionEvaluated("log(e^100)", 100)
        checkExpressionEvaluated("log(exp100)", 100)
        checkEvaluateThrowsInvalidExpressionException("log0")
        checkEvaluateThrowsInvalidExpressionException("log-4")
        checkEvaluateThrowsInvalidExpressionException("log(-4)")

        checkExpressionEvaluated("sin0", 0)
        checkExpressionEvaluated("tan(pi/3)", sqrt(3.0))
        // checkEvaluateThrowsInvalidExpressionException("tan(pi/2)") // TODO: should throw, instead returns a number

        checkExpressionEvaluated("0!", 1)
        checkExpressionEvaluated("5!", 120)
        checkExpressionEvaluated("-5!", -120)
        checkExpressionEvaluated("5!+3", 123)
        checkExpressionEvaluated("3+5!", 123)
        checkExpressionEvaluated("3!^2", 36)
        checkEvaluateThrowsInvalidExpressionException("5.3!")
        checkEvaluateThrowsInvalidExpressionException("5!+")
    }

    private suspend fun checkExpressionEvaluated(expression: String, expectedResult: Number) {
        val result = evaluator(expression)
        assertThat(result.getOrThrow()).isWithin(0.01).of(expectedResult.toDouble())
    }

    private suspend fun checkEvaluateThrowsInvalidExpressionException(expression: String) {
        val result = evaluator(expression)
        assertThat(result.isFailure)
        assertThat(result.exceptionOrNull()).isInstanceOf(InvalidExpressionException::class.java)
    }
}

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

import android.text.Editable
import android.text.SpannableStringBuilder
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.insiderser.android.calculator.domain.math.EvaluateExpressionUseCase
import com.insiderser.android.calculator.domain.math.LocalizeExpressionUseCase
import com.insiderser.android.calculator.utils.Result
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.mapNotNull
import timber.log.Timber
import javax.inject.Inject

/**
 * A [ViewModel] for [CalculatorFragment].
 */
class CalculatorFragmentViewModel @Inject constructor(
    private val evaluateExpressionUseCase: EvaluateExpressionUseCase,
    private val localizeExpressionUseCase: LocalizeExpressionUseCase
) : ViewModel() {

    private val expressionChannel = ConflatedBroadcastChannel("")

    /** Localized current expression. */
    val expression: LiveData<CharSequence> = expressionChannel.asFlow()
        .mapLatest { expression -> localizeExpressionUseCase(expression) }
        .buffer(CONFLATED)
        .asLiveData()

    /** The localized result of the current expression. */
    val expressionResult: LiveData<CharSequence> = expressionChannel.asFlow()
        .mapLatest { expression -> evaluateExpressionUseCase.executeNow(expression) }
        .buffer(CONFLATED)
        .mapNotNull { result ->
            when (result) {
                is Result.Success -> localizeExpressionUseCase(result.data)
                is Result.Error -> {
                    Timber.i(result.cause)
                    ""
                }
                is Result.Loading -> null
            }
        }
        .asLiveData()

    fun onArithmeticButtonClicked(tag: String, cursorSelectionStart: Int, cursorSelectionEnd: Int) =
        updateExpression {
            replace(cursorSelectionStart, cursorSelectionEnd, tag)
        }

    fun onEqualButtonClicked() = updateExpression {
        // TODO
    }

    fun onClearButtonClicked(cursorSelectionStart: Int, cursorSelectionEnd: Int) =
        if (expressionResult.value.isNullOrEmpty()) clearExpression()
        else deleteSelection(cursorSelectionStart, cursorSelectionEnd)

    private fun deleteSelection(start: Int, end: Int) {
        updateExpression {
            if (start != end) {
                delete(start, end)
            } else if (start != 0) {
                delete(start - 1, start)
            }
        }
    }

    fun onClearButtonLongClick() = clearExpression()

    private fun clearExpression() {
        updateExpression { clear() }
    }

    private inline fun updateExpression(transform: Editable.() -> Unit) {
        val expression = SpannableStringBuilder(expressionChannel.valueOrNull ?: "")
        transform(expression)
        expressionChannel.offer(expression.toString())
    }
}

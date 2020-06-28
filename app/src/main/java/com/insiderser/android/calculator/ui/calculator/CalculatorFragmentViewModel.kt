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

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.insiderser.android.calculator.domain.history.AddExpressionToHistoryUseCase
import com.insiderser.android.calculator.domain.math.EvaluateExpressionUseCase
import com.insiderser.android.calculator.domain.math.LocalizeExpressionUseCase
import com.insiderser.android.calculator.model.Expression
import kotlinx.coroutines.channels.Channel.Factory.CONFLATED
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * A [ViewModel] for [CalculatorFragment].
 */
class CalculatorFragmentViewModel @Inject constructor(
    private val evaluateExpressionUseCase: EvaluateExpressionUseCase,
    private val localizeExpressionUseCase: LocalizeExpressionUseCase,
    private val addExpressionToHistoryUseCase: AddExpressionToHistoryUseCase
) : ViewModel() {

    private val expressionBuilder = StringBuilder()
    private val expressionChangedChannel = ConflatedBroadcastChannel(Unit)

    /** Localized current expression. */
    val expression: LiveData<Expression> = expressionChangedChannel.asFlow()
        .map { localizeExpressionUseCase(expressionBuilder.toExpression()) }
        .asLiveData(viewModelScope.coroutineContext)

    /** The localized result of the current expression. */
    val result: LiveData<Expression> = expressionChangedChannel.asFlow()
        .mapLatest {
            evaluateExpressionUseCase(expressionBuilder.toExpression())
                .onFailure { e -> Timber.i(e) }
                .map { result -> localizeExpressionUseCase(result) }
                .getOrDefault(Expression(""))
        }
        .buffer(CONFLATED)
        .asLiveData(viewModelScope.coroutineContext)

    fun onArithmeticButtonClicked(tag: String) = updateExpression { append(tag) }

    fun onEqualButtonClicked() = updateExpression {
        if (isNotEmpty()) {
            addExpressionToHistoryUseCase(expressionBuilder.toExpression())
        }
    }

    fun onClearButtonClicked() = updateExpression {
        if (isNotEmpty()) {
            delete(lastIndex, length)
        }
    }

    fun onClearButtonLongClick() = updateExpression { clear() }

    private inline fun updateExpression(crossinline transform: suspend StringBuilder.() -> Unit) {
        viewModelScope.launch {
            transform(expressionBuilder)
            expressionChangedChannel.offer(Unit)
        }
    }
}

private fun StringBuilder.toExpression() = Expression(this.toString())

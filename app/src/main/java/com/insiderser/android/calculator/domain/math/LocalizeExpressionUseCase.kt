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

import com.insiderser.android.calculator.model.Expression
import java.text.DecimalFormatSymbols
import javax.inject.Inject

/**
 * Helper class that localizes expressions.
 */
class LocalizeExpressionUseCase @Inject constructor() {

    private val replacementMap: Map<String, String>

    init {
        val formatSymbols = DecimalFormatSymbols()
        val replacement = mutableMapOf<String, String>()
        replacementMap = replacement

        replacement["."] = formatSymbols.decimalSeparator.toString()
        replacement["*"] = "×" // TODO localize ×
        replacement["/"] = "÷" // TODO localize ÷

        val zero = formatSymbols.zeroDigit
        for (i in 0..9) {
            replacement[i.toString()] = (zero + i).toString()
        }
    }

    /**
     * @return Localized representation of the [number][value].
     */
    operator fun invoke(value: Double): Expression {
        val valueAsString = value.toString().removeTrailingZeros()
        val expression = Expression(valueAsString)
        return invoke(expression)
    }

    /**
     * @return Localized representation of the [expression].
     */
    operator fun invoke(expression: Expression): Expression {
        var localizedExpression: String = expression.value
        replacementMap.forEach { (key, replacement) ->
            localizedExpression = localizedExpression.replace(key, replacement)
        }
        return Expression(localizedExpression)
    }

    private fun String.removeTrailingZeros(): String = replace(Regex("""\.0+$"""), "")
}

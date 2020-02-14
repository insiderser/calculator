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

import androidx.annotation.FloatRange
import androidx.annotation.IntRange

/**
 * @return Mathematical factorial of [n] (e.g. `n!`).
 */
@FloatRange(from = 1.0)
fun factorial(@IntRange(from = 0) n: Long): Double {
    require(n >= 0) { "Operand must not be negative, was $n" }
    var result = 1.0
    for (i in 2..n) {
        result *= i
        if (result == Double.POSITIVE_INFINITY) break
    }
    return result
}

/**
 * @return `true` is this [Double] represents an integer number (e.g. `1.0`), or `false` otherwise.
 */
fun Double.isInteger(): Boolean = this % 1 == 0.0

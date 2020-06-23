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
import org.junit.Assert.assertThrows
import org.junit.Test

class MathKtTest {

    @Test
    fun factorial() {
        assertThat(factorial(0)).isEqualTo(1.0)
        assertThat(factorial(1)).isEqualTo(1.0)
        assertThat(factorial(2)).isEqualTo(2.0)
        assertThat(factorial(5)).isEqualTo(120.0)
        assertThat(factorial(7)).isEqualTo(5040.0)
        assertThat(factorial(250)).isEqualTo(Double.POSITIVE_INFINITY)
        assertThat(factorial(Long.MAX_VALUE)).isEqualTo(Double.POSITIVE_INFINITY)

        assertThrows(IllegalArgumentException::class.java) {
            factorial(-1)
        }
        assertThrows(IllegalArgumentException::class.java) {
            factorial(Long.MIN_VALUE)
        }
    }

    @Test
    fun double_isInteger() {
        assertThat(5.0.isInteger()).isTrue()
        assertThat(0.0.isInteger()).isTrue()
        assertThat((-5555555555555.0).isInteger()).isTrue()

        assertThat(0.1.isInteger()).isFalse()
        assertThat(0.6706967069.isInteger()).isFalse()
        assertThat((-0.6188020).isInteger()).isFalse()
    }
}

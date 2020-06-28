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

import com.insiderser.android.calculator.dagger.Default
import com.insiderser.android.calculator.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.operator.Operator
import javax.inject.Inject

/**
 * A use case that can evaluate mathematical expressions. **Expressions must be not localized**.
 */
class EvaluateExpressionUseCase @Inject constructor(
    @Default defaultDispatcher: CoroutineDispatcher
) : UseCase<String, Double>() {

    override val coroutineDispatcher = defaultDispatcher

    override suspend fun execute(param: String): Double {
        try {
            val result = ExpressionBuilder(param)
                .operator(FactorialOperator)
                .build()
                .evaluate() + 0.0 // This ensures that we don't get -0.0
            require(result.isFinite()) { "Got $result result" }
            return result
        } catch (e: ArithmeticException) {
            throw InvalidExpressionException(e)
        } catch (e: IllegalArgumentException) {
            throw InvalidExpressionException(e)
        }
    }
}

private object FactorialOperator : Operator("!", 1, true, PRECEDENCE_POWER + 1) {
    override fun apply(vararg args: Double): Double {
        val arg = args[0]
        require(arg.isInteger()) { "Operand for factorial has to be an integer, was $arg" }
        require(arg >= 0) { "The operand of the factorial cannot be less than zero, was $arg" }

        val factorial = factorial(arg.toLong())
        require(factorial.isFinite()) { "Overflow in factorial. Possibly too big number: $arg" }
        return factorial
    }
}

/**
 * Thrown to indicate that the expression is invalid.
 */
class InvalidExpressionException(cause: Throwable) : IllegalArgumentException(cause)

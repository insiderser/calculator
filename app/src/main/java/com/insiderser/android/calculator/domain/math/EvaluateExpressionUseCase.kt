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

import com.insiderser.android.calculator.domain.UseCase
import kotlinx.coroutines.Dispatchers
import net.objecthunter.exp4j.ExpressionBuilder
import net.objecthunter.exp4j.operator.Operator
import javax.inject.Inject

/**
 * A use case that can evaluate mathematical expressions. **Expressions must be not localized**.
 */
class EvaluateExpressionUseCase @Inject constructor() :
    UseCase<String, Double>(Dispatchers.Default) {

    override suspend fun execute(param: String): Double {
        try {
            val result = ExpressionBuilder(param)
                .operator(FactorialOperator)
                .build()
                .evaluate()
            require(result.isFinite()) { "Got $result result" }
            return result
        } catch (e: RuntimeException) {
            throw when (e) {
                is ArithmeticException, is IllegalArgumentException ->
                    InvalidExpressionException(param, e)
                else -> e
            }
        }
    }
}

private object FactorialOperator : Operator("!", 1, false, PRECEDENCE_POWER + 1) {
    override fun apply(vararg args: Double): Double {
        val arg = args[0]
        require(arg.isInteger()) { "Operand for factorial has to be an integer, was $arg" }
        require(arg >= 0) { "The operand of the factorial cannot be less than zero, was $arg" }

        val factorial =
            factorial(arg.toLong())
        require(factorial.isFinite()) { "Overflow in factorial. Possibly too big number: $arg" }
        return factorial
    }
}

/**
 * Thrown to indicate that the expression is invalid.
 */
class InvalidExpressionException(expression: String, cause: Throwable) :
    IllegalArgumentException(
        "Exception occurred while trying to evaluate expression $expression",
        cause
    )

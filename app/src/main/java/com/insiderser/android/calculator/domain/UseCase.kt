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
package com.insiderser.android.calculator.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Executes a single unit of business logic asynchronously.
 */
abstract class UseCase<in P, R> {

    abstract val coroutineDispatcher: CoroutineDispatcher

    /**
     * Execute this use case with the given params. Can be called on any thread.
     * @return [Flow] where the result will be posted.
     */
    suspend operator fun invoke(param: P): Result<R> = withContext(coroutineDispatcher) {
        runCatching { execute(param) }
    }

    /**
     * Executes core logic.
     *
     * Will be called on a [coroutineDispatcher] passed as a parameter
     * to [UseCase] constructor. Can be canceled at any moment.
     *
     * Execution is considered successful if it returns without any [Exception].
     */
    protected abstract suspend fun execute(param: P): R
}

/**
 * Execute this use case. Can be called on any thread.
 * @return A [Flow] where the result will be posted.
 */
suspend operator fun <R> UseCase<Unit, R>.invoke(): Result<R> = invoke(Unit)

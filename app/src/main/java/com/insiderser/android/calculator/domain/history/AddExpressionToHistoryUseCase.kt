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

package com.insiderser.android.calculator.domain.history

import com.insiderser.android.calculator.dagger.IO
import com.insiderser.android.calculator.db.ExpressionsHistoryDao
import com.insiderser.android.calculator.db.ExpressionsHistoryEntity
import com.insiderser.android.calculator.domain.UseCase
import com.insiderser.android.calculator.domain.math.EvaluateExpressionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddExpressionToHistoryUseCase @Inject constructor(
    private val historyDao: ExpressionsHistoryDao,
    private val evaluateExpressionUseCase: EvaluateExpressionUseCase,
    @IO ioDispatcher: CoroutineDispatcher
) : UseCase<String, Unit>() {

    override val coroutineDispatcher = ioDispatcher

    override suspend fun execute(param: String) {
        val result = evaluateExpressionUseCase(param)
        val entity = ExpressionsHistoryEntity(param, result.getOrThrow())

        historyDao.insertOne(entity)
    }
}

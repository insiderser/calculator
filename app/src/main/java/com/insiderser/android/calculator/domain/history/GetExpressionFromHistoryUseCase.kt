package com.insiderser.android.calculator.domain.history

import com.insiderser.android.calculator.dagger.IO
import com.insiderser.android.calculator.db.ExpressionsHistoryDao
import com.insiderser.android.calculator.domain.UseCase
import com.insiderser.android.calculator.model.Expression
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

typealias HistoryId = Int

class GetExpressionFromHistoryUseCase @Inject constructor(
    private val historyDao: ExpressionsHistoryDao,
    @IO ioDispatcher: CoroutineDispatcher
) : UseCase<HistoryId, Expression>() {

    override val coroutineDispatcher = ioDispatcher

    override suspend fun execute(param: HistoryId): Expression {
        val value = historyDao.findById(param)?.expression
            ?: throw NoSuchElementException("History with id $param doesn't exist")
        return Expression(value)
    }
}

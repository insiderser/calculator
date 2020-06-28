package com.insiderser.android.calculator.domain.history

import com.insiderser.android.calculator.dagger.IO
import com.insiderser.android.calculator.db.ExpressionsHistoryDao
import com.insiderser.android.calculator.domain.UseCase
import com.insiderser.android.calculator.model.HistoryItem
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RemoveFromHistoryUseCase @Inject constructor(
    private val historyDao: ExpressionsHistoryDao,
    @IO ioDispatcher: CoroutineDispatcher
) : UseCase<HistoryItem, Unit>() {

    override val coroutineDispatcher = ioDispatcher

    override suspend fun execute(param: HistoryItem) {
        val itemsDeleted = historyDao.deleteOneById(param.id)
        check(itemsDeleted == 1)
    }
}

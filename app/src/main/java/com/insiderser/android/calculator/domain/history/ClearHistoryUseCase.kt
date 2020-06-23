package com.insiderser.android.calculator.domain.history

import com.insiderser.android.calculator.dagger.IO
import com.insiderser.android.calculator.db.ExpressionsHistoryDao
import com.insiderser.android.calculator.domain.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ClearHistoryUseCase @Inject constructor(
    private val historyDao: ExpressionsHistoryDao,
    @IO ioDispatcher: CoroutineDispatcher
) : UseCase<Unit, Unit>() {

    override val coroutineDispatcher = ioDispatcher

    override suspend fun execute(param: Unit) {
        historyDao.deleteAll()
    }
}

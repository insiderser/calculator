package com.insiderser.android.calculator.domain.history

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.insiderser.android.calculator.db.ExpressionsHistoryDao
import com.insiderser.android.calculator.db.ExpressionsHistoryEntity
import com.insiderser.android.calculator.domain.math.LocalizeExpressionUseCase
import com.insiderser.android.calculator.model.HistoryItem
import javax.inject.Inject

class GetFullHistoryUseCase @Inject constructor(
    private val historyDao: ExpressionsHistoryDao,
    private val localizeExpressionUseCase: LocalizeExpressionUseCase
) {

    operator fun invoke(): LiveData<PagedList<HistoryItem>> = historyDao.findAll()
        .map { it.toHistoryItem() }
        .toLiveData(PAGE_SIZE)

    private fun ExpressionsHistoryEntity.toHistoryItem() = HistoryItem(
        id = id,
        expression = localizeExpressionUseCase(expression),
        result = localizeExpressionUseCase(result),
        dateAdded = timeAdded
    )

    companion object {
        private const val PAGE_SIZE = 25
    }
}

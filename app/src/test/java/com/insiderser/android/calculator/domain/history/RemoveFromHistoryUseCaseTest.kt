package com.insiderser.android.calculator.domain.history

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.calculator.fakes.FakeExpressionsHistoryDao
import com.insiderser.android.calculator.model.Expression
import com.insiderser.android.calculator.model.HistoryItem
import com.insiderser.android.calculator.test.ExpressionsHistoryEntityProvider
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class RemoveFromHistoryUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val historyDao = FakeExpressionsHistoryDao()

    private val useCase = RemoveFromHistoryUseCase(historyDao, testDispatcher)

    @Test
    fun whenItemExists_deletesItem() = testDispatcher.runBlockingTest {
        val entity = ExpressionsHistoryEntityProvider.item1
        val item = HistoryItem(
            entity.id,
            Expression(entity.expression),
            Expression(entity.result.toString()),
            entity.timeAdded
        )
        historyDao.history[entity.id] = entity

        val result = useCase(item)

        assertThat(result.isSuccess).isTrue()
        assertThat(historyDao.history[entity.id]).isNull()
    }

    @Test
    fun whenItemDoesNotExists_returnsFailure() = testDispatcher.runBlockingTest {
        val entity = ExpressionsHistoryEntityProvider.item1
        val item = HistoryItem(
            entity.id,
            Expression(entity.expression),
            Expression(entity.result.toString()),
            entity.timeAdded
        )
        historyDao.history.remove(entity.id)

        val result = useCase(item)

        assertThat(result.isFailure).isTrue()
    }
}

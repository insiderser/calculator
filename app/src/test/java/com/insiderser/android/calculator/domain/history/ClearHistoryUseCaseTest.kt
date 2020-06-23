package com.insiderser.android.calculator.domain.history

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.calculator.domain.invoke
import com.insiderser.android.calculator.fakes.FakeExpressionsHistoryDao
import com.insiderser.android.calculator.test.ExpressionsHistoryEntityProvider
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class ClearHistoryUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val fakeHistoryDao = FakeExpressionsHistoryDao()

    private val useCase = ClearHistoryUseCase(fakeHistoryDao, testDispatcher)

    @Test
    fun invoke_clearsHistoryTable() = testDispatcher.runBlockingTest {
        fakeHistoryDao.history = ExpressionsHistoryEntityProvider.list
            .associateBy { it.id }
            .toMutableMap()

        useCase()

        assertThat(fakeHistoryDao.history).isEmpty()
    }
}

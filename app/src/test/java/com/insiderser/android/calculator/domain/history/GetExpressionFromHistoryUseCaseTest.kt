package com.insiderser.android.calculator.domain.history

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.calculator.db.ExpressionsHistoryEntity
import com.insiderser.android.calculator.fakes.FakeExpressionsHistoryDao
import com.insiderser.android.calculator.model.Expression
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class GetExpressionFromHistoryUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val historyDao = FakeExpressionsHistoryDao()

    private val useCase = GetExpressionFromHistoryUseCase(historyDao, testDispatcher)

    @Test
    fun whenHistoryItemExists_invoke_returnsFoundExpression() = testDispatcher.runBlockingTest {
        val id = 1000
        val expression = "some-expression-here"
        historyDao.history[id] = ExpressionsHistoryEntity(expression, 100.3)

        val result = useCase(id)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrThrow()).isEqualTo(Expression(expression))
    }

    @Test
    fun whenHistoryItemDoesNotExists_invoke_returnsFailure() = testDispatcher.runBlockingTest {
        val id = 1000
        historyDao.history.remove(id)

        val result = useCase(id)

        assertThat(result.isFailure).isTrue()
    }
}

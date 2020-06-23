package com.insiderser.android.calculator.domain.history

import com.google.common.truth.Truth.assertThat
import com.insiderser.android.calculator.domain.math.EvaluateExpressionUseCase
import com.insiderser.android.calculator.fakes.FakeExpressionsHistoryDao
import com.insiderser.android.calculator.test.ExpressionsHistoryEntityProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class AddExpressionToHistoryUseCaseTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val fakeHistoryDao = FakeExpressionsHistoryDao()
    private val evaluateExpressionUseCase: EvaluateExpressionUseCase = mockk()

    private val useCase = AddExpressionToHistoryUseCase(fakeHistoryDao, evaluateExpressionUseCase, testDispatcher)

    @Test
    fun givenValidExpression_invoke_addsExpressionToHistoryTable() = testDispatcher.runBlockingTest {
        fakeHistoryDao.history.clear()
        val (expression, value, _, _) = ExpressionsHistoryEntityProvider.item1

        coEvery { evaluateExpressionUseCase.invoke(expression) } returns Result.success(value)

        val result = useCase(expression)

        assertThat(result.isSuccess).isTrue()

        val insertedHistoryEntity = fakeHistoryDao.history.values.find { it.expression == expression }
        assertThat(insertedHistoryEntity?.expression).isEqualTo(expression)
        assertThat(insertedHistoryEntity?.result).isEqualTo(value)
    }

    @Test
    fun givenInvalidExpression_invoke_fails() = testDispatcher.runBlockingTest {
        val (expression, _, _, _) = ExpressionsHistoryEntityProvider.item1

        coEvery { evaluateExpressionUseCase.invoke(expression) } returns Result.failure(Throwable())

        val result = useCase(expression)

        assertThat(result.isFailure).isTrue()

        val insertedHistoryEntity = fakeHistoryDao.history.values.find { it.expression == expression }
        assertThat(insertedHistoryEntity).isNull()
    }
}

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

import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.SpyK
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

class UseCaseTest {

    private val fakeParam = FakeParameter()
    private val fakeResult = FakeResult()
    private val exception = Exception()

    private val dispatcher = TestCoroutineDispatcher()

    @SpyK
    private var useCaseImpl = FakeUseCase()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun givenExecuteReturnsSuccessfully_invoke_returnsSuccess() = runBlockingTest(dispatcher) {
        coEvery { useCaseImpl.execute(fakeParam) } returns fakeResult
        val result = useCaseImpl(fakeParam)
        coVerify(exactly = 1) { useCaseImpl.execute(fakeParam) }
        assertThat(result.getOrNull()).isSameInstanceAs(fakeResult)
    }

    @Test
    fun givenExecuteThrowsException_invoke_returnsFailure() = runBlockingTest(dispatcher) {
        coEvery { useCaseImpl.execute(fakeParam) } throws exception
        val result = useCaseImpl(fakeParam)
        coVerify(exactly = 1) { useCaseImpl.execute(fakeParam) }
        assertThat(result.exceptionOrNull()).isSameInstanceAs(exception)
    }

    private inner class FakeUseCase : UseCase<FakeParameter, FakeResult>(dispatcher) {
        public override suspend fun execute(param: FakeParameter) = fakeResult
    }

    private class FakeParameter
    private class FakeResult
}

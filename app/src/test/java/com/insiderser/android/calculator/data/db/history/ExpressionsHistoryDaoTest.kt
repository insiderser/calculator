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
package com.insiderser.android.calculator.data.db.history

import androidx.room.Room
import androidx.room.paging.LimitOffsetDataSource
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.insiderser.android.calculator.data.db.AppDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
@Suppress("BlockingMethodInNonBlockingContext")
class ExpressionsHistoryDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ExpressionsHistoryDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(getApplicationContext(), AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.historyDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun givenNewEntityWithId_insertOne_insertsWithThisId() = runBlocking {
        val id = 6
        val expression = "5 tan π + 6 ^ 3"
        val time = Date(-67375685)
        val entity = ExpressionsHistoryEntity(
            id = id,
            expression = expression,
            timeAdded = time
        )

        val newId = dao.insertOne(entity)
        assertThat(newId).isEqualTo(id)

        val insertedEntity = dao.findOneById(id)
        assertThat(insertedEntity).isNotNull()
        assertThat(insertedEntity.id).isEqualTo(id)
        assertThat(insertedEntity.expression).isEqualTo(expression)
        assertThat(insertedEntity.timeAdded).isEqualTo(time)

        checkTableContainsExactly(insertedEntity)
    }

    @Test
    fun givenNewEntityWithoutId_insertOne_insertsWithGeneratedId() = runBlocking {
        val expression = "5 tan π + 6 ^ 3"
        val time = Date(78281612)
        val entity = ExpressionsHistoryEntity(
            id = 0,
            expression = expression,
            timeAdded = time
        )

        val id = dao.insertOne(entity).toInt()
        assertThat(id).isAtLeast(1)

        val insertedEntity = dao.findOneById(id)
        assertThat(insertedEntity.id).isEqualTo(id)
        assertThat(insertedEntity.expression).isEqualTo(expression)
        assertThat(insertedEntity.timeAdded).isEqualTo(time)

        checkTableContainsExactly(insertedEntity)
    }

    @Test
    fun givenInsertedEntity_deleteOneById_deletesThisEntity() = runBlocking {
        val id = 100

        dao.insertOne(
            ExpressionsHistoryEntity(
                id = id,
                expression = "whatever"
            )
        )
        val affectedEntries = dao.deleteOneById(id)

        assertThat(affectedEntries).isEqualTo(1)
        checkTableContainsExactly()
        assertThat(dao.findOneById(id)).isNull()
    }

    @Test
    fun tryingToDeleteNonExistingEntity_deleteOneById_doesNothing() = runBlocking {
        val id = 15
        val affectedEntries = dao.deleteOneById(id)
        assertThat(affectedEntries).isEqualTo(0)
    }

    @Test
    fun givenInsertedEntity_deleteAll_deletesThem() = runBlocking {
        dao.insertOne(ExpressionsHistoryEntity(id = 5, expression = "whatever"))
        dao.insertOne(ExpressionsHistoryEntity(id = 6, expression = "whatever"))
        dao.insertOne(ExpressionsHistoryEntity(id = 7, expression = "whatever"))
        dao.insertOne(ExpressionsHistoryEntity(id = 8, expression = "whatever"))

        val affectedEntries = dao.deleteAll()
        assertThat(affectedEntries).isEqualTo(4)
    }

    @Test
    fun tryingToDeleteNonExistingEntity_deleteAll_doesNothing() = runBlocking {
        val affectedEntries = dao.deleteAll()
        assertThat(affectedEntries).isEqualTo(0)
    }

    private fun checkTableContainsExactly(vararg entities: ExpressionsHistoryEntity) {
        val dataSource = dao.findAll().create() as LimitOffsetDataSource<ExpressionsHistoryEntity>
        val allEntities = dataSource.loadRange(0, Int.MAX_VALUE)
        assertThat(allEntities).containsExactlyElementsIn(entities)
    }
}

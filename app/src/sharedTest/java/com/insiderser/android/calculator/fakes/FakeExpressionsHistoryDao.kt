package com.insiderser.android.calculator.fakes

import androidx.paging.DataSource
import com.insiderser.android.calculator.db.ExpressionsHistoryDao
import com.insiderser.android.calculator.db.ExpressionsHistoryEntity
import kotlin.random.Random

class FakeExpressionsHistoryDao : ExpressionsHistoryDao {

    var history = mutableMapOf<Int, ExpressionsHistoryEntity>()

    override fun findAll(): DataSource.Factory<Int, ExpressionsHistoryEntity> {
        throw TODO("not implemented")
    }

    override suspend fun findById(id: Int): ExpressionsHistoryEntity? = history.getOrDefault(id, null)

    override fun insertOne(entity: ExpressionsHistoryEntity): Long {
        if (entity.id >= 1) {
            history[entity.id] = entity
            return entity.id.toLong()
        }

        // We have to generate an ID.
        val newId = generateNewId()
        val newEntity = entity.copy(id = newId)
        return insertOne(newEntity)
    }

    private fun generateNewId(): Int {
        var newId: Int
        do {
            newId = Random.nextInt(from = 1, until = Int.MAX_VALUE)
        } while (history.containsKey(newId))
        return newId
    }

    override fun deleteOneById(id: Int): Int {
        val previousValue = history.remove(id)
        return if (previousValue != null) 1 else 0
    }

    override fun deleteAll(): Int {
        val size = history.size
        history.clear()
        return size
    }
}

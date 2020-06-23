package com.insiderser.android.calculator.test

import com.insiderser.android.calculator.db.ExpressionsHistoryEntity
import java.util.Date

object ExpressionsHistoryEntityProvider {

    val item1 = ExpressionsHistoryEntity(
        expression = "3+32-2^4",
        result = 31.0,
        id = 30,
        timeAdded = Date(1999893833289)
    )
    val item2 = ExpressionsHistoryEntity(
        expression = "3+32-sin1",
        result = -31.0,
        id = 23,
        timeAdded = Date(1999893033289)
    )
    val item3 = ExpressionsHistoryEntity(
        expression = "3^(-2)+sin1",
        result = Double.POSITIVE_INFINITY,
        id = 1,
        timeAdded = Date(1599893033289)
    )

    val list = listOf(item1, item2, item3)
}

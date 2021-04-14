package com.formaloo.loyalty.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.formaloo.loyalty.data.model.Deal

@Dao
abstract class DealsDao : BaseDao<Deal>() {

    @Query("SELECT * FROM Deal")
    abstract suspend fun getDeals(): List<Deal>

    @Query("SELECT * FROM Deal WHERE id = :id")
    abstract suspend fun getDeal(id: String): Deal

    @Query("DELETE FROM Deal")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(deal: Deal) {
        insert(deal)
    }

    suspend fun save(deals: List<Deal>) {
        insert(deals)
    }
}
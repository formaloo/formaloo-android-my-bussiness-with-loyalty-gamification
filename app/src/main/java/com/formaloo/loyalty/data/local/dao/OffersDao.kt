package com.formaloo.loyalty.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.formaloo.loyalty.data.model.Offer

@Dao
abstract class OffersDao : BaseDao<Offer>() {

    @Query("SELECT * FROM Offer")
    abstract suspend fun getOffers(): List<Offer>

    @Query("SELECT * FROM Offer WHERE id = :id")
    abstract suspend fun getOffer(id: String): Offer

    @Query("DELETE FROM Offer")
    abstract suspend fun deleteAllFromTable()

    suspend fun save(offer: Offer) {
        insert(offer)
    }

    suspend fun save(offers: List<Offer>) {
        insert(offers)
    }
}
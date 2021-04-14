package com.formaloo.loyalty.data.repository

import android.annotation.SuppressLint
import com.formaloo.loyalty.common.Either
import com.formaloo.loyalty.common.Failure
import com.formaloo.loyalty.common.ViewFailure
import com.formaloo.loyalty.data.local.dao.DealsDao
import com.formaloo.loyalty.data.local.dao.OffersDao
import com.formaloo.loyalty.data.model.Deal
import com.formaloo.loyalty.data.model.Offer
import org.json.JSONObject
import retrofit2.Call
import timber.log.Timber
import java.net.UnknownHostException

interface ClubRepository {
    suspend fun getOffers(): List<Offer>
    suspend fun getOfferById(id: String): Offer
    suspend fun getDeals(): List<Deal>
    suspend fun getDealById(id: String): Deal
}

class ClubRepositoryImpl(
    private val dao: OffersDao,
    private val dealsDao: DealsDao,
) : ClubRepository {

    override suspend fun getDeals(): List<Deal> {
        return dealsDao.getDeals()

    }

    override suspend fun getDealById(id: String): Deal {
        return dealsDao.getDeal(id)
    }

    override suspend fun getOffers(): List<Offer> {
        return dao.getOffers()

    }

    override suspend fun getOfferById(id: String): Offer {
        return dao.getOffer(id)
    }


    @SuppressLint("LogNotTimber")
    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            var jObjError: JSONObject? = null
            Timber.e("raw ${response.raw()}")
            Timber.e("body ${response.body()}")
            try {
                jObjError = JSONObject(response.errorBody()?.string())
                Timber.e("Repo responseErrorBody jObjError-> $jObjError")
            } catch (e: Exception) {

            }

            when (response.code()) {
                200 -> Either.Right(transform((response.body() ?: default)))
                201 -> Either.Right(transform((response.body() ?: default)))
                400 -> Either.Left(ViewFailure.responseError("$jObjError"))
                401 -> Either.Left(Failure.UNAUTHORIZED_Error)
                500 -> Either.Left(Failure.ServerError)
                else -> Either.Left(ViewFailure.responseError("$jObjError"))
            }

        } catch (exception: Throwable) {
            Timber.e("exception $exception")
            if (exception is UnknownHostException) {
                Either.Left(Failure.NetworkConnection)

            } else {
                Either.Left(ViewFailure.responseError("exception++>  $exception"))

            }
        }

    }
}
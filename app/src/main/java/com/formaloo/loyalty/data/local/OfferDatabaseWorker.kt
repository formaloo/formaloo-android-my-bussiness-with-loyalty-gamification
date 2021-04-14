package com.formaloo.loyalty.data.local

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.formaloo.loyalty.common.Constants.OFFERS_DATA_FILENAME
import com.formaloo.loyalty.data.model.Offer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject

class OfferDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    val dataBase: AppDataBase by inject()

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(OFFERS_DATA_FILENAME).use { inputStream ->
                /*
                * Collecting from the Flows in [OffersDao] is main-safe.  Room supports Coroutines and moves the
                * query execution off of the main thread.
                */
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val offersType = object : TypeToken<List<Offer>>() {}.type
                    val offersList: List<Offer> = Gson().fromJson(jsonReader, offersType)

                    dataBase.offersDao().save(offersList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "OfferDatabaseWorker"
    }
}

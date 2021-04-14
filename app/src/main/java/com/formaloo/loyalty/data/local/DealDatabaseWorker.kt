package com.formaloo.loyalty.data.local

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.formaloo.loyalty.common.Constants.DEALS_DATA_FILENAME
import com.formaloo.loyalty.common.Constants.OFFERS_DATA_FILENAME
import com.formaloo.loyalty.data.model.Deal
import com.formaloo.loyalty.data.model.Offer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import kotlinx.coroutines.coroutineScope
import org.koin.core.KoinComponent
import org.koin.core.inject

class DealDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    val dataBase: AppDataBase by inject()

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(DEALS_DATA_FILENAME).use { inputStream ->
                /*
                * Collecting from the Flows in [DealsDao] is main-safe.  Room supports Coroutines and moves the
                * query execution off of the main thread.
                */
                JsonReader(inputStream.reader()).use { jsonReader ->
                    val dealsType = object : TypeToken<List<Deal>>() {}.type
                    val dealsList: List<Deal> = Gson().fromJson(jsonReader, dealsType)

                    dataBase.dealsDao().save(dealsList)

                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error database", ex)
            Result.failure()
        }
    }

    companion object {
        private const val TAG = "DealDatabaseWorker"
    }
}

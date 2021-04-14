package com.formaloo.loyalty.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.formaloo.loyalty.common.Constants.DATABASE_NAME
import com.formaloo.loyalty.data.local.dao.CustomerDao
import com.formaloo.loyalty.data.local.dao.DealsDao
import com.formaloo.loyalty.data.local.dao.OffersDao
import com.formaloo.loyalty.data.model.Deal
import com.formaloo.loyalty.data.model.Offer
import com.formaloo.loyalty.data.model.customer.Customer

/**
 * The Room database for this app
 */

@Database(entities = [Customer::class, Offer::class, Deal::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun offersDao(): OffersDao
    abstract fun dealsDao(): DealsDao

    companion object {

        fun buildDatabase(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                .addCallback(
                    object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            val offerRequest = OneTimeWorkRequestBuilder<OfferDatabaseWorker>().build()
                            val dealRequest = OneTimeWorkRequestBuilder<DealDatabaseWorker>().build()
                            WorkManager.getInstance(context).enqueue(listOf(dealRequest,offerRequest))
                        }
                    }
                )
                .build()
        }


    }

}
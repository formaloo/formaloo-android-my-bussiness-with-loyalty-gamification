package com.formaloo.loyalty.data.local.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.formaloo.loyalty.common.BaseMethod
import com.formaloo.loyalty.common.Constants
import com.formaloo.loyalty.data.local.AppDataBase
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localModule = module {
    single() { AppDataBase.buildDatabase(androidContext()) }
    single { provideSharePreferences(androidApplication()) }
    factory { (get() as AppDataBase).customerDao() }
    factory { (get() as AppDataBase).offersDao() }
    factory { (get() as AppDataBase).dealsDao() }
    single { BaseMethod() }

}

private fun provideSharePreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(Constants.SharedPreferences, Context.MODE_PRIVATE)
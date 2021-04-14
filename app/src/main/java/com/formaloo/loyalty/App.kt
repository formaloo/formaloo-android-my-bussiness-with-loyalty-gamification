package com.formaloo.loyalty

import android.app.Application
import com.formaloo.loyalty.di.appComponent
import com.google.android.gms.maps.MapsInitializer
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber


open class App : Application() {
    override fun onCreate() {
        super.onCreate()

        configureDi()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
//            AppCenter.start(this, "", Analytics::class.java, Crashes::class.java)
        }

    }

    //     CONFIGURATION ---
    open fun configureDi() =
        startKoin {
            androidContext(this@App)
            // your modules
            modules(provideComponent())

        }

    //     PUBLIC API ---
    open fun provideComponent() = appComponent


}
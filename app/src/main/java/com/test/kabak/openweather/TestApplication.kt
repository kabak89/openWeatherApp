package com.test.kabak.openweather

import android.app.Application
import com.test.kabak.openweather.core.schedulers.UpdateScheduler

import com.test.kabak.openweather.core.storage.DatabaseManager
import com.test.kabak.openweather.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TestApplication)
            modules(appModule)
        }

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        DatabaseManager.init(this)
        UpdateScheduler.scheduleUpdate()
    }
}
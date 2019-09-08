package com.test.kabak.openweather

import android.app.Application

import com.test.kabak.openweather.core.storage.DatabaseManager
import timber.log.Timber


class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        DatabaseManager.init(this)
    }
}

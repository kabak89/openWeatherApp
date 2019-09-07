package com.test.kabak.openweather

import android.app.Application

import com.test.kabak.openweather.core.storage.DatabaseManager


class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        DatabaseManager.init(this)
    }
}

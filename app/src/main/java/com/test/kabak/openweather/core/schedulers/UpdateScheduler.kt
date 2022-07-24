package com.test.kabak.openweather.core.schedulers

import androidx.work.*
import com.test.kabak.openweather.data.workers.UpdateForecastDataWorker
import java.util.concurrent.TimeUnit

object UpdateScheduler {
    private const val SCHEDULED_FORECAST_UPDATE = "SCHEDULED_FORECAST_UPDATE"

    fun scheduleUpdate() {
        val myConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        val builder = PeriodicWorkRequestBuilder<UpdateForecastDataWorker>(1, TimeUnit.HOURS)
                .setConstraints(myConstraints)

        val work = builder
                .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(SCHEDULED_FORECAST_UPDATE, ExistingPeriodicWorkPolicy.KEEP, work)
    }
}
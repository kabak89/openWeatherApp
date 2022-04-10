package com.test.kabak.openweather.core.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.test.kabak.openweather.core.network.ServerApi
import com.test.kabak.openweather.core.storage.CurrentWeather
import com.test.kabak.openweather.core.storage.DatabaseManager
import kotlinx.coroutines.coroutineScope

class UpdateForecastDataWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        val cities = DatabaseManager.getDb().cityDao().loadAllSynchronous()

        cities.forEach { currentCity ->
            val cityId = currentCity.cityId
            var weather = DatabaseManager.getDb().currentWeatherDao().getById(cityId)

            if (weather == null || weather.isOutdated()) {
                val weatherResponse = ServerApi.weatherApi.getCurrentWeather(cityId).await()
                weather = CurrentWeather.buildFromServerResponse(cityId, weatherResponse)
                DatabaseManager.getDb().currentWeatherDao().insert(weather)
            }
        }

        Result.success()
    }


}
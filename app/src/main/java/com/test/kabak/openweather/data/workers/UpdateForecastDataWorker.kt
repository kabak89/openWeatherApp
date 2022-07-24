package com.test.kabak.openweather.data.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.test.kabak.openweather.domain.repo.CitiesRepo
import com.test.kabak.openweather.domain.repo.WeatherRepo
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UpdateForecastDataWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams), KoinComponent {

    private val weatherRepo: WeatherRepo by inject()
    private val citiesRepo: CitiesRepo by inject()

    override suspend fun doWork(): Result {
        citiesRepo.getCitiesFlow()
            .map { citiesList ->
                citiesList.forEach { city ->
                    weatherRepo.updateWeather(city.id)
                }
            }

        return Result.success()
    }

}
/**
 * Created by Eugeny Kabak on 16.07.2022
 */
package com.test.kabak.openweather.domain.logic

import com.test.kabak.openweather.domain.entity.CurrentWeather
import com.test.kabak.openweather.domain.repo.WeatherRepo
import com.test.kabak.openweather.util.CallResult
import com.test.kabak.openweather.util.callForResult
import kotlinx.coroutines.flow.Flow

class WeatherInteractor(
    private val weatherRepo: WeatherRepo,
) {
    fun getWeatherFlow(): Flow<List<CurrentWeather>> = weatherRepo.getWeatherFlow()

    fun isWeatherOutdated(weather: CurrentWeather): Boolean =
        System.currentTimeMillis() - weather.timestamp > CURRENT_WEATHER_MAX_LIVE_TIME

    suspend fun updateWeather(citiesIds: List<String>): CallResult<Unit> = callForResult {
        citiesIds.forEach {
            val result = weatherRepo.updateWeather(it)

            if (result is CallResult.Error) {
                throw result.error
            }
        }
    }

    private companion object {
        const val CURRENT_WEATHER_MAX_LIVE_TIME = 1000 * 60 * 10
    }
}
/**
 * Created by Eugeny Kabak on 16.07.2022
 */
package com.test.kabak.openweather.domain.logic

import com.test.kabak.openweather.domain.entity.CurrentWeather
import com.test.kabak.openweather.domain.repo.WeatherRepo
import com.test.kabak.openweather.util.CallResult
import kotlinx.coroutines.flow.Flow

class WeatherInteractor(
    private val weatherRepo: WeatherRepo,
) {
    fun getWeatherFlow(): Flow<List<CurrentWeather>> = weatherRepo.getWeatherFlow()
    suspend fun updateWeather(cityId: String): CallResult<Unit> = weatherRepo.updateWeather(cityId)

    fun isWeatherOutdated(weather: CurrentWeather): Boolean =
        System.currentTimeMillis() - weather.timestamp > CURRENT_WEATHER_MAX_LIVE_TIME

    private companion object {
        const val CURRENT_WEATHER_MAX_LIVE_TIME = 1000 * 60 * 10
    }
}
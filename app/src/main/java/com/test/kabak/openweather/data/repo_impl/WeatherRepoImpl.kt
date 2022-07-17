/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.data.repo_impl

import com.test.kabak.openweather.data.db.Storage
import com.test.kabak.openweather.data.network.WeatherService
import com.test.kabak.openweather.domain.entity.CurrentWeather
import com.test.kabak.openweather.domain.repo.WeatherRepo
import com.test.kabak.openweather.util.CallResult
import com.test.kabak.openweather.util.callForResult
import kotlinx.coroutines.flow.Flow

class WeatherRepoImpl(
    private val storage: Storage,
    private val weatherService: WeatherService,
) : WeatherRepo {
    override fun getWeatherFlow(): Flow<List<CurrentWeather>> = storage.getWeatherFlow()

    override suspend fun updateWeather(cityId: String): CallResult<Unit> = callForResult {
        val weather = weatherService.getWeather(cityId)
        storage.saveWeather(weather)
    }
}
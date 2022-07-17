/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.domain.repo

import com.test.kabak.openweather.domain.entity.CurrentWeather
import com.test.kabak.openweather.util.CallResult
import kotlinx.coroutines.flow.Flow

interface WeatherRepo {
    fun getWeatherFlow(): Flow<List<CurrentWeather>>
    suspend fun updateWeather(cityId: String): CallResult<Unit>
}
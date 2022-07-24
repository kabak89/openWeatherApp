/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.data.network

import com.test.kabak.openweather.core.network.WeatherApi
import com.test.kabak.openweather.domain.entity.CurrentWeather

class WeatherService(
    private val weatherApi: WeatherApi,
) {
    suspend fun getWeather(cityId: String): CurrentWeather =
        weatherApi.getCurrentWeather(cityId)
            .let { response ->
                WeatherServiceMapper.mapWeatherResponse(response = response, cityId = cityId)
            }
}
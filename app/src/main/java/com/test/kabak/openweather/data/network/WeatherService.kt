/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.data.network

import com.test.kabak.openweather.core.network.ServerApi
import com.test.kabak.openweather.domain.entity.CurrentWeather


class WeatherService(

) {
    suspend fun getWeather(cityId: String): CurrentWeather =
        ServerApi.weatherApi.getCurrentWeather(cityId)
            .let { response ->
                WeatherServiceMapper.mapWeatherResponse(response = response, cityId = cityId)
            }
}
/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.data.network

import com.test.kabak.openweather.core.network.dataClasses.CurrentWeatherResponse
import com.test.kabak.openweather.domain.entity.CurrentWeather

object WeatherServiceMapper {
    fun mapWeatherResponse(response: CurrentWeatherResponse, cityId: String): CurrentWeather {
        val weatherObject = response.weather.first()

        return CurrentWeather(
            cityId = cityId,
            minT = response.main.tempMin,
            maxT = response.main.tempMax,
            icon = weatherObject.icon,
            timestamp = System.currentTimeMillis(),
            windSpeed = response.wind.windSpeed,
            description = weatherObject.description,
        )
    }
}
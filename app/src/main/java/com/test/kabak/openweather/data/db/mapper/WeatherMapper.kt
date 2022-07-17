package com.test.kabak.openweather.data.db.mapper

import com.test.kabak.openweather.data.db.entity.CurrentWeatherTable
import com.test.kabak.openweather.domain.entity.CurrentWeather

object WeatherMapper {
    fun mapWeather(table: CurrentWeatherTable): CurrentWeather =
        CurrentWeather(
            cityId = table.cityId,
            minT = table.minT,
            maxT = table.maxT,
            icon = table.icon,
            timestamp = table.timestamp,
            windSpeed = table.windSpeed,
            description = table.description,
        )

    fun mapWeatherToTable(weather: CurrentWeather): CurrentWeatherTable =
        CurrentWeatherTable(
            cityId = weather.cityId,
            minT = weather.minT,
            maxT = weather.maxT,
            description = weather.description,
            icon = weather.icon,
            timestamp = weather.timestamp,
            windSpeed = weather.windSpeed,
        )
}
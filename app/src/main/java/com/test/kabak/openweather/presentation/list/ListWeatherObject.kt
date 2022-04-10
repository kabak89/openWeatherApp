package com.test.kabak.openweather.presentation.list

import com.test.kabak.openweather.core.storage.City
import com.test.kabak.openweather.core.storage.CurrentWeather

data class ListWeatherObject(
        val city: City,
        val currentWeather: CurrentWeather?,
)

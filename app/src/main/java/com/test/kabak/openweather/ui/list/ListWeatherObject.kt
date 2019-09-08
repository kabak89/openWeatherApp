package com.test.kabak.openweather.ui.list

import androidx.databinding.ObservableBoolean
import com.test.kabak.openweather.core.storage.City
import com.test.kabak.openweather.core.storage.CurrentWeather

data class ListWeatherObject(
        val city: City,
        val currentWeather: CurrentWeather?,

        val clickObservable: ObservableBoolean = ObservableBoolean()
)

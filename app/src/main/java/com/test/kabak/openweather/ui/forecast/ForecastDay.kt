package com.test.kabak.openweather.ui.forecast

import com.test.kabak.openweather.data.db.entity.ForecastWeatherTable

data class ForecastDay(
    val dayWeather: ForecastWeatherTable,
    val hourlyWeather: ArrayList<ForecastWeatherTable>,
) {
    class ForecastDayComparator : Comparator<ForecastDay> {
        override fun compare(o1: ForecastDay, o2: ForecastDay): Int {
            return when {
                o1.dayWeather.dateTime > o2.dayWeather.dateTime -> 1
                o1.dayWeather.dateTime < o2.dayWeather.dateTime -> -1
                else -> 0
            }
        }
    }
}
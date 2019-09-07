package com.test.kabak.openweather.ui.forecast

import com.test.kabak.openweather.core.storage.ForecastWeather
import java.util.*

data class ForecastDay(
        val dayWeather: ForecastWeather,
        val hourlyWeather: ArrayList<ForecastWeather>
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

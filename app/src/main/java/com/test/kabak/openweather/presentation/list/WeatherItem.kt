/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.presentation.list

import com.test.kabak.openweather.util.PrintableText

data class WeatherItem(
    val cityName: PrintableText,
    val cityId: String,
    val isOutdated: Boolean,
    val weather: Weather?,
) {
    data class Weather(
        val iconUrl: String,
        val minT: Float,
        val maxT: Float,
        val windSpeed: Float,
        val updateTime: PrintableText,
        val description: String,
    )
}
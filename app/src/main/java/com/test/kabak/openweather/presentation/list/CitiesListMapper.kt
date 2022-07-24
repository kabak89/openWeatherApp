package com.test.kabak.openweather.presentation.list

import android.content.Context
import android.text.format.DateUtils
import com.test.kabak.openweather.domain.entity.City
import com.test.kabak.openweather.domain.entity.CurrentWeather
import com.test.kabak.openweather.domain.logic.WeatherInteractor
import com.test.kabak.openweather.util.PrintableText

class CitiesListMapper(
    private val context: Context,
    private val weatherInteractor: WeatherInteractor,
) {
    fun mapWeatherItem(
        cityWeather: CurrentWeather?,
        city: City,
    ): WeatherItem {
        val weather = if (cityWeather != null) {
            WeatherItem.Weather(
                iconUrl = cityWeather.icon,
                minT = cityWeather.minT,
                maxT = cityWeather.maxT,
                windSpeed = cityWeather.windSpeed,
                updateTime = PrintableText.Raw(formatShortDate(cityWeather.timestamp)),
                description = cityWeather.description,
            )
        } else null

        val isOutdated =
            if (cityWeather == null) true else weatherInteractor.isWeatherOutdated(cityWeather)

        return WeatherItem(
            cityName = PrintableText.Raw(city.name),
            cityId = city.id,
            isOutdated = isOutdated,
            weather = weather,
        )
    }

    private fun formatShortDate(timestamp: Long): String {
        val time = DateUtils.formatDateTime(
            context,
            timestamp,
            DateUtils.FORMAT_SHOW_TIME
        )
        val date = DateUtils.formatDateTime(
            context,
            timestamp,
            DateUtils.FORMAT_NUMERIC_DATE
        )
        return "$date $time"
    }
}
package com.test.kabak.openweather.core.network.dataClasses

import com.google.gson.annotations.SerializedName
import java.util.*

data class CurrentWeatherResponse(
        val weather: ArrayList<WeatherObject>,
        val main: MainObject,
        val wind: WindObject
) {
    data class WeatherObject(
            var description: String,
            var icon: String
    )

    data class MainObject(
            @SerializedName("temp_min")
            val tempMin: Float,

            @SerializedName("temp_max")
            val tempMax: Float
    )

    data class WindObject(
            @SerializedName("speed")
            val windSpeed: Float
    )
}
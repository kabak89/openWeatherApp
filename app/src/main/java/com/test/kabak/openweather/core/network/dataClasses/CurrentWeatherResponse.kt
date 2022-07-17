package com.test.kabak.openweather.core.network.dataClasses

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("weather") val weather: List<WeatherObject>,
    @SerializedName("main") val main: MainObject,
    @SerializedName("wind") val wind: WindObject,
) {
    data class WeatherObject(
        @SerializedName("description") var description: String,
        @SerializedName("icon") var icon: String,
    )

    data class MainObject(
        @SerializedName("temp_min") val tempMin: Float,
        @SerializedName("temp_max") val tempMax: Float,
    )

    data class WindObject(
        @SerializedName("speed") val windSpeed: Float,
    )
}
package com.test.kabak.openweather.core.network.dataClasses

import com.google.gson.annotations.SerializedName

class CurrentWeatherResponse(
    @SerializedName("weather") val weather: List<WeatherObject>,
    @SerializedName("main") val main: MainObject,
    @SerializedName("wind") val wind: WindObject,
) {
    class WeatherObject(
        @SerializedName("description") var description: String,
        @SerializedName("icon") var icon: String,
    )

    class MainObject(
        @SerializedName("temp_min") val tempMin: Float,
        @SerializedName("temp_max") val tempMax: Float,
    )

    class WindObject(
        @SerializedName("speed") val windSpeed: Float,
    )
}
package com.test.kabak.openweather.core.network.dataClasses

import com.google.gson.annotations.SerializedName

class ForecastResponse(
    @SerializedName("list") val forecasts: List<ForecastObject>,
) {
    class ForecastObject(
        @SerializedName("dt") val dateTime: Long,
        @SerializedName("main") val main: MainObject,
        @SerializedName("weather") val weather: ArrayList<WeatherObject>,
        @SerializedName("wind") val wind: WindObject,
    )

    class MainObject(
        @SerializedName("temp") var temperature: Float,
        @SerializedName("temp_min") val minT: Float,
        @SerializedName("temp_max") val maxT: Float,
    )

    class WeatherObject(
        @SerializedName("description") val description: String,
        @SerializedName("icon") val icon: String,
    )

    class WindObject(
        @SerializedName("speed") val windSpeed: Float,
    )
}
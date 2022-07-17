package com.test.kabak.openweather.domain.entity

data class CurrentWeather(
    val cityId: String,
    val minT: Float,
    val maxT: Float,
    val icon: String,
    val timestamp: Long,
    val windSpeed: Float,
    val description: String,
)
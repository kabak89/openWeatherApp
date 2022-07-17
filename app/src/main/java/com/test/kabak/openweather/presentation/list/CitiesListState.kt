package com.test.kabak.openweather.presentation.list

data class CitiesListState(
    val cities: List<WeatherItem>,
    val isLoading: Boolean,
)
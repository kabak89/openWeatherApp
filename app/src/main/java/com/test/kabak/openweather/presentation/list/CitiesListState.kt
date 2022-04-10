package com.test.kabak.openweather.presentation.list

data class CitiesListState(
    val cities: List<ListWeatherObject>,
    val isLoading: Boolean,
)
package com.test.kabak.openweather.presentation.list

import androidx.lifecycle.viewModelScope
import com.test.kabak.openweather.domain.entity.City
import com.test.kabak.openweather.domain.entity.CurrentWeather
import com.test.kabak.openweather.domain.logic.CitiesInteractor
import com.test.kabak.openweather.domain.logic.WeatherInteractor
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CitiesListViewModel(
    private val citiesInteractor: CitiesInteractor,
    private val weatherInteractor: WeatherInteractor,
    private val citiesListMapper: CitiesListMapper,
) : com.example.mvvm.BaseViewModel<CitiesListState, CitiesListEvent>() {
    private val citiesFlow = citiesInteractor.getCities()
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val weatherFlow = weatherInteractor.getWeatherFlow()

    init {
        citiesFlow
            .combine(weatherFlow) { citiesList: List<City>, weatherList: List<CurrentWeather> ->
                citiesList.associateWith { city ->
                    weatherList.find { it.cityId == city.id }
                }
            }
            .map { cityWeatherMap ->
                val newWeather = cityWeatherMap
                    .map { currentPair ->
                        val weatherItem = currentPair.value
                        val city = currentPair.key
                        citiesListMapper.mapWeatherItem(cityWeather = weatherItem, city = city)
                    }

                updateState {
                    it.copy(cities = newWeather)
                }

                cityWeatherMap
                    .filter {
                        it.value == null
                    }
                    .forEach { currentPair ->
                        weatherInteractor.updateWeather(cityId = currentPair.key.id)
                    }
            }
            .launchIn(viewModelScope)
    }

    override fun createInitialState(): CitiesListState =
        CitiesListState(
            cities = emptyList(),
            isLoading = false,
        )

    fun loadData() {
        viewModelScope.launch {
            citiesFlow.value
                .forEach { weatherInteractor.updateWeather(it.id) }
        }
    }
}
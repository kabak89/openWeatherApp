package com.test.kabak.openweather.presentation.list

import androidx.lifecycle.viewModelScope
import com.test.kabak.openweather.domain.entity.City
import com.test.kabak.openweather.domain.entity.CurrentWeather
import com.test.kabak.openweather.domain.logic.CitiesInteractor
import com.test.kabak.openweather.domain.logic.WeatherInteractor
import com.test.kabak.openweather.presentation.ErrorConverter
import com.test.kabak.openweather.presentation.ErrorTranslator
import com.test.kabak.openweather.util.CallResult
import com.test.kabak.openweather.util.PrintableText
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CitiesListViewModel(
    citiesInteractor: CitiesInteractor,
    private val weatherInteractor: WeatherInteractor,
    private val citiesListMapper: CitiesListMapper,
    private val errorTranslator: ErrorTranslator,
    private val errorConverter: ErrorConverter,
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

                val citiesIds = cityWeatherMap
                    .filter {
                        it.value == null
                    }
                    .map { currentPair ->
                        currentPair.key.id
                    }

                updateWeatherForCitiesIds(citiesIds)
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
            val citiesIds = citiesFlow.value
                .map { it.id }

            updateWeatherForCitiesIds(citiesIds)
        }
    }

    private suspend fun updateWeatherForCitiesIds(citiesIds: List<String>) {
        updateState { it.copy(isLoading = true) }

        val result = weatherInteractor.updateWeather(citiesIds)

        if (result is CallResult.Error) {
            val error = errorConverter.convertError(result.error)
            val message = errorTranslator.buildErrorMessage(error)
            sendEvent(CitiesListEvent.ShowToast(PrintableText.Raw(message)))
        }

        updateState { it.copy(isLoading = false) }
    }
}
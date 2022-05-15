package com.test.kabak.openweather.presentation.list

import androidx.lifecycle.viewModelScope
import com.test.kabak.openweather.core.network.ServerApi
import com.test.kabak.openweather.core.storage.City
import com.test.kabak.openweather.core.storage.CurrentWeather
import com.test.kabak.openweather.core.storage.DatabaseManager
import com.test.kabak.openweather.core.storage.LocalDatabase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class CitiesListViewModel(
    private val db: LocalDatabase,
) : com.example.mvvm.BaseViewModel<CitiesListState, CitiesListEvent>() {
    init {
        db.cityDao().citiesFlow()
            .onEach { loadData() }
            .launchIn(viewModelScope)

        loadData()
    }

    override fun createInitialState(): CitiesListState =
        CitiesListState(
            cities = emptyList(),
            isLoading = false,
        )

    fun loadData() {
        viewModelScope.launch {
            try {
                updateState {
                    it.copy(
                        isLoading = true,
                    )
                }

                val cachedWeather = db.cityDao().loadAllSynchronous()
                    .map { currentCity ->
                        val cityId = currentCity.cityId
                        val weather = DatabaseManager.getDb().currentWeatherDao().getById(cityId)
                        buildListWeatherObject(currentCity, weather)
                    }

                updateState {
                    it.copy(cities = cachedWeather)
                }

                val actualWeather = DatabaseManager.getDb().cityDao().loadAllSynchronous()
                    .map { currentCity ->
                        val cityId = currentCity.cityId
                        val weatherResponse =
                            ServerApi.weatherApi.getCurrentWeather(cityId).await()
                        val newWeather =
                            CurrentWeather.buildFromServerResponse(cityId, weatherResponse)
                        DatabaseManager.getDb().currentWeatherDao().insert(newWeather)
                        buildListWeatherObject(currentCity, newWeather)
                    }

                updateState {
                    it.copy(cities = actualWeather, isLoading = false)
                }
            } catch (exception: Exception) {
                Timber.e(exception)
                //TODO
//                errorsLiveData.postValue(Event(ErrorConverter.convertError(exception)))
            }
        }
    }

    private fun buildListWeatherObject(
        currentCity: City,
        weather: CurrentWeather?,
    ): ListWeatherObject = ListWeatherObject(currentCity, weather)
}
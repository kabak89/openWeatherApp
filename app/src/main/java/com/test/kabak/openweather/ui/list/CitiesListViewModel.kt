package com.test.kabak.openweather.ui.list

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.test.kabak.openweather.core.common.ErrorConverter
import com.test.kabak.openweather.core.network.ServerApi
import com.test.kabak.openweather.core.storage.City
import com.test.kabak.openweather.core.storage.CurrentWeather
import com.test.kabak.openweather.core.storage.DatabaseManager
import com.test.kabak.openweather.ui.common.BaseViewModel
import com.test.kabak.openweather.ui.common.Event
import kotlinx.coroutines.launch

class CitiesListViewModel(application: Application) : BaseViewModel(application) {
    val stateLiveData by lazy { MutableLiveData(State(emptyList(), false)) }
    val errorsLiveData by lazy { MutableLiveData<Event<Exception>>() }

    fun loadData() {
        scope.launch {
            try {
                updateCurrentState { it.copy(isLoading = true) }

                val cachedWeather = DatabaseManager.getDb().cityDao().loadAllSynchronous
                    .map { currentCity ->
                        val cityId = currentCity.cityId
                        val weather = DatabaseManager.getDb().currentWeatherDao().getById(cityId)
                        buildListWeatherObject(currentCity, weather)
                    }

                updateCurrentState {
                    it.copy(cities = cachedWeather)
                }

                val actualWeather = DatabaseManager.getDb().cityDao().loadAllSynchronous
                    .map { currentCity ->
                        val cityId = currentCity.cityId
                        val weather = DatabaseManager.getDb().currentWeatherDao().getById(cityId)
                        if (weather == null || weather.isOutdated()) {
                            val weatherResponse =
                                ServerApi.weatherApi.getCurrentWeather(cityId).await()
                            val newWeather =
                                CurrentWeather.buildFromServerResponse(cityId, weatherResponse)
                            DatabaseManager.getDb().currentWeatherDao().insert(newWeather)
                            buildListWeatherObject(currentCity, newWeather)
                        } else {
                            buildListWeatherObject(currentCity, weather)
                        }
                    }

                updateCurrentState {
                    it.copy(cities = actualWeather, isLoading = false)
                }
            } catch (exception: Exception) {
                errorsLiveData.postValue(Event(ErrorConverter.convertError(exception)))
            }
        }
    }

    fun onAddCityClicked() {

    }

    private fun buildListWeatherObject(
        currentCity: City,
        weather: CurrentWeather?
    ): ListWeatherObject = ListWeatherObject(currentCity, weather)

    private fun currentState() = stateLiveData.value!!

    private fun updateCurrentState(block: (State) -> State) {
        val newState = block(currentState())
        stateLiveData.postValue(newState)
    }

    data class State(
        val cities: List<ListWeatherObject>,
        val isLoading: Boolean,
    )
}

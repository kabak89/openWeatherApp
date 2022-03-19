package com.test.kabak.openweather.ui.list

import android.app.Application
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.MutableLiveData
import com.test.kabak.openweather.core.common.ErrorConverter
import com.test.kabak.openweather.core.network.ServerApi
import com.test.kabak.openweather.core.network.dataClasses.CurrentWeatherResponse
import com.test.kabak.openweather.core.storage.City
import com.test.kabak.openweather.core.storage.CurrentWeather
import com.test.kabak.openweather.core.storage.DatabaseManager
import com.test.kabak.openweather.ui.common.BaseViewModel
import com.test.kabak.openweather.ui.common.Event
import com.test.kabak.openweather.ui.common.addOnPropertyChanged
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class CitiesListViewModel(application: Application) : BaseViewModel(application) {
    private val state = State()
    val stateLiveData by lazy { MutableLiveData(state) }
    val errorsLiveData by lazy { MutableLiveData<Event<Exception>>() }
    val goAddCityLiveData by lazy { MutableLiveData<Event<Boolean>>() }
    val goCityDetailsLiveData by lazy { MutableLiveData<Event<ListWeatherObject>>() }

    init {
        state.addCityClickObservable.addOnPropertyChanged {
            goAddCityLiveData.value = Event(true)
        }
    }

    fun loadData() {
        scope.launch {
            try {
                state.loadingObservable.set(true)

                scope.launch(Dispatchers.Main) {
                    state.cities.clear()
                }.join()

                val cities = DatabaseManager.getDb().cityDao().loadAllSynchronous

                cities.forEach { currentCity ->
                    val cityId = currentCity.cityId
                    var weather = DatabaseManager.getDb().currentWeatherDao().getById(cityId)

                    if (weather == null || weather.isOutdated()) {
                        scope.launch(Dispatchers.Main) {
                            state.cities.add(buildListWeatherObject(currentCity, weather))
                        }.join()

                        val weatherResponse: CurrentWeatherResponse

                        try {
                            weatherResponse = ServerApi.weatherApi.getCurrentWeather(cityId).await()
                            weather = CurrentWeather.buildFromServerResponse(cityId, weatherResponse)
                            DatabaseManager.getDb().currentWeatherDao().insert(weather)
                        } catch (exception: Exception) {
                            Timber.e(exception)
                        }
                    }

                    val listWeatherObject = buildListWeatherObject(currentCity, weather)
                    val existedCity = state.cities.find { it.city.cityId == cityId }

                    scope.launch(Dispatchers.Main) {
                        state.cities.remove(existedCity)
                        state.cities.add(listWeatherObject)
                    }.join()
                }

                state.loadingObservable.set(false)
            } catch (exception: Exception) {
                errorsLiveData.postValue(Event(ErrorConverter.convertError(exception)))
            }
        }
    }

    private fun buildListWeatherObject(
            currentCity: City,
            weather: CurrentWeather?
    ): ListWeatherObject {
        val listWeatherObject = ListWeatherObject(currentCity, weather)

        listWeatherObject.clickObservable.addOnPropertyChanged {
            goCityDetailsLiveData.value = Event(listWeatherObject)
        }

        return listWeatherObject
    }

    data class State(
            val cities: ObservableArrayList<ListWeatherObject> = ObservableArrayList(),

            val loadingObservable: ObservableBoolean = ObservableBoolean(),
            val addCityClickObservable: ObservableBoolean = ObservableBoolean()
    )
}

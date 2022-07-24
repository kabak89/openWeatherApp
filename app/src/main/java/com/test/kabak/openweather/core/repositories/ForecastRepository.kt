package com.test.kabak.openweather.core.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.test.kabak.openweather.core.Resource
import com.test.kabak.openweather.core.network.WeatherApi
import com.test.kabak.openweather.core.network.dataClasses.ForecastResponse
import com.test.kabak.openweather.core.network.dataClasses.ForecastResponse.ForecastObject
import com.test.kabak.openweather.core.storage.DatabaseManager.getDb
import com.test.kabak.openweather.data.db.entity.ForecastWeatherTable
import com.test.kabak.openweather.data.db.entity.ForecastWeatherTable.ForecastWeatherComparator
import com.test.kabak.openweather.ui.forecast.ForecastDay
import com.test.kabak.openweather.ui.forecast.ForecastDay.ForecastDayComparator
import com.test.kabak.openweather.ui.forecast.ForecastDayObject
import io.reactivex.Single
import io.reactivex.SingleEmitter
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.*

class ForecastRepository : KoinComponent {
    private val weatherApi: WeatherApi by inject()

    var data = MutableLiveData<Resource<ForecastDayObject>>()
    fun getForecast(cityId: String): LiveData<Resource<ForecastDayObject>> {
        data.value = Resource(Resource.LOADING, null, null)
        Single
            .create { e: SingleEmitter<List<ForecastWeatherTable>> ->
                val cachedForecast = getDb().forecastWeatherDao().getCachedForecast(
                    cityId = cityId,
                    timestamp = forecastMaxLiveTime
                )
                if (cachedForecast.isEmpty()) {
                    e.onError(Exception("Empty database"))
                } else {
                    e.onSuccess(cachedForecast)
                }
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<ForecastWeatherTable>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(hourForecasts: List<ForecastWeatherTable>) {
                    val forecastDayObject = prepareResult(hourForecasts)
                    data.value = Resource(Resource.COMPLETED, forecastDayObject, null)
                }

                override fun onError(e: Throwable) {
                    downloadForecast(cityId)
                }
            })
        return data
    }

    private val forecastMaxLiveTime: Long
        get() = System.currentTimeMillis() - 1000 * 60 * 30

    fun downloadForecast(cityId: String) {
        val responseSingle: Single<ForecastResponse> = weatherApi.getForecast(cityId)
        responseSingle
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map {
                val hourForecasts = convertServerResponseToStorageObjects(
                    it.forecasts, cityId)
                getDb().forecastWeatherDao().insertAll(hourForecasts)
                hourForecasts
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<ForecastWeatherTable>> {
                override fun onSubscribe(d: Disposable) {}
                override fun onSuccess(hourForecasts: List<ForecastWeatherTable>) {
                    val forecastDayObject = prepareResult(hourForecasts)
                    data.setValue(Resource(Resource.COMPLETED, forecastDayObject, null))
                }

                override fun onError(e: Throwable) {
                    val objectResource =
                        Resource<ForecastDayObject>(Resource.COMPLETED, null, Exception(e))
                    data.setValue(objectResource)
                }
            })
    }

    fun prepareResult(hourForecasts: List<ForecastWeatherTable>): ForecastDayObject {
        val groupedForecasts = groupForecastsByDay(hourForecasts)
        val entries = groupedForecasts.entries
        val forecastDayObject = buildDayObject(entries)
        Collections.sort(forecastDayObject.forecastDays, ForecastDayComparator())
        return forecastDayObject
    }

    companion object {
        fun buildDayObject(entries: Set<Map.Entry<Int, ArrayList<ForecastWeatherTable>>>): ForecastDayObject {
            val objects = ArrayList<ForecastDay>(10)
            for ((_, dayForecasts) in entries) {
                var nearestWeather: ForecastWeatherTable? = null
                var nearestHour = Int.MAX_VALUE
                val dayWeatherClock = 12
                for (currentForecastWeatherTable in dayForecasts) {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = currentForecastWeatherTable.dateTime
                    val hourOfDay = calendar[Calendar.HOUR_OF_DAY]
                    if (Math.abs(hourOfDay - dayWeatherClock) < Math.abs(
                            nearestHour - dayWeatherClock)
                    ) {
                        nearestHour = hourOfDay
                        nearestWeather = currentForecastWeatherTable
                    }
                }
                val forecastDay = ForecastDay(nearestWeather!!, dayForecasts)
                Collections.sort(forecastDay.hourlyWeather,
                    ForecastWeatherComparator())
                objects.add(forecastDay)
            }
            return ForecastDayObject(objects)
        }

        fun groupForecastsByDay(hourForecasts: List<ForecastWeatherTable>): Map<Int, ArrayList<ForecastWeatherTable>> {
            val groupedForecasts: MutableMap<Int, ArrayList<ForecastWeatherTable>> = HashMap(10)
            for (currentForecast in hourForecasts) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = currentForecast.dateTime
                val key = calendar[Calendar.YEAR] * 1000 + calendar[Calendar.DAY_OF_YEAR]
                var existedArray = groupedForecasts[key]
                if (existedArray == null) {
                    existedArray = ArrayList(10)
                    groupedForecasts[key] = existedArray
                }
                existedArray.add(currentForecast)
            }
            return groupedForecasts
        }

        fun convertServerResponseToStorageObjects(
            forecasts: List<ForecastObject>,
            cityId: String,
        ): List<ForecastWeatherTable> {
            val hourForecasts: MutableList<ForecastWeatherTable> = ArrayList(forecasts.size)
            val now = System.currentTimeMillis()

            forecasts.forEach {
                val forecastWeatherTable = ForecastWeatherTable(
                    cityId = cityId,
                    minT = it.main.minT,
                    maxT = it.main.maxT,
                    description = it.weather[0].description,
                    icon = it.weather[0].icon,
                    timestamp = now,
                    windSpeed = it.wind.windSpeed,
                    dateTime = it.dateTime * 1000,
                    temperature = it.main.temperature)
                hourForecasts.add(forecastWeatherTable)
            }
            return hourForecasts
        }
    }
}
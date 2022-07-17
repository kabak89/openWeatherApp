/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.data.db

import com.test.kabak.openweather.data.db.entity.CityTable
import com.test.kabak.openweather.data.db.mapper.CityMapper
import com.test.kabak.openweather.data.db.mapper.WeatherMapper
import com.test.kabak.openweather.domain.entity.City
import com.test.kabak.openweather.domain.entity.CurrentWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Storage(
    private val db: LocalDatabase,
) {
    suspend fun saveCity(newCityTable: CityTable) = db.cityDao().insert(newCityTable)

    fun getCitiesFlow(): Flow<List<City>> = db.cityDao().citiesFlow()
        .map { citiesList ->
            citiesList.map(CityMapper::mapCity)
        }

    fun getWeatherFlow(): Flow<List<CurrentWeather>> =
        db.currentWeatherDao().getWeatherFlow().map { weatherList ->
            weatherList.map(WeatherMapper::mapWeather)
        }

    suspend fun saveWeather(weather: CurrentWeather) {
        val table = WeatherMapper.mapWeatherToTable(weather)
        db.currentWeatherDao().insert(table)
    }
}
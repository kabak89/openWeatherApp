package com.test.kabak.openweather.core.storage

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [City::class, CurrentWeather::class, ForecastWeather::class], version = 8, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastWeatherDao(): ForecastWeatherDao
}

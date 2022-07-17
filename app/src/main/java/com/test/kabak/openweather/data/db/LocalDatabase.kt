package com.test.kabak.openweather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.kabak.openweather.data.db.dao.CityDao
import com.test.kabak.openweather.data.db.dao.CurrentWeatherDao
import com.test.kabak.openweather.data.db.dao.ForecastWeatherDao
import com.test.kabak.openweather.data.db.entity.CityTable
import com.test.kabak.openweather.data.db.entity.CurrentWeatherTable
import com.test.kabak.openweather.data.db.entity.ForecastWeatherTable

@Database(
    entities = [CityTable::class, CurrentWeatherTable::class, ForecastWeatherTable::class],
    version = 11,
    exportSchema = false,
)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun cityDao(): CityDao
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastWeatherDao(): ForecastWeatherDao
}
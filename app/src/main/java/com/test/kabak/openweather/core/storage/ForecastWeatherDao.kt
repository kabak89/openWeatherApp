package com.test.kabak.openweather.core.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface ForecastWeatherDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(newData: List<ForecastWeather>)

    @Query("SELECT * FROM ForecastWeather where cityId = :cityId AND timestamp > :timestamp")
    fun getCachedForecast(cityId: String, timestamp: Long): List<ForecastWeather>
}

package com.test.kabak.openweather.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.test.kabak.openweather.data.db.entity.ForecastWeatherTable

@Dao
interface ForecastWeatherDao {
    @Insert(onConflict = REPLACE)
    fun insertAll(newData: List<ForecastWeatherTable>)

    @Query("SELECT * FROM ForecastWeatherTable where cityId = :cityId AND timestamp > :timestamp")
    fun getCachedForecast(cityId: String, timestamp: Long): List<ForecastWeatherTable>
}
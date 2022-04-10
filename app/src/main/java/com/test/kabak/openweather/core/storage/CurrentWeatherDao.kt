package com.test.kabak.openweather.core.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = REPLACE)
    suspend fun insertAll(weatherArray: List<CurrentWeather>)

    @Insert(onConflict = REPLACE)
    suspend fun insert(weatherArray: CurrentWeather)

    @Query("SELECT * FROM CurrentWeather where cityId = :cityId")
    suspend fun getById(cityId: String): CurrentWeather?
}
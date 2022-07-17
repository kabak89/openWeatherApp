package com.test.kabak.openweather.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.test.kabak.openweather.data.db.entity.CurrentWeatherTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(weatherArray: CurrentWeatherTable)

    @Query("SELECT * FROM CurrentWeatherTable where cityId = :cityId")
    suspend fun getById(cityId: String): CurrentWeatherTable?

    @Query("SELECT * FROM CurrentWeatherTable")
    fun getWeatherFlow(): Flow<List<CurrentWeatherTable>>
}
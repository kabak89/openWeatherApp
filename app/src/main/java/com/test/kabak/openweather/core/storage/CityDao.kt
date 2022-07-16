package com.test.kabak.openweather.core.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @get:Query("SELECT * FROM City")
    val loadAll: LiveData<List<City>>

    @Query("SELECT * FROM City")
    suspend fun loadAllSynchronous(): List<City>

    @Insert(onConflict = REPLACE)
    fun insert(newCity: City)

    @Query("SELECT * FROM City")
    fun citiesFlow(): Flow<City>
}
package com.test.kabak.openweather.core.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface CityDao {
    @get:Query("SELECT * FROM city")
    val loadAll: LiveData<List<City>>

    @get:Query("SELECT * FROM city")
    val loadAllSynchronous: List<City>

    @Insert(onConflict = REPLACE)
    fun insert(newCity: City)
}

package com.test.kabak.openweather.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.test.kabak.openweather.data.db.entity.CityTable
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert(onConflict = REPLACE)
    suspend fun insert(newCityTable: CityTable)

    @get:Query("SELECT * FROM CityTable")
    val loadAll: LiveData<List<CityTable>>

    @Query("SELECT * FROM CityTable")
    suspend fun loadAllSynchronous(): List<CityTable>

    @Query("SELECT * FROM CityTable")
    fun citiesFlow(): Flow<List<CityTable>>
}
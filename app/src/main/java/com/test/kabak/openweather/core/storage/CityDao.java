package com.test.kabak.openweather.core.storage;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CityDao {
    @Insert(onConflict = REPLACE)
    void insert(City newCity);

    @Query("SELECT * FROM city")
    LiveData<List<City>> getAll();
}

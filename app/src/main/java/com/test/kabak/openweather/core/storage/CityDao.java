package com.test.kabak.openweather.core.storage;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CityDao {
    @Insert(onConflict = REPLACE)
    void insert(City newCity);

    @Query("SELECT * FROM city")
    LiveData<List<City>> getAll();
}

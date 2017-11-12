package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CityDao {
    @Insert(onConflict = REPLACE)
    void insert(City newCity);
}

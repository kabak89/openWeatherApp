package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CurrentWeatherDao {
    @Insert(onConflict = REPLACE)
    void insertAll(List<CurrentWeather> weatherArray);

    @Query("SELECT * FROM CurrentWeather where cityId = :cityId")
    CurrentWeather getById(String cityId);
}

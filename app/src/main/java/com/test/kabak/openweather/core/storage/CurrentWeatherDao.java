package com.test.kabak.openweather.core.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface CurrentWeatherDao {
    @Insert(onConflict = REPLACE)
    void insertAll(List<CurrentWeather> weatherArray);

    @Query("SELECT * FROM CurrentWeather where cityId = :cityId")
    CurrentWeather getById(String cityId);
}

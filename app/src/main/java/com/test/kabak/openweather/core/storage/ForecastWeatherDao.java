package com.test.kabak.openweather.core.storage;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ForecastWeatherDao {
    @Insert(onConflict = REPLACE)
    void insertAll(List<ForecastWeather> newData);

    @Query("SELECT * FROM ForecastWeather where cityId = :cityId AND timestamp > :timestamp")
    List<ForecastWeather> getCachedForecast(String cityId, long timestamp);
}

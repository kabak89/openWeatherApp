package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ForecastWeatherDao {
    @Insert(onConflict = REPLACE)
    void insertAll(List<ForecastWeather> newData);

    @Query("SELECT * FROM ForecastWeather where cityId = :cityId AND timestamp > :timestamp")
    List<ForecastWeather> getCachedForecast(String cityId, long timestamp);
}

package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {City.class, CurrentWeather.class, ForecastWeather.class}, version = 2, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract CityDao cityDao();
    public abstract CurrentWeatherDao currentWeatherDao();
}

package com.test.kabak.openweather.core.storage;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {City.class, CurrentWeather.class, ForecastWeather.class}, version = 7, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract CityDao cityDao();
    public abstract CurrentWeatherDao currentWeatherDao();
    public abstract ForecastWeatherDao forecastWeatherDao();
}

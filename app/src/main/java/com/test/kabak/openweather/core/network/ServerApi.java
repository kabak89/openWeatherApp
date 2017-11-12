package com.test.kabak.openweather.core.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerApi {
    public static final String TELEPORT_BASE_URL = "https://api.teleport.org/api/";
    public static final String WEATHER_BASE_URL = "http://api.openweathermap.org/data/2.5/";
    public static final String IMAGES_BASE_URL = "http://openweathermap.org/img/w/";
    
    public static TeleportApi getTeleportApi() {
        return TeleportApiHolder.TELEPORT_API;
    }

    public static WeatherApi getWeatherApi() {
        return WeatherApiHolder.WEATHER_API;
    }

    private static class TeleportApiHolder {
        static final TeleportApi TELEPORT_API = new Retrofit.Builder()
                .baseUrl(TELEPORT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TeleportApi.class);
    }

    private static class WeatherApiHolder {
        static final WeatherApi WEATHER_API = new Retrofit.Builder()
                .baseUrl(WEATHER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WeatherApi.class);
    }
}

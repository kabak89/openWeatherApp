package com.test.kabak.openweather.core.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather?appid=3edcc798fa2769cd37362534cc6e5a78&lang=ru&units=metric")
    Single<CurrentWeatherResponse> getCurrentWeather(@Query("id") String cityId);

    @GET("forecast?appid=3edcc798fa2769cd37362534cc6e5a78&lang=ru&units=metric")
    Single<CurrentWeatherResponse> getForecast(@Query("id") String cityId);
}

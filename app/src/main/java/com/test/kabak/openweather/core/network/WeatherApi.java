package com.test.kabak.openweather.core.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("weather?units=metric")
    Single<CurrentWeatherResponse> getCurrentWeather(@Query("id") String cityId);

    @GET("forecast?units=metric")
    Single<ForecastResponse> getForecast(@Query("id") String cityId);
}

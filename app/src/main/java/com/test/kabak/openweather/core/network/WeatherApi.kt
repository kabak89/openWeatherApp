package com.test.kabak.openweather.core.network

import com.test.kabak.openweather.core.network.dataClasses.CurrentWeatherResponse
import com.test.kabak.openweather.core.network.dataClasses.ForecastResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("weather?units=metric")
    suspend fun getCurrentWeather(@Query("id") cityId: String): CurrentWeatherResponse

    @GET("forecast?units=metric")
    fun getForecast(@Query("id") cityId: String): Single<ForecastResponse>
}
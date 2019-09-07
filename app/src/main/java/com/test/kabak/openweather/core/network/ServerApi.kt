package com.test.kabak.openweather.core.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.test.kabak.openweather.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

object ServerApi {
    val teleportApi by lazy {
        buildTeleportApi(Constants.TELEPORT_BASE_URL)
    }

    val weatherApi by lazy {
        buildWeatherApi(Constants.WEATHER_BASE_URL)
    }

    private fun buildTeleportApi(baseUrl: String): TeleportApi {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
                        .build())
                .baseUrl(baseUrl)
                .build()
                .create(TeleportApi::class.java)
    }

    private fun buildWeatherApi(baseUrl: String): WeatherApi {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder()
                        .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
                        .addInterceptor { chain ->
                            val original = chain.request()
                            val originalHttpUrl = original.url()

                            val url = originalHttpUrl.newBuilder()
                                    .addQueryParameter("appid", "3edcc798fa2769cd37362534cc6e5a78")
                                    .addQueryParameter("lang", Locale.getDefault().country)
                                    .build()

                            val requestBuilder = original.newBuilder()
                                    .url(url)

                            val request = requestBuilder.build()
                            return@addInterceptor chain.proceed(request)
                        }
                        .build())
                .baseUrl(baseUrl)
                .build()
                .create(WeatherApi::class.java)
    }
}
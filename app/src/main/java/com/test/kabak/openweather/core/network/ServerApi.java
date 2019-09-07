package com.test.kabak.openweather.core.network;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
                .client(
                        new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                            @Override
                            public Response intercept(@NonNull Chain chain) throws IOException {
                                Request original = chain.request();
                                HttpUrl originalHttpUrl = original.url();

                                HttpUrl url = originalHttpUrl.newBuilder()
                                        .addQueryParameter("appid", "3edcc798fa2769cd37362534cc6e5a78")
                                        .addQueryParameter("lang", Locale.getDefault().getCountry())
                                        .build();

                                Request.Builder requestBuilder = original.newBuilder()
                                        .url(url);

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        }).build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(WeatherApi.class);
    }
}

package com.test.kabak.openweather.core.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServerApi {
    public static final String TELEPORT_BASE_URL = "https://api.teleport.org/api/";
    
    public static TeleportApi getTeleportApi() {
        return InstanceHolder.TELEPORT_API;
    }

    private static class InstanceHolder {
        static final TeleportApi TELEPORT_API = new Retrofit.Builder()
                .baseUrl(TELEPORT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(TeleportApi.class);
    }
}

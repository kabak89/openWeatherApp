package com.test.kabak.openweather.core.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ForecastResponse {
    @SerializedName("list")
    public ArrayList<ForecastObject> forecasts;

    public static class ForecastObject {
        @SerializedName("dt")
        public long dateTime;

        @SerializedName("main")
        public MainObject main;
    }

    public static class MainObject {
        @SerializedName("temp")
        public float temperature;
    }
}

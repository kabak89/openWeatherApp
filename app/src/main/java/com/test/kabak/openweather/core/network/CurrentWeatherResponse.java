package com.test.kabak.openweather.core.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CurrentWeatherResponse {
    public ArrayList<WeatherObject> weather;
    public MainObject main;

    public static class WeatherObject {
        public String description;
        public String icon;
    }

    public static class MainObject {
        @SerializedName("temp_min")
        public float tempMin;

        @SerializedName("temp_max")
        public float tempMax;
    }
}

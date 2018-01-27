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

        @SerializedName("weather")
        public ArrayList<WeatherObject> weather;

        @SerializedName("wind")
        public WindObject wind;
    }

    public static class MainObject {
        @SerializedName("temp")
        public float temperature;

        @SerializedName("temp_min")
        public float minT;

        @SerializedName("temp_max")
        public float maxT;
    }

    public static class WeatherObject {
        @SerializedName("description")
        public String description;

        @SerializedName("icon")
        public String icon;
    }

    public static class WindObject {
        @SerializedName("speed")
        public float windSpeed;
    }
}

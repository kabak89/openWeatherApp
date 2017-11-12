package com.test.kabak.openweather.core.network;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SearchCityResponse {
    @SerializedName("_embedded")
    public Embedded embedded;

    public static class Embedded {
        @SerializedName("city:search-results")
        public ArrayList<SearchResult> searchList;
    }

    public static class SearchResult {
        @SerializedName("matching_full_name")
        public String name;
        @SerializedName("_links")
        public Links links;
    }

    public static class Links {
        @SerializedName("city:item")
        public CityItem cityItem;
    }

    public static class CityItem {
        @SerializedName("href")
        public String link;
    }
}


package com.test.kabak.openweather.core.network;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TeleportApi {
    @GET("cities/")
    Single<SearchCityResponse> getCities(@Query("search") String searchString);

    @GET("cities/")
    Single<Object> getCities(@Query("geonameid") int cityId);
}

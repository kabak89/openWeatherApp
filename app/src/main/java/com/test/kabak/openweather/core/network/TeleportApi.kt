package com.test.kabak.openweather.core.network

import com.test.kabak.openweather.core.network.dataClasses.SearchCityResponse

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface TeleportApi {
    @GET("cities/")
    fun getCities(@Query("search") searchString: String): Single<SearchCityResponse>
}

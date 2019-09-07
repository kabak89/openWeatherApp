package com.test.kabak.openweather.core.network.dataClasses

import com.google.gson.annotations.SerializedName
import java.util.*

data class SearchCityResponse(
        @SerializedName("_embedded")
        val embedded: Embedded
) {

    data class Embedded(
            @SerializedName("city:search-results")
            val searchList: ArrayList<SearchResult>
    )

    data class SearchResult(
            @SerializedName("matching_full_name")
            val name: String,

            @SerializedName("_links")
            val links: Links
    )

    data class Links(
            @SerializedName("city:item")
            val cityItem: CityItem
    )

    data class CityItem(
            @SerializedName("href")
            val link: String
    )
}


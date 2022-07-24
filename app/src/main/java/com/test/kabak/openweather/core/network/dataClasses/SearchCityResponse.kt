package com.test.kabak.openweather.core.network.dataClasses

import com.google.gson.annotations.SerializedName

class SearchCityResponse(
    @SerializedName("_embedded") val embedded: Embedded,
) {
    class Embedded(
        @SerializedName("city:search-results")
        val searchList: ArrayList<SearchResult>,
    )

    class SearchResult(
        @SerializedName("matching_full_name") val name: String,
        @SerializedName("_links") val links: Links,
    )

    class Links(
        @SerializedName("city:item") val cityItem: CityItem,
    )

    data class CityItem(
        @SerializedName("href") val link: String,
    )
}
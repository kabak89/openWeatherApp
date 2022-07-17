package com.test.kabak.openweather.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.test.kabak.openweather.core.network.dataClasses.CurrentWeatherResponse

@Entity(foreignKeys = [ForeignKey(entity = CityTable::class,
    parentColumns = ["id"],
    childColumns = ["cityId"])])
data class CurrentWeatherTable(
    @PrimaryKey
    val cityId: String,
    val minT: Float,
    val maxT: Float,
    val description: String,
    val icon: String,
    val timestamp: Long,
    val windSpeed: Float,
) {
    fun isOutdated(): Boolean {
        return System.currentTimeMillis() - timestamp > CURRENT_WEATHER_MAX_LIVE_TIME
    }

    companion object {
        const val CURRENT_WEATHER_MAX_LIVE_TIME = 1000 * 60 * 10

        fun buildFromServerResponse(
            cityId: String,
            weatherResponse: CurrentWeatherResponse,
        ): CurrentWeatherTable {
            val weatherObject = weatherResponse.weather.first()

            return CurrentWeatherTable(
                cityId,
                weatherResponse.main.tempMin,
                weatherResponse.main.tempMax,
                weatherObject.description,
                weatherObject.icon,
                System.currentTimeMillis(),
                weatherResponse.wind.windSpeed
            )
        }
    }
}
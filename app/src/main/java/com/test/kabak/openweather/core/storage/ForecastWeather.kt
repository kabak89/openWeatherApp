package com.test.kabak.openweather.core.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.*

@Entity(foreignKeys = [ForeignKey(entity = City::class, parentColumns = ["cityId"], childColumns = ["cityId"])], primaryKeys = ["cityId", "dateTime"])
data class ForecastWeather(
        val cityId: String,
        val minT: Float,
        val maxT: Float,
        val description: String?,
        val icon: String?,
        val timestamp: Long,
        val windSpeed: Float,
        val dateTime: Long,
        val temperature: Float) {

    class ForecastWeatherComparator : Comparator<ForecastWeather> {
        override fun compare(o1: ForecastWeather, o2: ForecastWeather): Int {
            return when {
                o1.dateTime > o2.dateTime -> 1
                o1.dateTime < o2.dateTime -> -1
                else -> 0
            }
        }
    }
}

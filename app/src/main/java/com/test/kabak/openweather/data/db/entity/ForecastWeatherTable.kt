package com.test.kabak.openweather.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    foreignKeys = [
        ForeignKey(entity = CityTable::class, parentColumns = ["id"], childColumns = ["cityId"])
    ],
    primaryKeys = ["cityId", "dateTime"]
)
data class ForecastWeatherTable(
    val cityId: String,
    val minT: Float,
    val maxT: Float,
    val description: String?,
    val icon: String?,
    val timestamp: Long,
    val windSpeed: Float,
    val dateTime: Long,
    val temperature: Float,
) {

    class ForecastWeatherComparator : Comparator<ForecastWeatherTable> {
        override fun compare(o1: ForecastWeatherTable, o2: ForecastWeatherTable): Int {
            return when {
                o1.dateTime > o2.dateTime -> 1
                o1.dateTime < o2.dateTime -> -1
                else -> 0
            }
        }
    }
}
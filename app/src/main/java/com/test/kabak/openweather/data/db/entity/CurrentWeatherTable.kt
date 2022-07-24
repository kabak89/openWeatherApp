package com.test.kabak.openweather.data.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = CityTable::class,
            parentColumns = ["id"],
            childColumns = ["cityId"]
        )
    ]
)
data class CurrentWeatherTable(
    @PrimaryKey
    val cityId: String,
    val minT: Float,
    val maxT: Float,
    val description: String,
    val icon: String,
    val timestamp: Long,
    val windSpeed: Float,
)
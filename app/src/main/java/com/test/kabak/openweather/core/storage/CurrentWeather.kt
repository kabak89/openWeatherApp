package com.test.kabak.openweather.core.storage

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(foreignKeys = [ForeignKey(entity = City::class, parentColumns = ["cityId"], childColumns = ["cityId"])])
data class CurrentWeather (
    @PrimaryKey
    val cityId: String,
    val minT: Float,
    val maxT: Float,
    val description: String?,
    val icon: String?,
    val timestamp: Long,
    val windSpeed: Float
)
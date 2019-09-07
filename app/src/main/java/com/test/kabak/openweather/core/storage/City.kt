package com.test.kabak.openweather.core.storage

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class City (
    @PrimaryKey
    val cityId: String,

    val name: String
)

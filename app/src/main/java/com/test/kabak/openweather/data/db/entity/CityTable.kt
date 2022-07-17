package com.test.kabak.openweather.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CityTable(
    @PrimaryKey
    val id: String,

    val name: String,
)
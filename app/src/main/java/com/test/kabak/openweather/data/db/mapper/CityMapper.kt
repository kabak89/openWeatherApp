package com.test.kabak.openweather.data.db.mapper

import com.test.kabak.openweather.data.db.entity.CityTable
import com.test.kabak.openweather.domain.entity.City

object CityMapper {
    fun mapCity(cityTable: CityTable): City =
        City(
            id = cityTable.id,
            name = cityTable.name,
        )
}
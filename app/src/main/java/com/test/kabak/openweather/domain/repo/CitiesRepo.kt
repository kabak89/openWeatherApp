/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.domain.repo

import com.test.kabak.openweather.domain.entity.City
import kotlinx.coroutines.flow.Flow

interface CitiesRepo {
    fun getAllCities(): Flow<List<City>>
}
/**
 * Created by Eugeny Kabak on 16.07.2022
 */
package com.test.kabak.openweather.domain.logic

import com.test.kabak.openweather.domain.entity.City
import com.test.kabak.openweather.domain.repo.CitiesRepo
import kotlinx.coroutines.flow.Flow

class CitiesInteractor(
    private val citiesRepo: CitiesRepo,
) {
    fun getCities(): Flow<List<City>> = citiesRepo.getCitiesFlow()
}
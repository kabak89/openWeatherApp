/**
 * Created by Eugeny Kabak on 17.07.2022
 */
package com.test.kabak.openweather.data.repo_impl

import com.test.kabak.openweather.data.db.Storage
import com.test.kabak.openweather.domain.entity.City
import com.test.kabak.openweather.domain.repo.CitiesRepo
import kotlinx.coroutines.flow.Flow


class CitiesRepoImpl(
    private val storage: Storage,
) : CitiesRepo {
    override fun getAllCities(): Flow<List<City>> = storage.getCitiesFlow()
}
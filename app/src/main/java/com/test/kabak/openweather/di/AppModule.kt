/**
 * Created by Eugeny Kabak on 15.05.2022
 */
package com.test.kabak.openweather.di

import androidx.room.Room
import com.test.kabak.openweather.data.db.LocalDatabase
import com.test.kabak.openweather.data.db.Storage
import com.test.kabak.openweather.data.network.WeatherService
import com.test.kabak.openweather.data.repo_impl.CitiesRepoImpl
import com.test.kabak.openweather.data.repo_impl.WeatherRepoImpl
import com.test.kabak.openweather.domain.logic.CitiesInteractor
import com.test.kabak.openweather.domain.logic.WeatherInteractor
import com.test.kabak.openweather.domain.repo.CitiesRepo
import com.test.kabak.openweather.domain.repo.WeatherRepo
import com.test.kabak.openweather.presentation.list.CitiesListMapper
import com.test.kabak.openweather.presentation.list.CitiesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    presentation()
    domain()
    data()
}

private fun Module.data() {
    single {
        Room
            .databaseBuilder(get(), LocalDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }
    singleOf(::Storage)
    singleOf(::WeatherService)
    singleOf(::WeatherRepoImpl) bind WeatherRepo::class
    singleOf(::CitiesRepoImpl) bind CitiesRepo::class
}

private fun Module.domain() {
    singleOf(::WeatherInteractor)
    singleOf(::CitiesInteractor)
}

private fun Module.presentation() {
    singleOf(::CitiesListMapper)
    viewModelOf(::CitiesListViewModel)
}
/**
 * Created by Eugeny Kabak on 15.05.2022
 */
package com.test.kabak.openweather.di

import androidx.room.Room
import com.test.kabak.openweather.core.storage.LocalDatabase
import com.test.kabak.openweather.presentation.list.CitiesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CitiesListViewModel)

    single {
        Room
            .databaseBuilder(get(), LocalDatabase::class.java, "database")
            .fallbackToDestructiveMigration()
            .build()
    }
}
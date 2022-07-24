/**
 * Created by Eugeny Kabak on 15.05.2022
 */
package com.test.kabak.openweather.di

import androidx.room.Room
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.test.kabak.openweather.BuildConfig
import com.test.kabak.openweather.core.network.Constants
import com.test.kabak.openweather.core.network.WeatherApi
import com.test.kabak.openweather.data.db.LocalDatabase
import com.test.kabak.openweather.data.db.Storage
import com.test.kabak.openweather.data.network.WeatherService
import com.test.kabak.openweather.data.repo_impl.CitiesRepoImpl
import com.test.kabak.openweather.data.repo_impl.WeatherRepoImpl
import com.test.kabak.openweather.domain.logic.CitiesInteractor
import com.test.kabak.openweather.domain.logic.WeatherInteractor
import com.test.kabak.openweather.domain.repo.CitiesRepo
import com.test.kabak.openweather.domain.repo.WeatherRepo
import com.test.kabak.openweather.presentation.ErrorConverter
import com.test.kabak.openweather.presentation.ErrorTranslator
import com.test.kabak.openweather.presentation.list.CitiesListMapper
import com.test.kabak.openweather.presentation.list.CitiesListViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

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
    single {
        Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
                .addInterceptor { chain ->
                    val original = chain.request()
                    val originalHttpUrl = original.url

                    val url = originalHttpUrl.newBuilder()
                        .addQueryParameter("appid", "3edcc798fa2769cd37362534cc6e5a78")
                        .addQueryParameter("lang", Locale.getDefault().country)
                        .build()

                    val requestBuilder = original.newBuilder()
                        .url(url)

                    val request = requestBuilder.build()
                    return@addInterceptor chain.proceed(request)
                }
                .build())
            .baseUrl(Constants.WEATHER_BASE_URL)
            .build()
            .create(WeatherApi::class.java)
    } bind WeatherApi::class
}

private fun Module.domain() {
    singleOf(::WeatherInteractor)
    singleOf(::CitiesInteractor)
}

private fun Module.presentation() {
    singleOf(::ErrorTranslator)
    singleOf(::ErrorConverter)

    singleOf(::CitiesListMapper)
    viewModelOf(::CitiesListViewModel)
}
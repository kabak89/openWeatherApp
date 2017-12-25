package com.test.kabak.openweather.core.viewModels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.repositories.CitiesRepository;
import com.test.kabak.openweather.core.storage.City;

import java.util.List;

public class CitiesListViewModel extends ViewModel {
    CitiesRepository citiesRepository = new CitiesRepository();

    public LiveData<Resource<List<ListWeatherObject>>> getListWeather() {
        return Transformations.switchMap(citiesRepository.getCities(), new Function<List<City>, LiveData<Resource<List<ListWeatherObject>>>>() {
            @Override
            public LiveData<Resource<List<ListWeatherObject>>> apply(List<City> input) {
                return citiesRepository.getCitiesWeather(input);
            }
        });
    }
}

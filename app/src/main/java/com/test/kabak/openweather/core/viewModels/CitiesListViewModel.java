package com.test.kabak.openweather.core.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.repositories.CitiesRepository;
import com.test.kabak.openweather.core.storage.CurrentWeather;

import java.util.List;

public class CitiesListViewModel extends ViewModel {
    CitiesRepository citiesRepository = new CitiesRepository();

    LiveData<Resource<List<CurrentWeather>>> listWeather;

    public LiveData<Resource<List<CurrentWeather>>> getListWeather() {
        if(listWeather == null) {
            listWeather =  citiesRepository.getCities();
        }

        return listWeather;
    }
}

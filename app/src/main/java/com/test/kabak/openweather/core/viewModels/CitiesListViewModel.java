package com.test.kabak.openweather.core.viewModels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.test.kabak.openweather.core.repositories.CitiesRepository;
import com.test.kabak.openweather.core.storage.City;
import com.test.kabak.openweather.core.storage.CurrentWeather;

import java.util.List;

public class CitiesListViewModel extends ViewModel {
    CitiesRepository citiesRepository = new CitiesRepository();

    public final LiveData<List<ListWeatherObject>> weatherLiveData
            = Transformations.switchMap(citiesRepository.getCities(), new Function<List<City>, LiveData<List<ListWeatherObject>>>() {
        @Override
        public LiveData<List<ListWeatherObject>> apply(List<City> input) {
            return citiesRepository.getCitiesWeather(input);
        }
    });



//    LiveData<Resource<List<CurrentWeather>>> listWeather = new MutableLiveData<>();
//
//    public CitiesListViewModel() {
//        citiesRepository.getCities().observe(, new Observer<List<City>>() {
//            @Override
//            public void onChanged(@Nullable List<City> cities) {
//
//            }
//        });
//    }
//
//    public LiveData<Resource<List<CurrentWeather>>> getListWeather() {
//        return listWeather;
//    }

//    final LiveData<List<City>> cityLiveData = citiesRepository.getCitiesWeather();

//    public final LiveData<CurrentWeather> currentWeatherLiveData =
//            Transformations.map(cityLiveData, new Function<List<City>, CurrentWeather>() {
//                @Override
//                public CurrentWeather apply(List<City> input) {
//                    CurrentWeather currentWeather = new CurrentWeather();
//                    return currentWeather;
//                }
//            });

    /*public LiveData<Resource<List<CurrentWeather>>> getListWeather() {
        if(listWeather == null) {
            listWeather =  citiesRepository.getCitiesWeather();
        }

        return listWeather;
    }  */


}

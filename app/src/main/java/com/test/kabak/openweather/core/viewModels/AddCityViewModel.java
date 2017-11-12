package com.test.kabak.openweather.core.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.repositories.AddCityRepository;
import com.test.kabak.openweather.core.storage.City;

import java.util.List;

import io.reactivex.Completable;

public class AddCityViewModel extends ViewModel {
    AddCityRepository addCityRepository = new AddCityRepository();
    LiveData<Resource<List<City>>> searchLiveData;

    public LiveData<Resource<List<City>>> setSearchLiveData() {
        if(searchLiveData == null) {
            searchLiveData = addCityRepository.getSearchLiveData();
        }

        return searchLiveData;
    }

    public void findCities(String searchString) {
        addCityRepository.findCities(searchString);
    }

    public Completable addCity(City city) {
        return addCityRepository.saveCity(city);
    }
}

package com.test.kabak.openweather.core.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.repositories.SearchRepository;
import com.test.kabak.openweather.core.storage.City;

import java.util.List;

public class AddCityViewModel extends ViewModel {
    SearchRepository searchRepository = new SearchRepository();
    LiveData<Resource<List<City>>> searchLiveData;

    public LiveData<Resource<List<City>>> setSearchLiveData() {
        if(searchLiveData == null) {
            searchLiveData = searchRepository.getSearchLiveData();
        }

        return searchLiveData;
    }

    public void findCities(String searchString) {
        searchRepository.findCities(searchString);
    }
}

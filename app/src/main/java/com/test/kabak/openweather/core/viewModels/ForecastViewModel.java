package com.test.kabak.openweather.core.viewModels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.repositories.ForecastRepository;

public class ForecastViewModel extends ViewModel{
    LiveData<Resource<ForecastDayObject>> forecastLiveData;
    ForecastRepository forecastRepository = new ForecastRepository();

    public LiveData<Resource<ForecastDayObject>> getForecast(String cityId) {
        if(forecastLiveData == null) {
            forecastLiveData = forecastRepository.getForecast(cityId);
        }

        return forecastLiveData;
    }
}

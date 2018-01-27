package com.test.kabak.openweather.core.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.network.ForecastResponse;
import com.test.kabak.openweather.core.network.ServerApi;
import com.test.kabak.openweather.core.storage.ForecastWeather;
import com.test.kabak.openweather.core.viewModels.ForecastDayObject;

import java.util.ArrayList;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ForecastRepository {
    MutableLiveData<Resource<ForecastDayObject>> data = new MutableLiveData<>();

    public LiveData<Resource<ForecastDayObject>> getForecast(final String cityId) {
        data.setValue(new Resource<ForecastDayObject>(Resource.LOADING, null, null));

        Single<ForecastResponse> responseSingle = ServerApi.getWeatherApi().getForecast(cityId);

        responseSingle
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ForecastResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ForecastResponse forecastResponse) {
                        ArrayList<ForecastResponse.ForecastObject> forecasts = forecastResponse.forecasts;
                        ForecastDayObject forecastDayObject = new ForecastDayObject();
                        forecastDayObject.forecasts = new ArrayList<>(forecasts.size());

                        for(ForecastResponse.ForecastObject currentForecast : forecasts) {
                            ForecastWeather forecastWeather = new ForecastWeather();
                            forecastWeather.cityId = cityId;;
                            forecastWeather.temperature = currentForecast.main.temperature;
                            forecastWeather.dateTime = currentForecast.dateTime * 1000;
                            forecastWeather.windSpeed = currentForecast.wind.windSpeed;
                            forecastWeather.icon = currentForecast.weather.get(0).icon;
                            forecastWeather.description = currentForecast.weather.get(0).description;
                            forecastWeather.minT = currentForecast.main.minT;
                            forecastWeather.maxT = currentForecast.main.maxT;

                            forecastDayObject.forecasts.add(forecastWeather);
                        }

                        data.setValue(new Resource<>(Resource.COMPLETED, forecastDayObject, null));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("", "");
                    }
                });

        return data;
    }
}

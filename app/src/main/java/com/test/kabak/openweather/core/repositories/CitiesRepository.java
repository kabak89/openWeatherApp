package com.test.kabak.openweather.core.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.test.kabak.openweather.core.network.CurrentWeatherResponse;
import com.test.kabak.openweather.core.network.ServerApi;
import com.test.kabak.openweather.core.storage.City;
import com.test.kabak.openweather.core.storage.CurrentWeather;
import com.test.kabak.openweather.core.storage.DatabaseManager;
import com.test.kabak.openweather.core.viewModels.ListWeatherObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CitiesRepository {
    MutableLiveData<List<ListWeatherObject>> citiesWeather = new MutableLiveData<>();

    public LiveData<List<City>> getCities() {
        return DatabaseManager.getDatabase().cityDao().getAll();
    }

    public LiveData<List<ListWeatherObject>> getCitiesWeather(final List<City> input) {
        Single
                .create(new SingleOnSubscribe<List<ListWeatherObject>>() {
                    @Override
                    public void subscribe(final SingleEmitter<List<ListWeatherObject>> e) throws Exception {
                        final List<ListWeatherObject> result = new ArrayList<>(input.size());

                        for (final City currentCity : input) {
                            Single<CurrentWeatherResponse> currentWeather = ServerApi.getWeatherApi().getCurrentWeather(currentCity.cityId);

                            currentWeather.subscribe(new SingleObserver<CurrentWeatherResponse>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(CurrentWeatherResponse currentWeatherResponse) {
                                    CurrentWeather currentWeather = new CurrentWeather();
                                    currentWeather.cityId = currentCity.cityId;
                                    CurrentWeatherResponse.WeatherObject weatherObject = currentWeatherResponse.weather.get(0);
                                    currentWeather.description = weatherObject.description;
                                    currentWeather.icon = weatherObject.icon;
                                    currentWeather.minT = currentWeatherResponse.main.tempMin;
                                    currentWeather.maxT = currentWeatherResponse.main.tempMax;

                                    ListWeatherObject listWeatherObject = new ListWeatherObject();
                                    listWeatherObject.city = currentCity;
                                    listWeatherObject.currentWeather = currentWeather;
                                    result.add(listWeatherObject);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            });
                        }

                        e.onSuccess(result);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ListWeatherObject>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ListWeatherObject> currentWeathers) {
                        citiesWeather.setValue(currentWeathers);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });

        return citiesWeather;
    }
}

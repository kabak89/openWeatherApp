package com.test.kabak.openweather.core.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.test.kabak.openweather.core.Resource;
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
    MutableLiveData<Resource<List<ListWeatherObject>>> citiesWeather = new MutableLiveData<>();

    public LiveData<List<City>> getCities() {
        return DatabaseManager.getDatabase().cityDao().getAll();
    }

    public LiveData<Resource<List<ListWeatherObject>>> getCitiesWeather(final List<City> input) {
        Resource <List<ListWeatherObject>> resource = new Resource(Resource.LOADING, null, null);
        citiesWeather.setValue(resource);

        Single
                .create(new SingleOnSubscribe<Resource<List<ListWeatherObject>>>() {
                    @Override
                    public void subscribe(final SingleEmitter<Resource<List<ListWeatherObject>>> e) throws Exception {
                        final List<ListWeatherObject> result = new ArrayList<>(input.size());
                        final List<CurrentWeather> weatherToSave = new ArrayList<>(input.size());

                        Exception exception = null;

                        for (final City currentCity : input) {
                            final String cityId = currentCity.cityId;
                            Single<CurrentWeatherResponse> currentWeatherSingle = ServerApi.getWeatherApi().getCurrentWeather(currentCity.cityId);

                            try {
                                CurrentWeatherResponse currentWeatherResponse = currentWeatherSingle.blockingGet();
                                CurrentWeather currentWeather = new CurrentWeather();
                                currentWeather.cityId = cityId;
                                CurrentWeatherResponse.WeatherObject weatherObject = currentWeatherResponse.weather.get(0);
                                currentWeather.description = weatherObject.description;
                                currentWeather.icon = weatherObject.icon;
                                currentWeather.minT = currentWeatherResponse.main.tempMin;
                                currentWeather.maxT = currentWeatherResponse.main.tempMax;
                                currentWeather.timestamp = System.currentTimeMillis();

                                ListWeatherObject listWeatherObject = new ListWeatherObject();
                                listWeatherObject.city = currentCity;
                                listWeatherObject.currentWeather = currentWeather;
                                result.add(listWeatherObject);

                                weatherToSave.add(currentWeather);
                            } catch (Exception networkException) {
                                exception = networkException;
                                CurrentWeather cachedWeather = DatabaseManager.getDatabase().currentWeatherDao().getById(cityId);

                                if(cachedWeather != null) {
                                    ListWeatherObject listWeatherObject = new ListWeatherObject();
                                    listWeatherObject.city = currentCity;
                                    listWeatherObject.currentWeather = cachedWeather;
                                    result.add(listWeatherObject);
                                }
                            }
                        }

                        DatabaseManager.getDatabase().currentWeatherDao().insertAll(weatherToSave);
                        Resource<List<ListWeatherObject>> resultResource = new Resource<>(Resource.COMPLETED, result, exception);
                        e.onSuccess(resultResource);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Resource<List<ListWeatherObject>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Resource<List<ListWeatherObject>> currentWeathersResource) {
                        Resource <List<ListWeatherObject>> resource = new Resource(Resource.COMPLETED, currentWeathersResource.data, currentWeathersResource.exception);
                        citiesWeather.setValue(resource);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Resource <List<ListWeatherObject>> resource = new Resource(Resource.COMPLETED, null, new Exception());
                        citiesWeather.setValue(resource);
                    }
                });

        return citiesWeather;
    }
}

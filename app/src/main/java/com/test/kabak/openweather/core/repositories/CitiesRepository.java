package com.test.kabak.openweather.core.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.storage.CurrentWeather;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class CitiesRepository {
    AtomicBoolean requestInProgress = new AtomicBoolean(false);
    MutableLiveData<Resource<List<CurrentWeather>>> listWeatherLiveData = new MutableLiveData<>();

    public LiveData<Resource<List<CurrentWeather>>> getCities() {
        Resource<List<CurrentWeather>> resource = new Resource<>(Resource.LOADING, null, null);
        listWeatherLiveData.setValue(resource);

        if (requestInProgress.get() == false) {
            loadCities();
        }

        return listWeatherLiveData;
    }

    private void loadCities() {
        requestInProgress.set(true);

        /*Single<CitiesResponse> citiesObservable = ServerApi.getInstance().findCities();

        citiesObservable
                .flatMap(new Function<CitiesResponse, SingleSource<Resource<List<City>>>>() {
                    @Override
                    public SingleSource<Resource<List<City>>> apply(final CitiesResponse citiesResponse) throws Exception {
                        return Single.create(new SingleOnSubscribe<Resource<List<City>>>() {
                            @Override
                            public void subscribe(SingleEmitter<Resource<List<City>>> e) throws Exception {
                                CityDao cityDao = DatabaseManager.getDatabase().cityDao();
                                cityDao.deleteAll();
                                List<City> cities = citiesResponse.cities;
                                cityDao.insertAll(cities);
                                SharedPreferencesManager.putBoolean(CITIES_DOWNLOADED_KEY, true);

                                sortCities(cities);

                                Resource<List<City>> resource = new Resource<>(Resource.COMPLETED, cities, null);
                                e.onSuccess(resource);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Resource<List<City>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Resource<List<City>> listResource) {
                        listWeatherLiveData.postValue(listResource);
                        requestInProgress.set(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Completable
                                .create(new CompletableOnSubscribe() {
                                    @Override
                                    public void subscribe(CompletableEmitter e) throws Exception {
                                        CityDao cityDao = DatabaseManager.getDatabase().cityDao();
                                        List<City> cities = cityDao.getAll();
                                        boolean citiesDownloadedOnce = SharedPreferencesManager.getBoolean(CITIES_DOWNLOADED_KEY, false);

                                        Exception exception;

                                        if (cities.isEmpty()) {
                                            if (citiesDownloadedOnce) {
                                                exception = new UpdateException();
                                            } else {
                                                exception = new DownloadException();
                                            }
                                        } else {
                                            exception = new UpdateException();
                                        }

                                        sortCities(cities);

                                        Resource<List<City>> resource = new Resource<>(Resource.COMPLETED, cities, exception);
                                        listWeatherLiveData.postValue(resource);
                                        requestInProgress.set(false);
                                        e.onComplete();
                                    }
                                })
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe();
                    }
                });  */
    }
}

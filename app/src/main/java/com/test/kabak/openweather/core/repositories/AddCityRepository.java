package com.test.kabak.openweather.core.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.network.SearchCityResponse;
import com.test.kabak.openweather.core.network.ServerApi;
import com.test.kabak.openweather.core.storage.City;
import com.test.kabak.openweather.core.storage.DatabaseManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.internal.operators.completable.CompletableFromAction;
import io.reactivex.schedulers.Schedulers;

public class AddCityRepository {
    static final Pattern DIGIT_EXTRACT_PATTERN = Pattern.compile("\\D+");

    MutableLiveData<Resource<List<City>>> searchLiveData = new MutableLiveData<>();
    String currentSearchString;

    public LiveData<Resource<List<City>>> getSearchLiveData() {
        return searchLiveData;
    }

    public void findCities(String searchString) {
        Resource<List<City>> resource = new Resource<>(Resource.LOADING, null, null);
        searchLiveData.setValue(resource);
        currentSearchString = searchString;
        performSearchRequest(searchString);
    }

    public Completable saveCity(final City city) {
        return new CompletableFromAction(new Action() {
            @Override
            public void run() throws Exception {
                DatabaseManager.getDatabase().cityDao().insert(city);
            }
        });
    }

    private void performSearchRequest(final String searchString) {
        if(searchString.isEmpty()) {
            List<City> emptyResult = new ArrayList<>(0);
            Resource<List<City>> listResource = new Resource<>(Resource.COMPLETED, emptyResult, null);
            searchLiveData.setValue(listResource);
            return;
        }

        Single<SearchCityResponse> searchObservable = ServerApi.getTeleportApi().getCities(searchString);

        searchObservable
                .toObservable()
                .flatMap(new Function<SearchCityResponse, ObservableSource<Resource<List<City>>>>() {
                    @Override
                    public ObservableSource<Resource<List<City>>> apply(final SearchCityResponse searchCityResponse) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Resource<List<City>>>() {
                            @Override
                            public void subscribe(ObservableEmitter<Resource<List<City>>> e) throws Exception {
                                ArrayList<SearchCityResponse.SearchResult> results = searchCityResponse.embedded.searchList;
                                List<City> cities = new ArrayList<>(results.size());

                                for(SearchCityResponse.SearchResult currentResult : results) {
                                    City city = new City();
                                    city.name = currentResult.name;
                                    city.cityId = DIGIT_EXTRACT_PATTERN.matcher(currentResult.links.cityItem.link).replaceAll("");
                                    cities.add(city);
                                }

                                Resource<List<City>> listResource = new Resource<>(Resource.COMPLETED, cities, null);
                                e.onNext(listResource);
                                e.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Resource<List<City>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Resource<List<City>> listResource) {
                        if(currentSearchString.equals(searchString) == false) {
                            return;
                        }

                        searchLiveData.setValue(listResource);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Resource<List<City>> listResource = new Resource<>(Resource.COMPLETED, null, new Exception(e.getMessage()));
                        searchLiveData.setValue(listResource);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

package com.test.kabak.openweather.core.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.network.ServerApi;
import com.test.kabak.openweather.core.network.dataClasses.SearchCityResponse;
import com.test.kabak.openweather.data.db.entity.CityTable;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class AddCityRepository {
    static final Pattern DIGIT_EXTRACT_PATTERN = Pattern.compile("\\D+");

    MutableLiveData<Resource<List<CityTable>>> searchLiveData = new MutableLiveData<>();
    String currentSearchString;

    public LiveData<Resource<List<CityTable>>> getSearchLiveData() {
        return searchLiveData;
    }

    public void findCities(String searchString) {
        Resource<List<CityTable>> resource = new Resource<>(Resource.LOADING, null, null);
        searchLiveData.setValue(resource);
        currentSearchString = searchString;
        performSearchRequest(searchString);
    }

    private void performSearchRequest(final String searchString) {
        if(searchString.isEmpty()) {
            List<CityTable> emptyResult = new ArrayList<>(0);
            Resource<List<CityTable>> listResource = new Resource<>(Resource.COMPLETED, emptyResult, null);
            searchLiveData.setValue(listResource);
            return;
        }

        Single<SearchCityResponse> searchObservable = ServerApi.INSTANCE.getTeleportApi().getCities(searchString);

        searchObservable
                .toObservable()
                .flatMap(new Function<SearchCityResponse, ObservableSource<Resource<List<CityTable>>>>() {
                    @Override
                    public ObservableSource<Resource<List<CityTable>>> apply(final SearchCityResponse searchCityResponse) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<Resource<List<CityTable>>>() {
                            @Override
                            public void subscribe(ObservableEmitter<Resource<List<CityTable>>> e) throws Exception {
                                ArrayList<SearchCityResponse.SearchResult> results = searchCityResponse.getEmbedded().getSearchList();
                                List<CityTable> cities = new ArrayList<>(results.size());

                                for (SearchCityResponse.SearchResult currentResult : results) {
                                    String cityId = DIGIT_EXTRACT_PATTERN.matcher(currentResult.getLinks().getCityItem().getLink()).replaceAll("");
                                    CityTable cityTable = new CityTable(cityId, currentResult.getName());
                                    cities.add(cityTable);
                                }

                                Resource<List<CityTable>> listResource = new Resource<>(Resource.COMPLETED, cities, null);
                                e.onNext(listResource);
                                e.onComplete();
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Resource<List<CityTable>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Resource<List<CityTable>> listResource) {
                        if (currentSearchString.equals(searchString) == false) {
                            return;
                        }

                        searchLiveData.setValue(listResource);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Resource<List<CityTable>> listResource = new Resource<>(Resource.COMPLETED, null, new Exception(e.getMessage()));
                        searchLiveData.setValue(listResource);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
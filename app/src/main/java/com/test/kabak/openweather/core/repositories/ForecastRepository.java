package com.test.kabak.openweather.core.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.network.ForecastResponse;
import com.test.kabak.openweather.core.network.ServerApi;
import com.test.kabak.openweather.core.storage.DatabaseManager;
import com.test.kabak.openweather.core.storage.ForecastWeather;
import com.test.kabak.openweather.core.storage.ForecastWeather.ForecastWeatherComparator;
import com.test.kabak.openweather.core.viewModels.ForecastDay;
import com.test.kabak.openweather.core.viewModels.ForecastDay.ForecastDayComparator;
import com.test.kabak.openweather.core.viewModels.ForecastDayObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class ForecastRepository {
    public static final long FORECAST_LIVE_TIME = System.currentTimeMillis() - 1000 * 60 * 30;
    MutableLiveData<Resource<ForecastDayObject>> data = new MutableLiveData<>();

    public LiveData<Resource<ForecastDayObject>> getForecast(final String cityId) {
        data.setValue(new Resource<ForecastDayObject>(Resource.LOADING, null, null));

        Single
                .create(new SingleOnSubscribe<List<ForecastWeather>>() {
                    @Override
                    public void subscribe(SingleEmitter<List<ForecastWeather>> e) throws Exception {
                        List<ForecastWeather> cachedForecast = DatabaseManager.getDatabase().forecastWeatherDao().getCachedForecast(cityId, FORECAST_LIVE_TIME);
                        if (cachedForecast.isEmpty()) {
                            e.onError(new Exception("Empty database"));
                        } else {
                            e.onSuccess(cachedForecast);
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ForecastWeather>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ForecastWeather> hourForecasts) {
                        ForecastDayObject forecastDayObject = prepareResult(hourForecasts);
                        data.setValue(new Resource<>(Resource.COMPLETED, forecastDayObject, null));
                    }

                    @Override
                    public void onError(Throwable e) {
                        downloadForecast(cityId);
                    }
                });

        return data;
    }

    void downloadForecast(final String cityId) {
        Single<ForecastResponse> responseSingle = ServerApi.getWeatherApi().getForecast(cityId);

        responseSingle
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Function<ForecastResponse, List<ForecastWeather>>() {
                    @Override
                    public List<ForecastWeather> apply(ForecastResponse forecastResponse) throws Exception {
                        List<ForecastWeather> hourForecasts = convertServerResponseToStorageObjects(forecastResponse.forecasts, cityId);
                        DatabaseManager.getDatabase().forecastWeatherDao().insertAll(hourForecasts);
                        return hourForecasts;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<ForecastWeather>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<ForecastWeather> hourForecasts) {
                        ForecastDayObject forecastDayObject = prepareResult(hourForecasts);
                        data.setValue(new Resource<>(Resource.COMPLETED, forecastDayObject, null));
                    }

                    @Override
                    public void onError(Throwable e) {
                        Resource<ForecastDayObject> objectResource = new Resource<>(Resource.COMPLETED, null, new Exception(e));
                        data.setValue(objectResource);
                    }
                });
    }

    @NonNull
    ForecastDayObject prepareResult(List<ForecastWeather> hourForecasts) {
        Map<Integer, ArrayList<ForecastWeather>> groupedForecasts = groupForecastsByDay(hourForecasts);

        Set<Map.Entry<Integer, ArrayList<ForecastWeather>>> entries = groupedForecasts.entrySet();
        ForecastDayObject forecastDayObject = buildDayObject(entries);

        Collections.sort(forecastDayObject.forecastDays, new ForecastDayComparator());
        return forecastDayObject;
    }

    @NonNull
    static ForecastDayObject buildDayObject(Set<Map.Entry<Integer, ArrayList<ForecastWeather>>> entries) {
        ForecastDayObject forecastDayObject = new ForecastDayObject();
        forecastDayObject.forecastDays = new ArrayList<>(10);

        for (Map.Entry<Integer, ArrayList<ForecastWeather>> entry : entries) {
            ArrayList<ForecastWeather> dayForecasts = entry.getValue();
            ForecastDay forecastDay = new ForecastDay();
            forecastDay.hourlyWeather = new ArrayList<>(dayForecasts.size());
            forecastDay.hourlyWeather.addAll(dayForecasts);
            Collections.sort(forecastDay.hourlyWeather, new ForecastWeatherComparator());

            ForecastWeather nearestWeather = null;
            int nearestHour = Integer.MAX_VALUE;
            int dayWeatherClock = 12;

            for (ForecastWeather currentForecastWeather : forecastDay.hourlyWeather) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentForecastWeather.dateTime);
                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                if(Math.abs(hourOfDay - dayWeatherClock) < Math.abs(nearestHour - dayWeatherClock)) {
                    nearestHour = hourOfDay;
                    nearestWeather = currentForecastWeather;
                }
            }

            forecastDay.dayWeather = nearestWeather;
            forecastDayObject.forecastDays.add(forecastDay);
        }
        return forecastDayObject;
    }

    @NonNull
    static Map<Integer, ArrayList<ForecastWeather>> groupForecastsByDay(List<ForecastWeather> hourForecasts) {
        Map<Integer, ArrayList<ForecastWeather>> groupedForecasts = new HashMap<>(10);

        for(ForecastWeather currentForecast : hourForecasts) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(currentForecast.dateTime);
            int key = calendar.get(Calendar.YEAR) * 1000 + calendar.get(Calendar.DAY_OF_YEAR);

            ArrayList<ForecastWeather> existedArray = groupedForecasts.get(key);

            if(existedArray == null) {
                existedArray = new ArrayList<>(10);
                groupedForecasts.put(key, existedArray);
            }

            existedArray.add(currentForecast);
        }
        return groupedForecasts;
    }

    @NonNull
    static List<ForecastWeather> convertServerResponseToStorageObjects(ArrayList<ForecastResponse.ForecastObject> forecasts, String cityId) {
        List<ForecastWeather> hourForecasts = new ArrayList<>(forecasts.size());
        long now = System.currentTimeMillis();

        for(ForecastResponse.ForecastObject currentForecast : forecasts) {
            ForecastWeather forecastWeather = new ForecastWeather();
            forecastWeather.cityId = cityId;
            forecastWeather.temperature = currentForecast.main.temperature;
            forecastWeather.dateTime = currentForecast.dateTime * 1000;
            forecastWeather.windSpeed = currentForecast.wind.windSpeed;
            forecastWeather.icon = currentForecast.weather.get(0).icon;
            forecastWeather.description = currentForecast.weather.get(0).description;
            forecastWeather.minT = currentForecast.main.minT;
            forecastWeather.maxT = currentForecast.main.maxT;
            forecastWeather.timestamp = now;

            hourForecasts.add(forecastWeather);
        }
        return hourForecasts;
    }
}

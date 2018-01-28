package com.test.kabak.openweather.core.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.test.kabak.openweather.core.Resource;
import com.test.kabak.openweather.core.network.ForecastResponse;
import com.test.kabak.openweather.core.network.ServerApi;
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
                        forecastDayObject.forecastDays = new ArrayList<>(forecasts.size());

                        List<ForecastWeather> hourForecasts = new ArrayList<>(forecasts.size());

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

                            hourForecasts.add(forecastWeather);
                        }

                        Map<Integer, ArrayList<ForecastWeather>> groupedForecasts = new HashMap<>(10);

                        for(ForecastWeather currentForeacst : hourForecasts) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(currentForeacst.dateTime);
                            int key = calendar.get(Calendar.YEAR) * 1000 + calendar.get(Calendar.DAY_OF_YEAR);

                            ArrayList<ForecastWeather> existedArray = groupedForecasts.get(key);

                            if(existedArray == null) {
                                existedArray = new ArrayList<>(10);
                                groupedForecasts.put(key, existedArray);
                            }

                            existedArray.add(currentForeacst);
                        }

                        Set<Map.Entry<Integer, ArrayList<ForecastWeather>>> entries = groupedForecasts.entrySet();
                        int dayWeatherClock = 12;

                        for (Map.Entry<Integer, ArrayList<ForecastWeather>> entry : entries) {
                            ArrayList<ForecastWeather> dayForecasts = entry.getValue();
                            ForecastDay forecastDay = new ForecastDay();
                            forecastDay.hourlyWeather = new ArrayList<>(dayForecasts.size());
                            forecastDay.hourlyWeather.addAll(dayForecasts);
                            Collections.sort(forecastDay.hourlyWeather, new ForecastWeatherComparator());

                            ForecastWeather nearestWeaher = null;
                            int nearestHour = Integer.MAX_VALUE;

                            for (ForecastWeather currentForecastWeather : forecastDay.hourlyWeather) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(currentForecastWeather.dateTime);
                                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                                if(Math.abs(hourOfDay - dayWeatherClock) < Math.abs(nearestHour - dayWeatherClock)) {
                                    nearestHour = hourOfDay;
                                    nearestWeaher = currentForecastWeather;
                                }
                            }

                            forecastDay.dayWeather = nearestWeaher;

                            forecastDayObject.forecastDays.add(forecastDay);
                        }

                        Collections.sort(forecastDayObject.forecastDays, new ForecastDayComparator());

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

package com.test.kabak.openweather.core.viewModels;

import com.test.kabak.openweather.core.storage.ForecastWeather;

import java.util.ArrayList;
import java.util.Comparator;

public class ForecastDay {
    public ForecastWeather dayWeather;
    public ArrayList<ForecastWeather> hourlyWeather;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForecastDay that = (ForecastDay) o;

        if (dayWeather != null ? !dayWeather.equals(that.dayWeather) : that.dayWeather != null)
            return false;
        return hourlyWeather != null ? hourlyWeather.equals(that.hourlyWeather) : that.hourlyWeather == null;
    }

    @Override
    public int hashCode() {
        int result = dayWeather != null ? dayWeather.hashCode() : 0;
        result = 31 * result + (hourlyWeather != null ? hourlyWeather.hashCode() : 0);
        return result;
    }

    public static class ForecastDayComparator implements Comparator<ForecastDay> {
        @Override
        public int compare(ForecastDay o1, ForecastDay o2) {
            if(o1.dayWeather.dateTime > o2.dayWeather.dateTime) {
                return 1;
            } else if(o1.dayWeather.dateTime < o2.dayWeather.dateTime) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

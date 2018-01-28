package com.test.kabak.openweather.core.viewModels;

import java.util.ArrayList;

public class ForecastDayObject {
    public ArrayList<ForecastDay> forecastDays;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForecastDayObject that = (ForecastDayObject) o;

        return forecastDays != null ? forecastDays.equals(that.forecastDays) : that.forecastDays == null;
    }

    @Override
    public int hashCode() {
        return forecastDays != null ? forecastDays.hashCode() : 0;
    }
}

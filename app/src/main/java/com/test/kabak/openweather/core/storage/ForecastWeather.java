package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import java.util.Comparator;

@Entity(foreignKeys = @ForeignKey(entity = City.class, parentColumns = "cityId", childColumns = "cityId"),
        primaryKeys = {"cityId", "dateTime"})
public class ForecastWeather {
    @android.support.annotation.NonNull
    public String cityId;
    public float minT;
    public float maxT;
    public String description;
    public String icon;
    public long timestamp;
    public float windSpeed;
    public long dateTime;
    public float temperature;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ForecastWeather that = (ForecastWeather) o;

        if (dateTime != that.dateTime) return false;
        return Float.compare(that.temperature, temperature) == 0;
    }

    @Override
    public int hashCode() {
        int result = (int) (dateTime ^ (dateTime >>> 32));
        result = 31 * result + (temperature != +0.0f ? Float.floatToIntBits(temperature) : 0);
        return result;
    }

    public static class ForecastWeatherComparator implements Comparator<ForecastWeather> {
        @Override
        public int compare(ForecastWeather o1, ForecastWeather o2) {
            if(o1.dateTime > o2.dateTime) {
                return 1;
            } else if(o1.dateTime < o2.dateTime) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}

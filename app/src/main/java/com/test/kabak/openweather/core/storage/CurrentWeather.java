package com.test.kabak.openweather.core.storage;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = City.class,
        parentColumns = "cityId",
        childColumns = "cityId"))
public class CurrentWeather {
    @PrimaryKey
    @androidx.annotation.NonNull
    public String cityId;
    public float minT;
    public float maxT;
    public String description;
    public String icon;
    public long timestamp;
    public float windSpeed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CurrentWeather that = (CurrentWeather) o;

        if (Float.compare(that.minT, minT) != 0) return false;
        if (Float.compare(that.maxT, maxT) != 0) return false;
        if (timestamp != that.timestamp) return false;
        if (Float.compare(that.windSpeed, windSpeed) != 0) return false;
        if (cityId != null ? !cityId.equals(that.cityId) : that.cityId != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        return icon != null ? icon.equals(that.icon) : that.icon == null;
    }

    @Override
    public int hashCode() {
        int result = cityId != null ? cityId.hashCode() : 0;
        result = 31 * result + (minT != +0.0f ? Float.floatToIntBits(minT) : 0);
        result = 31 * result + (maxT != +0.0f ? Float.floatToIntBits(maxT) : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + (windSpeed != +0.0f ? Float.floatToIntBits(windSpeed) : 0);
        return result;
    }
}

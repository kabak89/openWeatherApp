package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = City.class,
        parentColumns = "cityId",
        childColumns = "cityId"))
public class CurrentWeather {
    @PrimaryKey
    @android.support.annotation.NonNull
    public String cityId;
    public float minT;
    public float maxT;
    public String description;
    public String icon;
    public long timestamp;
}

package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(foreignKeys = @ForeignKey(entity = City.class,
        parentColumns = "cityId",
        childColumns = "cityId"))
public class ForecastWeather extends CurrentWeather {

}

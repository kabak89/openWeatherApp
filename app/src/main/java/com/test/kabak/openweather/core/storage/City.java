package com.test.kabak.openweather.core.storage;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class City {
    @PrimaryKey()
    @NonNull
    public String cityId;

    public String name;
}

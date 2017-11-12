package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class City {
    @PrimaryKey()
    @NonNull
    public String cityId;

    public String name;
}

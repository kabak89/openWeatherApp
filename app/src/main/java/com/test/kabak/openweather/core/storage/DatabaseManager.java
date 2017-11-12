package com.test.kabak.openweather.core.storage;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

public class DatabaseManager {
    private static LocalDatabase database;

    public static void init(@NonNull Context context) {
        database = Room.databaseBuilder(context, LocalDatabase.class, "database").build();
    }

    public static LocalDatabase getDatabase() {
        if (database == null) {
            throw new RuntimeException("Call init() before");
        }

        return database;
    }
}

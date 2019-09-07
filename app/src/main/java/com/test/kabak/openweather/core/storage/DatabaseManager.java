package com.test.kabak.openweather.core.storage;

import androidx.room.Room;
import android.content.Context;
import androidx.annotation.NonNull;

public class DatabaseManager {
    private static LocalDatabase database;

    public static void init(@NonNull Context context) {
        database = Room
                .databaseBuilder(context, LocalDatabase.class, "database")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static LocalDatabase getDatabase() {
        if (database == null) {
            throw new RuntimeException("Call init() before");
        }

        return database;
    }
}

package com.test.kabak.openweather;

import android.app.Application;

import com.test.kabak.openweather.core.storage.DatabaseManager;


public class TestApplication extends Application {
    
    @Override
    public void onCreate() {
        super.onCreate();

        DatabaseManager.init(this);
    }
}

package com.example.musicbank;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {
    private static App instance;
    public static Boolean isBackground = false;

    public static synchronized App getInstance(){
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}

package com.example.musicbank;

import androidx.multidex.MultiDexApplication;

public class App extends MultiDexApplication {
    private static App instance;


    public static synchronized App getInstance(){
        return instance;
    }
}

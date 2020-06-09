package com.example.musicbank.Utils;

public class SharedPrefUtil {

    public static String getValue(String key, String defaultValue){
        String value;
        SharedPrefsHelper helper = SharedPrefsHelper.getInstance();
        value = helper.get(key, defaultValue);

        return value;
    }

    public static void setKey(String key, String value){
        SharedPrefsHelper helper = SharedPrefsHelper.getInstance();
        helper.save(key, value);
    }


}

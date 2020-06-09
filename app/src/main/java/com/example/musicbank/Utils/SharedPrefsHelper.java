package com.example.musicbank.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.musicbank.App;

import java.util.Set;

public class SharedPrefsHelper {
    private static SharedPrefsHelper instance;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFS_NAME = "cs";
    private static final String SECRET_TIMER = "secret_chat_timer";
    private static final String FRIEND_SECRET_TIMER = "friend_secret_chat_timer";
    private static final String VIDEOPOSITION = "video_position";

    public static synchronized SharedPrefsHelper getInstance() {
        if (instance == null) {
            instance = new SharedPrefsHelper();
        }

        return instance;
    }

    private SharedPrefsHelper() {
        instance = this;
        sharedPreferences = App.getInstance().getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void delete(String key) {
        if (sharedPreferences.contains(key)) {
            getEditor().remove(key).commit();
        }
    }

    public void save(String key, Object value) {
        SharedPreferences.Editor editor = getEditor();
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Enum) {
            editor.putString(key, value.toString());
        } else if (value instanceof Set) {
            editor.putStringSet(key, (Set) value);
        } else if (value != null) {
            throw new RuntimeException("Attempting to save non-supported preference");
        }

        editor.commit();
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) sharedPreferences.getAll().get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defValue) {
        T returnValue = (T) sharedPreferences.getAll().get(key);
        return returnValue == null ? defValue : returnValue;
    }

    public boolean has(String key) {
        return sharedPreferences.contains(key);
    }


    public void clearAllData(){
        SharedPreferences.Editor editor = getEditor();
        editor.clear().commit();
    }

    private SharedPreferences.Editor getEditor() {
        return sharedPreferences.edit();
    }

    public int getTimer(){
        return get(SECRET_TIMER, 60);
    }

    public void setTimer(int timer){
        save(SECRET_TIMER, timer);
    }

    public void setFriendTimer(String dialogId, int timer){
        save(FRIEND_SECRET_TIMER+ "_" + dialogId, timer);
    }

    public int getFriendTimer(String dialogId){
        return get(FRIEND_SECRET_TIMER+ "_" + dialogId, 60);
    }

    public void saveVideoPosition(long timer){
        save(VIDEOPOSITION, timer);
    }

    public long getVideoPosition(){
        return get(VIDEOPOSITION, Long.valueOf(0));
    }
}

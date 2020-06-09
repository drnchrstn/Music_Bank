package com.example.musicbank.Utils;

import android.content.Context;

public class PreferenceUtils {

    public static final String YOUTUBE = "youtube_track";


    public static String getYoutubetrack(Context context) {
        String news = SharedPrefUtil.getValue(YOUTUBE, null);
        return news;
    }

    public static void setYoutubeTrack(Context context,String value) {
        SharedPrefUtil.setKey(YOUTUBE, value);
    }

}

package com.example.musicbank.ApiTransactions;

import android.content.Context;

import androidx.collection.ArrayMap;

import com.example.musicbank.App;
import com.example.musicbank.AppConfig.Config;
import com.example.musicbank.Objects.SongResultResponse;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRetrofitHelper {
    private static ApiRetrofitHelper instance;
    private Context context;
    private ApiRetrofitHelper(Context context) {
        this.context = context;
    }
    public static synchronized ApiRetrofitHelper getInstance() {
        if (instance == null) {
            instance = new ApiRetrofitHelper(App.getInstance());
        }
        return instance;
    }

    public void getSong(String songName, Callback<SongResultResponse> callback){
        ApiService mApiService = ApiUtils.getApiServiceSong(Config.ApiLyricsUrl);
        if (mApiService != null){
            mApiService.getSong(songName).enqueue(callback);
        }
    }

    public void getSuggestion(ArrayMap<String, Object> arr, Callback<ResponseBody> callback){
        ApiService mApiService = ApiUtils.getApiGoogleSuggestion(Config.googleSuggestion);
        if (mApiService != null){
            mApiService.getSuggestion(objectParam(arr)).enqueue(callback);
        }
    }

    //parameter generator
    private String objectParam(ArrayMap<String, Object> arr){
        JSONObject paramObject = new JSONObject(arr);
        return paramObject.toString();
    }

}

package com.example.musicbank.ApiTransactions;

import com.example.musicbank.Objects.SongResultResponse;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {

    @Headers({
                "x-rapidapi-key: b414f428a6msh14edb3425f45cbbp156057jsnf50d5df930fe"})
    @GET("lyrics/{song_name}")
    Call<SongResultResponse> getSong(@Path("song_name") String name);

}

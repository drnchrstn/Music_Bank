package com.example.musicbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbank.ApiTransactions.ApiRetrofitHelper;
import com.example.musicbank.Objects.SongResultResponse;
import com.example.musicbank.Utils.DiskLruCacheUtil;
import com.example.musicbank.adapters.SearchLyricsAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchSongActivity extends AppCompatActivity {

    private RecyclerView songRecycler;
    private List<SongResultResponse.Song> songs;
    private SearchLyricsAdapter searchAdapter;
    private DiskLruCacheUtil mDiskLruCacheUtil;
    private String recentSearches;
    private ProgressBar progressBar;
    private TextView tv_info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_song_activity);
        songRecycler = findViewById(R.id.songRecycler);
        progressBar = findViewById(R.id.progressLoading);
        tv_info = findViewById(R.id.tv_info);
        initAdapter();



    }

    private void initAdapter(){
        mDiskLruCacheUtil = new DiskLruCacheUtil(SearchSongActivity.this, "journal");
        recentSearches = mDiskLruCacheUtil.getStringCache("searchBackup");
        if (recentSearches != null){
            songs = new Gson().fromJson(recentSearches, new TypeToken<List<SongResultResponse.Song>>(){}.getType());
        }else{
            songs = new ArrayList<>();
        }

        searchAdapter = new SearchLyricsAdapter(SearchSongActivity.this, songs);
        songRecycler.setLayoutManager(new LinearLayoutManager(SearchSongActivity.this));
        songRecycler.setAdapter(searchAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Find songs");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showLoading();

                ApiRetrofitHelper.getInstance().getSong(query, new Callback<SongResultResponse>() {
                    @Override
                    public void onResponse(Call<SongResultResponse> call, Response<SongResultResponse> response) {
                        if (response.isSuccessful()){
                            SongResultResponse songsX = response.body();
                            if (songsX != null && songsX.songs.size() > 0){
                                songs.clear();
                                songs.addAll(songsX.songs);
                                searchAdapter.notifyDataSetChanged();

                                List<SongResultResponse.Song> searchBackup = songs;
                                String backUp = new Gson().toJson(searchBackup);
                                mDiskLruCacheUtil.put("searchBackup", backUp);

                                dismissLoading();

                            }
                        }

                        Log.v("","");
                    }

                    @Override
                    public void onFailure(Call<SongResultResponse> call, Throwable t) {
                        Log.v("","");
                    }
                });

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    public void goToLyrics(SongResultResponse.Song song){
        Intent i = new Intent(SearchSongActivity.this, SongLyricsActivity.class);
        i.putExtra("song_name", song.getName());
        i.putExtra("song_artist", song.getArtist());
        i.putExtra("song_lyrics", song.getLyrics());
        startActivity(i);
    }

    public void showLoading(){
        songRecycler.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        tv_info.setVisibility(View.GONE);
    }

    public void dismissLoading(){
        progressBar.setVisibility(View.GONE);
        tv_info.setText("Search results");
        tv_info.setVisibility(View.VISIBLE);
        songRecycler.setVisibility(View.VISIBLE);
    }

}

package com.example.musicbank;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbank.AsyncTasks.FetchFromYoutubeTask;
import com.example.musicbank.AsyncTasks.Response;
import com.example.musicbank.DbBaseColumns.CursorDbHelper;
import com.example.musicbank.DbBaseColumns.SongBaseColumns;
import com.example.musicbank.DbBaseColumns.SongTableHelper;
import com.example.musicbank.Objects.Song;
import com.example.musicbank.Objects.YoutubeResult;
import com.example.musicbank.Utils.YoutubeUtils;
import com.example.musicbank.adapters.YoutubeAdapter;

import java.util.ArrayList;
import java.util.List;

public class SongLyricsActivity extends AppCompatActivity {

    TextView tv_song_name, tv_lyrics, tv_artist;
    String songName;
    String songArtist;
    String lyrics;
    Button btn_add;


    //youtube
    private List<YoutubeResult> youtubaArrayList;
    private YoutubeAdapter ytAdapter;
    private RecyclerView youtube_recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_lyrics_activity);

        initView();
        getActivityIntent();
        initBroadCast();
        fetchYoutube();

    }

    public void initView(){
        tv_song_name = findViewById(R.id.tv_song_name);
        tv_lyrics = findViewById(R.id.tv_lyrics);
        tv_artist = findViewById(R.id.tv_artist);
        btn_add = findViewById(R.id.btn_add);
        youtube_recyclerView = findViewById(R.id.youtube_Recyclerview);
    }

    public void getActivityIntent(){
        songName = getIntent().getStringExtra("song_name");
        songArtist = getIntent().getStringExtra("song_artist");
        lyrics = getIntent().getStringExtra("song_lyrics");
        Song songSelected = new Song(songName, songArtist, lyrics);
        songName = songName.substring(0, songName.lastIndexOf("by"));

        tv_song_name.setText(songSelected.getName().substring(0, songSelected.getName().lastIndexOf("by")));
        tv_artist.setText(songSelected.getArtist());
        tv_lyrics.setText(songSelected.getLyics());

        boolean isSongExist = CursorDbHelper.getInstance().isSongAdded(SongLyricsActivity.this ,songSelected);
        if (isSongExist){
            btn_add.setVisibility(View.GONE);
        }else{
            btn_add.setVisibility(View.VISIBLE);
            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ContentValues values = SongTableHelper.newContentValues(songSelected);
                    getContentResolver().insert(SongBaseColumns.CONTENT_URI, values);
                }
            });
        }
    }


    public void initBroadCast(){
        IntentFilter intentFilter = new IntentFilter(YoutubeUtils.ACTION_YOUTUBE_BROADCAST);
        registerReceiver(broadcastReceiver,intentFilter);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action != null && action.equals(YoutubeUtils.ACTION_YOUTUBE_BROADCAST)){
                List<YoutubeResult> result = (List<YoutubeResult>) intent.getExtras().getSerializable(YoutubeUtils.PUTEXTRA_BROADCAST);

                if(youtubaArrayList == null){
                    youtubaArrayList = new ArrayList<>();
                }
                youtubaArrayList.addAll(result);
                if (youtubaArrayList.size() > 0){
                    youtube_recyclerView.setLayoutManager(new LinearLayoutManager(SongLyricsActivity.this, LinearLayoutManager.HORIZONTAL, false));
                    ytAdapter = new YoutubeAdapter(SongLyricsActivity.this, youtubaArrayList, songName);
                    youtube_recyclerView.setAdapter(ytAdapter);
                    ytAdapter.notifyDataSetChanged();


                }
            }

        }
    };


    public void fetchYoutube(){
        new FetchFromYoutubeTask(new Response.Listener<List<YoutubeResult>>() {
            @Override
            public void onResponse(List<YoutubeResult> result) {

            }

            @Override
            public void onErrorResponse(Exception exception) {

            }
        }, SongLyricsActivity.this, songName, "").execute();
    }

}

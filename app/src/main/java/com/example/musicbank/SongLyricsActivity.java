package com.example.musicbank;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.musicbank.DbBaseColumns.CursorDbHelper;
import com.example.musicbank.DbBaseColumns.SongBaseColumns;
import com.example.musicbank.DbBaseColumns.SongTableHelper;
import com.example.musicbank.Objects.Song;

public class SongLyricsActivity extends AppCompatActivity {

    TextView tv_song_name, tv_lyrics, tv_artist;
    String songName;
    String songArtist;
    String lyrics;
    Button btn_add;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_lyrics_activity);

        initView();
        getActivityIntent();


    }

    public void initView(){
        tv_song_name = findViewById(R.id.tv_song_name);
        tv_lyrics = findViewById(R.id.tv_lyrics);
        tv_artist = findViewById(R.id.tv_artist);
        btn_add = findViewById(R.id.btn_add);
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
}

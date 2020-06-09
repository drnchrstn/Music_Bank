package com.example.musicbank.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbank.Objects.Song;
import com.example.musicbank.R;
import com.example.musicbank.Utils.StringUtils;

import java.text.DateFormat;
import java.util.Date;

public class SongListCursorAdapter extends BaseCursorAdapter<SongListCursorAdapter.ViewHolder>{

    private Context context;

    public SongListCursorAdapter(Context context) {
        super(null);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {

        Song song = new Song(cursor);

        String songName = song.getName();
        if (songName.contains("by")){
            songName = songName.substring(0,songName.lastIndexOf("by"));
        }
//        holder.tv_song_name.setAnimation(AnimationUtils.loadAnimation());
        holder.tv_song_name.setText(songName);
        holder.tv_artist.setText(song.getArtist());
        holder.tv_date_added.setText(StringUtils.timestampToDate(song.getDate_added()));


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false);


        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_song_name, tv_artist, tv_date_added;


        public ViewHolder(@NonNull View v) {
            super(v);

            tv_song_name = v.findViewById(R.id.tv_song_name);
            tv_artist = v.findViewById(R.id.tv_artist);
            tv_date_added = v.findViewById(R.id.tv_date_added);

        }
    }
}

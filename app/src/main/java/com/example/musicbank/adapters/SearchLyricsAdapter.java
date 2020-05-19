package com.example.musicbank.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbank.Objects.SongResultResponse;
import com.example.musicbank.R;
import com.example.musicbank.SearchSongActivity;

import org.w3c.dom.Text;

import java.util.List;

public class SearchLyricsAdapter extends RecyclerView.Adapter<SearchLyricsAdapter.SearchViewHolder>{

    private Context context;
    private List<SongResultResponse.Song> songs;

    public SearchLyricsAdapter(Context context, List<SongResultResponse.Song> songs){
        this.context = context;
        this.songs = songs;


        setHasStableIds(true);
    }


    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_layout, parent, false);
        return new SearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {

        final SongResultResponse.Song song = songs.get(position);
        holder.tv_song_name.setText(song.getName());
        holder.tv_artist.setText(song.getArtist());
        holder.song_list_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchSongActivity)context).goToLyrics(song);
            }
        });


    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SearchViewHolder extends RecyclerView.ViewHolder {

        TextView tv_song_name;
        TextView tv_artist;
        LinearLayout song_list_layout;

        public SearchViewHolder(@NonNull View v) {
            super(v);

            tv_song_name = v.findViewById(R.id.tv_song_name);
            tv_artist = v.findViewById(R.id.tv_artist);
            song_list_layout = v.findViewById(R.id.song_list_layout);
        }
    }
}

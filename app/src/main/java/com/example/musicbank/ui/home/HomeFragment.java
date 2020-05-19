package com.example.musicbank.ui.home;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbank.DbBaseColumns.SongBaseColumns;
import com.example.musicbank.R;
import com.example.musicbank.adapters.SongListCursorAdapter;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    TextView tv_all_songs;
    RecyclerView songRecycler;
    private SongListCursorAdapter adapter;
    private int SONG_ID = 1;
    private LinearLayoutManager mLinearLayoutManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tv_all_songs = view.findViewById(R.id.tv_all_songs);
        songRecycler = view.findViewById(R.id.songRecycler);
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        songRecycler.setLayoutManager(mLinearLayoutManager);
        adapter = new SongListCursorAdapter(getActivity());
        songRecycler.setAdapter(adapter);


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(SONG_ID, null, this);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == SONG_ID){
            return songLoader();
        }



        return null;
    }


    private Loader<Cursor> songLoader(){
        Uri uri = SongBaseColumns.CONTENT_URI;
        String[] projection = null;
        String selection = null;
        String[] selectionArgs = null;
        return new CursorLoader(getActivity(), uri, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == SONG_ID){
            adapter.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == SONG_ID){
            adapter.swapCursor(null);
        }
    }
}
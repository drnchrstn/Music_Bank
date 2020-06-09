package com.example.musicbank.ui.home;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicbank.DbBaseColumns.SongBaseColumns;
import com.example.musicbank.R;
import com.example.musicbank.SearchSongActivity;
import com.example.musicbank.adapters.SongListCursorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    TextView tv_all_songs;
    RecyclerView songRecycler;
    private SongListCursorAdapter adapter;
    private int SONG_ID = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private String query ="";
    public static FloatingActionButton addSong;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tv_all_songs = view.findViewById(R.id.tv_all_songs);
        songRecycler = view.findViewById(R.id.songRecycler);
        addSong = view.findViewById(R.id.addSong);
        addSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addSong.hide();
                    Intent intent = new Intent(getActivity(), SearchSongActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slideup, R.anim.noanimation);
            }
        });
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        songRecycler.setLayoutManager(mLinearLayoutManager);
        adapter = new SongListCursorAdapter(getActivity());
        songRecycler.setAdapter(adapter);
        songRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && addSong.getVisibility() == View.VISIBLE){
                    addSong.hide();
                }else if (dy < 0 && addSong.getVisibility() != View.VISIBLE){
                    addSong.show();
                }

            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

        if (hasQuery()){
            selection = SongBaseColumns.COLUMN_NAME_SONG_NAME + " LIKE ? OR " +
                    SongBaseColumns.COLUMN_NAME_SONG_ARTIST + " LIKE ? OR " +
                    SongBaseColumns.COLUMN_NAME_SONG_LYRICS + " LIKE ?";
            selectionArgs =new String[]{"%"+query+"%", "%"+query+"%", "%"+query+"%"};
        }

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.toolbar_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView sv = (SearchView) MenuItemCompat.getActionView(item);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                query = newText;
                restartLoader();
                return true;
            }
        });
//        SearchView sv = new SearchView(getActivity().getsupp)

//        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean hasQuery(){
        return query != null || !query.equals("");
    }


    public void restartLoader(){
        getLoaderManager().restartLoader(SONG_ID, null, this);
    }

}
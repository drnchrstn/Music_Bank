package com.example.musicbank.DbBaseColumns;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicbank.App;
import com.example.musicbank.Objects.Song;

public class CursorDbHelper {
    private static CursorDbHelper instance;
    private Context context;
    private CursorDbHelper(Context context){
        this.context = context;
    }

    public static synchronized CursorDbHelper getInstance(){
        if (instance == null){
            instance = new CursorDbHelper(App.getInstance());
        }
        return instance;
    }

    public boolean isTableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tableName+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public boolean isSongAdded(Context context,Song song){
        String songName = song.getName();
        String songLyrics = song.getLyics();

        Cursor cursor = context.getContentResolver().query(SongBaseColumns.CONTENT_URI,
                new String[]{SongBaseColumns.COLUMN_NAME_SONG_NAME, SongBaseColumns.COLUMN_NAME_SONG_LYRICS},
                SongBaseColumns.COLUMN_NAME_SONG_NAME + "=? AND " + SongBaseColumns.COLUMN_NAME_SONG_LYRICS + " =?"
                    , new String[]{songName, songLyrics}, null);

        if (cursor != null && cursor.moveToFirst()){
            cursor.close();
            return true;
        }else{
         cursor.close();
         return false;
        }
    }


}

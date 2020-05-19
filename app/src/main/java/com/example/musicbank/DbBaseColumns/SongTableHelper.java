package com.example.musicbank.DbBaseColumns;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.example.musicbank.Objects.Song;

public class SongTableHelper {
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE " + SongBaseColumns.TABLE_NAME + " (" +
            SongBaseColumns.COLUMN_NAME_SONG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            SongBaseColumns.COLUMN_NAME_SONG_NAME + DbHelper.TEXT_TYPE + DbHelper.COMMA_SEP +
            SongBaseColumns.COLUMN_NAME_SONG_LYRICS + DbHelper.TEXT_TYPE + DbHelper.COMMA_SEP +
            SongBaseColumns.COLUMN_NAME_SONG_ARTIST + DbHelper.TEXT_TYPE + DbHelper.COMMA_SEP +
            SongBaseColumns.COLUMN_NAME_SONG_FILE + DbHelper.TEXT_TYPE + DbHelper.COMMA_SEP +
            SongBaseColumns.COLUMN_NAME_DATE_ADDED + DbHelper.TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + SongBaseColumns.TABLE_NAME;

    public static void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public static void onDelete(SQLiteDatabase db){
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    public static ContentValues newContentValues(Song song){
        ContentValues values = new ContentValues();
        values.put(SongBaseColumns.COLUMN_NAME_SONG_NAME, song.getName());
        values.put(SongBaseColumns.COLUMN_NAME_SONG_LYRICS, song.getLyics());
        values.put(SongBaseColumns.COLUMN_NAME_SONG_ARTIST, song.getArtist());
        values.put(SongBaseColumns.COLUMN_NAME_SONG_FILE, song.getFileName());
        values.put(SongBaseColumns.COLUMN_NAME_DATE_ADDED, System.currentTimeMillis());
        return values;
    }
}

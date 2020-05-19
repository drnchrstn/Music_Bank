package com.example.musicbank.Objects;

import android.database.Cursor;

import com.example.musicbank.DbBaseColumns.SongBaseColumns;

import java.io.Serializable;
import java.util.Map;

public class Song implements Serializable {
    String id;
    String name;
    String lyics;
    String artist;
    String fileName;
    String date_added;


    public Song(Cursor cursor){
        this.id = cursor.getString(cursor.getColumnIndex(SongBaseColumns.COLUMN_NAME_SONG_ID));
        this.name = cursor.getString(cursor.getColumnIndex(SongBaseColumns.COLUMN_NAME_SONG_NAME));
        this.lyics = cursor.getString(cursor.getColumnIndex(SongBaseColumns.COLUMN_NAME_SONG_LYRICS));
        this.artist = cursor.getString(cursor.getColumnIndex(SongBaseColumns.COLUMN_NAME_SONG_ARTIST));
        this.fileName = cursor.getString(cursor.getColumnIndex(SongBaseColumns.COLUMN_NAME_SONG_FILE));
        this.date_added = cursor.getString(cursor.getColumnIndex(SongBaseColumns.COLUMN_NAME_DATE_ADDED));
    }

    public Song(String name, String artist, String lyrics){
        this.name = name;
        this.artist = artist;
        this.lyics = lyrics;
    }


    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String date_added) {
        this.date_added = date_added;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLyics() {
        return lyics;
    }

    public void setLyics(String lyics) {
        this.lyics = lyics;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

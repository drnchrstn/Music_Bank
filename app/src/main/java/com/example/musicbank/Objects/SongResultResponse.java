package com.example.musicbank.Objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SongResultResponse implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("failed")
    @Expose
    private boolean failed = false;

    @SerializedName("content")
    @Expose
    public List<Song> songs = null;


    public class Song implements Serializable{
        @SerializedName("title")
        @Expose
        private String name;

        @SerializedName("lyrics")
        @Expose
        private String lyrics;

        @SerializedName("artist")
        @Expose
        private String artist;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLyrics() {
            return lyrics;
        }

        public void setLyrics(String lyrics) {
            this.lyrics = lyrics;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }
    }

}

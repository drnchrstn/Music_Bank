package com.example.musicbank.DbBaseColumns;

import android.net.Uri;
import android.provider.BaseColumns;

public class SongBaseColumns implements BaseColumns {
    public static final String TABLE_NAME = "songtable";
    public static final String COLUMN_NAME_SONG_ID = "song_id";
    public static final String COLUMN_NAME_SONG_NAME = "song_name";
    public static final String COLUMN_NAME_SONG_LYRICS = "song_lyrics";
    public static final String COLUMN_NAME_SONG_ARTIST = "song_artist";
    public static final String COLUMN_NAME_SONG_FILE = "song_file";
    public static final String COLUMN_NAME_DATE_ADDED = "song_date_added";

    public static final Uri CONTENT_URI =  Uri.parse("content://" + DatabaseContentProvider.AUTHORITY + "/songtable");

    public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + DatabaseContentProvider.AUTHORITY + ".songtable";
    public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + DatabaseContentProvider.AUTHORITY + ".songtable";

}

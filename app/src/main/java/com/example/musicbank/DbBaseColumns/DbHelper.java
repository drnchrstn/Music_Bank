package com.example.musicbank.DbBaseColumns;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "music_bank";
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS";

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String LONG_TYPE = " LONG";
    public static final String COMMA_SEP = ",";

    private static DbHelper instance;

    public static synchronized DbHelper getInstance(Context context){
        if (instance == null){
            instance = new DbHelper(context.getApplicationContext());
        }
        return instance;
    }


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        SQLiteDatabase db = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            boolean isSongTableExists = CursorDbHelper.getInstance().isTableExists(db, SongBaseColumns.TABLE_NAME);
            if (!isSongTableExists){
                SongTableHelper.onCreate(db);
            }

        db.execSQL(DROP_TABLE + SongBaseColumns.TABLE_NAME);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public void createTable(SQLiteDatabase db){
        SongTableHelper.onCreate(db);
    }

}

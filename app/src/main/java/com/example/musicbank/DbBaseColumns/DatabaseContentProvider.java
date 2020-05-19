package com.example.musicbank.DbBaseColumns;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseContentProvider extends ContentProvider {
    public static final String AUTHORITY = "com.example.musicbank.contentprovider";

    private static final int SONG = 1;
    private static final int SONG_ID = 2;

    private final UriMatcher uriMatcher;

    private DbHelper dbHelper;

    public DatabaseContentProvider(){
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, SongBaseColumns.TABLE_NAME, SONG);
        uriMatcher.addURI(AUTHORITY, SongBaseColumns.TABLE_NAME + "/#", SONG_ID);

    }


    @Override
    public boolean onCreate() {
        dbHelper = DbHelper.getInstance(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String table = null;
        final int code = uriMatcher.match(uri);

        if (code == SONG || code == SONG_ID){
            table = SongBaseColumns.TABLE_NAME;

            if (code == SONG_ID){
                selection = SongBaseColumns.COLUMN_NAME_SONG_ID + "=?";
                selectionArgs = new String[]{uri.getLastPathSegment()};
            }
        }else{
            throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)){
            case SONG:
                return SongBaseColumns.CONTENT_TYPE;

            case SONG_ID:
                return SongBaseColumns.CONTENT_ITEM_TYPE;


            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri contentUri = null;
        String table = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)){
            case SONG:
                table = SongBaseColumns.TABLE_NAME;
                contentUri = SongBaseColumns.CONTENT_URI;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        long rowId = db.insert(table, null, values);
        if (rowId > 0) {
            Uri noteUri = ContentUris.withAppendedId(contentUri, rowId);
            getContext().getContentResolver().notifyChange(noteUri, null);
            return noteUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        String table = null;
        switch (uriMatcher.match(uri)) {
            case SONG:
                table = SongBaseColumns.TABLE_NAME;
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.delete(table, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
        String table = null;
        switch (uriMatcher.match(uri)) {
            case SONG:
                table = SongBaseColumns.TABLE_NAME;
                break;
            case SONG_ID:
                table = SongBaseColumns.TABLE_NAME;
                where = DatabaseUtils.concatenateWhere(SongBaseColumns.COLUMN_NAME_SONG_ID + " = " + ContentUris.parseId(uri), where);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count = db.update(table, values, where, whereArgs);
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentProviderResult[] results = super.applyBatch(operations);
            db.setTransactionSuccessful();

            return results;
        } catch (OperationApplicationException e) {
            e.printStackTrace();
            return null;
        } finally {
            db.endTransaction();
        }
    }

}

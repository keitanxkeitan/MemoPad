package org.keitanxkeitan.memopad;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

public class MemoProvider extends ContentProvider {

    private SQLiteDatabase mDb;
    private MemoDbOpenHelper mDbHelper;

    private static final String URI =
            "content://org.keitanxkeitan.provider.memopad/items";
    public static final Uri CONTENT_URI = Uri.parse(URI);    

    @Override
    public boolean onCreate() {
        mDbHelper = new MemoDbOpenHelper(getContext());
        try {
            mDb = mDbHelper.getWritableDatabase();
        } catch (SQLiteException e) {
            mDb = mDbHelper.getReadableDatabase();
        }
        return true;
    }

    // Create the constants used to differentiate between the different
    // URI requests.
    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;

    private static final UriMatcher uriMatcher;

    // Populate the UriMatcher object, where a URI ending in 'items' will
    // correspond to a request for all items, and 'items/[rowID]'
    // represents a single row.
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("org.keitanxkeitan.provider.memopad", "items", ALLROWS);
        uriMatcher.addURI("org.keitanxkeitan.provider.memopad", "items/#",
                SINGLE_ROW);
    }

    @Override
    public Cursor query(Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sort) {

        // If this is a row query, limit the result set to the passed in row.
        switch (uriMatcher.match(uri)) {
        case SINGLE_ROW :
            final long id = Long.parseLong(uri.getPathSegments().get(1));
            selection =
                    android.provider.BaseColumns._ID + "=" + Long.toString(id) 
                    + (selection == null ? "" : "AND(" + selection + ")");
        }
        Cursor c = mDb.query(MemoDbOpenHelper.DB_TABLE, projection, selection, 
                selectionArgs, null, null, sort);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        long id = mDb.insert(MemoDbOpenHelper.DB_TABLE, null, initialValues);
        final Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String where, String[] whereArgs) {
        final int deleteCount;
        switch (uriMatcher.match(uri)) {
        case ALLROWS:
            deleteCount = mDb.delete(MemoDbOpenHelper.DB_TABLE, where, 
                    whereArgs);
            break;
        case SINGLE_ROW:
            final long id = Long.parseLong(uri.getPathSegments().get(1));
            final String idPlusSelection = 
                    android.provider.BaseColumns._ID + "=" + Long.toString(id) 
                    + (where == null ? "" : "AND (" + where + ")");
            deleteCount = mDb.delete(MemoDbOpenHelper.DB_TABLE, 
                    idPlusSelection, whereArgs);
            break;
        default: 
            throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        return deleteCount;
    }

    @Override
    public int update(Uri uri, ContentValues values, String where,
            String[] whereArgs) {
        final int updateCount;
        switch (uriMatcher.match(uri)) {
        case ALLROWS:
            updateCount = mDb.update(MemoDbOpenHelper.DB_TABLE, values, where, 
                    whereArgs);
            break;
        case SINGLE_ROW:
            final long id = Long.parseLong(uri.getPathSegments().get(1));
            final String idPlusSelection = 
                    android.provider.BaseColumns._ID + "=" + Long.toString(id) 
                    + (where == null ? "" : "AND(" + where + ")");
            updateCount = mDb.update(MemoDbOpenHelper.DB_TABLE, values, 
                    idPlusSelection, whereArgs);
            break;
        default: throw new IllegalArgumentException("Unsupported URI:" +
                uri);
        }
        // ïœçXÇí ímÇ∑ÇÈ
        getContext().getContentResolver().notifyChange(uri, null);

        return updateCount;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
        case ALLROWS:
            return "vnd.keitanxkeitan.cursor.dir/memoprovidercontent";
        case SINGLE_ROW:
            return "vnd.keitanxkeitan.cursor.item/memoprovidercontent";
        default:
            throw new IllegalArgumentException("Unsupported URI: " +
                    uri);
        }
    }

}
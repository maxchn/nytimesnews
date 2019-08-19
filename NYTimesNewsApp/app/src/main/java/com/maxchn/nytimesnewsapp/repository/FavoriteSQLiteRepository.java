package com.maxchn.nytimesnewsapp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.model.Favorites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavoriteSQLiteRepository implements Repository<Favorites> {

    private static final String TAG = "FavoriteSQLiteRep";
    private final DbHelper mDbHelper;

    public FavoriteSQLiteRepository(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public List<Favorites> getAll() {
        List<Favorites> items = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(DbHelper.FAVORITES_TABLE_NAME, DbHelper.favoriteFields,
                null, null,
                null, null, null)) {

            Map<String, Integer> collIdx = getColumnsIndexesMap(cursor);

            while (cursor.moveToNext()) {
                try {
                    Favorites item = getItem(cursor, collIdx);
                    items.add(item);
                } catch (NullPointerException e) {
                    Log.e(TAG, "getAll: ", e);
                }
            }
        }

        return items;
    }

    @Override
    public boolean create(Favorites item) {
        if (item != null) {
            SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.FavoriteFields.TITLE, item.getTitle());
            contentValues.put(DbHelper.FavoriteFields.SOURCE, item.getSource());
            contentValues.put(DbHelper.FavoriteFields.PUBLISHED_DATE, item.getPublished_date());
            contentValues.put(DbHelper.FavoriteFields.UPDATED, item.getUpdated());
            contentValues.put(DbHelper.FavoriteFields.URL, item.getUrl());
            contentValues.put(DbHelper.FavoriteFields.DATA, item.getData());

            try {
                long count = writableDatabase.insert(DbHelper.FAVORITES_TABLE_NAME, null, contentValues);
                return count != -1;
            } catch (Exception e) {
                Log.e(TAG, "create: ", e);
            }
        }

        return false;
    }

    @Override
    public boolean delete(Integer id) {
        int count = 0;

        try {
            SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();

            count = writableDatabase.delete(
                    DbHelper.FAVORITES_TABLE_NAME,
                    String.format("%s=?", DbHelper.FavoriteFields.ID),
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "delete: ", e);
        }

        return count != 0;
    }

    @Override
    public List<Favorites> find(String selection, String[] selectionArgs) {
        List<Favorites> item = new ArrayList<>();

        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            try (Cursor cursor = db.query(DbHelper.FAVORITES_TABLE_NAME, DbHelper.favoriteFields,
                    selection, selectionArgs, null, null, null)) {

                Map<String, Integer> collIdx = getColumnsIndexesMap(cursor);

                while (cursor.moveToNext()) {
                    try {
                        item.add(getItem(cursor, collIdx));
                    } catch (NullPointerException e) {
                        Log.e(TAG, "getAll: ", e);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "find: ", e);
        }

        return item;
    }

    private Favorites getItem(Cursor cursor, Map<String, Integer> collIdx) {
        int id = cursor.getInt(getCollIndex(DbHelper.FavoriteFields.ID, collIdx));
        String title = cursor.getString(getCollIndex(DbHelper.FavoriteFields.TITLE, collIdx));
        String source = cursor.getString(getCollIndex(DbHelper.FavoriteFields.SOURCE, collIdx));
        String publishedDate = cursor.getString(getCollIndex(DbHelper.FavoriteFields.PUBLISHED_DATE, collIdx));
        String updated = cursor.getString(getCollIndex(DbHelper.FavoriteFields.UPDATED, collIdx));
        String url = cursor.getString(getCollIndex(DbHelper.FavoriteFields.URL, collIdx));
        String data = cursor.getString(getCollIndex(DbHelper.FavoriteFields.DATA, collIdx));

        Favorites item = new Favorites();
        item.setId(id);
        item.setTitle(title);
        item.setSource(source);
        item.setPublished_date(publishedDate);
        item.setUpdated(updated);
        item.setUrl(url);
        item.setData(data);

        return item;
    }

    private Integer getCollIndex(String collName, Map<String, Integer> collIdx) {
        if (collName != null)
            return collIdx.get(collName);
        else
            return -1;
    }

    private Map<String, Integer> getColumnsIndexesMap(Cursor cursor) {
        Map<String, Integer> collIdx = new HashMap<>();
        collIdx.put(DbHelper.FavoriteFields.ID, cursor.getColumnIndex(DbHelper.FavoriteFields.ID));
        collIdx.put(DbHelper.FavoriteFields.TITLE, cursor.getColumnIndex(DbHelper.FavoriteFields.TITLE));
        collIdx.put(DbHelper.FavoriteFields.SOURCE, cursor.getColumnIndex(DbHelper.FavoriteFields.SOURCE));
        collIdx.put(DbHelper.FavoriteFields.PUBLISHED_DATE, cursor.getColumnIndex(DbHelper.FavoriteFields.PUBLISHED_DATE));
        collIdx.put(DbHelper.FavoriteFields.UPDATED, cursor.getColumnIndex(DbHelper.FavoriteFields.UPDATED));
        collIdx.put(DbHelper.FavoriteFields.URL, cursor.getColumnIndex(DbHelper.FavoriteFields.URL));
        collIdx.put(DbHelper.FavoriteFields.DATA, cursor.getColumnIndex(DbHelper.FavoriteFields.DATA));

        return collIdx;
    }
}
package com.maxchn.nytimesnewsapp.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.maxchn.nytimesnewsapp.helper.DbHelper;
import com.maxchn.nytimesnewsapp.model.MostPopularType;
import com.maxchn.nytimesnewsapp.model.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MostPopularSQLiteRepository implements Repository<Result> {

    private static final String TAG = "MostPopularSQLiteRep";
    private final DbHelper mDbHelper;

    public MostPopularSQLiteRepository(DbHelper dbHelper) {
        mDbHelper = dbHelper;
    }

    @Override
    public List<Result> getAll() {
        List<Result> items = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        try (Cursor cursor = db.query(DbHelper.MOST_POPULAR_TABLE_NAME, DbHelper.mostPopularFields,
                null, null,
                null, null, null)) {

            Map<String, Integer> collIdx = getColumnsIndexesMap(cursor);

            while (cursor.moveToNext()) {
                try {
                    Result item = getItem(cursor, collIdx);
                    items.add(item);
                } catch (NullPointerException e) {
                    Log.e(TAG, "getAll: ", e);
                }
            }
        }

        return items;
    }

    @Override
    public boolean create(Result item) {
        if (item != null) {
            SQLiteDatabase writableDatabase = mDbHelper.getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(DbHelper.MostPopularFields.TITLE, item.getTitle());
            contentValues.put(DbHelper.MostPopularFields.SOURCE, item.getSource());
            contentValues.put(DbHelper.MostPopularFields.PUBLISHED_DATE, item.getPublishedDate());
            contentValues.put(DbHelper.MostPopularFields.UPDATED, item.getUpdated());
            contentValues.put(DbHelper.MostPopularFields.URL, item.getUrl());
            contentValues.put(DbHelper.MostPopularFields.TYPE, item.getType().toString());

            try {
                long count = writableDatabase.insert(DbHelper.MOST_POPULAR_TABLE_NAME, null, contentValues);
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
                    DbHelper.MOST_POPULAR_TABLE_NAME,
                    String.format("%s=?", DbHelper.MostPopularFields.ID),
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Log.e(TAG, "delete: ", e);
        }

        return count != 0;
    }

    @Override
    public List<Result> find(String selection, String[] selectionArgs) {
        List<Result> items = new ArrayList<>();

        try {
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            try (Cursor cursor = db.query(DbHelper.MOST_POPULAR_TABLE_NAME, DbHelper.mostPopularFields,
                    selection, selectionArgs, null, null, null)) {

                Map<String, Integer> collIdx = getColumnsIndexesMap(cursor);

                while (cursor.moveToNext()) {
                    try {
                        items.add(getItem(cursor, collIdx));
                    } catch (NullPointerException e) {
                        Log.e(TAG, "getAll: ", e);
                    }
                }
            }
        } catch (
                Exception e) {
            Log.e(TAG, "find: ", e);
        }

        return items;
    }

    private Result getItem(Cursor cursor, Map<String, Integer> collIdx) {
        int id = cursor.getInt(getCollIndex(DbHelper.MostPopularFields.ID, collIdx));
        String title = cursor.getString(getCollIndex(DbHelper.MostPopularFields.TITLE, collIdx));
        String source = cursor.getString(getCollIndex(DbHelper.MostPopularFields.SOURCE, collIdx));
        String publishedDate = cursor.getString(getCollIndex(DbHelper.MostPopularFields.PUBLISHED_DATE, collIdx));
        String updated = cursor.getString(getCollIndex(DbHelper.MostPopularFields.UPDATED, collIdx));
        String url = cursor.getString(getCollIndex(DbHelper.MostPopularFields.URL, collIdx));
        String type = cursor.getString(getCollIndex(DbHelper.MostPopularFields.TYPE, collIdx));

        Result item = new Result();
        item.setId(id);
        item.setTitle(title);
        item.setSource(source);
        item.setPublishedDate(publishedDate);
        item.setUpdated(updated);
        item.setUrl(url);
        item.setType(MostPopularType.valueOf(type));

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
        collIdx.put(DbHelper.MostPopularFields.ID, cursor.getColumnIndex(DbHelper.MostPopularFields.ID));
        collIdx.put(DbHelper.MostPopularFields.TITLE, cursor.getColumnIndex(DbHelper.MostPopularFields.TITLE));
        collIdx.put(DbHelper.MostPopularFields.SOURCE, cursor.getColumnIndex(DbHelper.MostPopularFields.SOURCE));
        collIdx.put(DbHelper.MostPopularFields.PUBLISHED_DATE, cursor.getColumnIndex(DbHelper.MostPopularFields.PUBLISHED_DATE));
        collIdx.put(DbHelper.MostPopularFields.UPDATED, cursor.getColumnIndex(DbHelper.MostPopularFields.UPDATED));
        collIdx.put(DbHelper.MostPopularFields.URL, cursor.getColumnIndex(DbHelper.MostPopularFields.URL));
        collIdx.put(DbHelper.MostPopularFields.TYPE, cursor.getColumnIndex(DbHelper.MostPopularFields.TYPE));

        return collIdx;
    }
}
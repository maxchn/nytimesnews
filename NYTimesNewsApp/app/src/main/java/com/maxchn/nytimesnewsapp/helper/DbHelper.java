package com.maxchn.nytimesnewsapp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "MostPopular.db";
    public static final String FAVORITES_TABLE_NAME = "favorites";
    public static final String MOST_POPULAR_TABLE_NAME = "most_populars";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static class FavoriteFields {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String SOURCE = "source";
        public static final String PUBLISHED_DATE = "published_date";
        public static final String UPDATED = "updated";
        public static final String URL = "url";
        public static final String DATA = "data";
    }

    public static class MostPopularFields {
        public static final String ID = "_id";
        public static final String TITLE = "title";
        public static final String SOURCE = "source";
        public static final String PUBLISHED_DATE = "published_date";
        public static final String UPDATED = "updated";
        public static final String URL = "url";
        public static final String TYPE = "type";
    }

    public static final String[] favoriteFields = {
            FavoriteFields.ID,
            FavoriteFields.TITLE,
            FavoriteFields.SOURCE,
            FavoriteFields.PUBLISHED_DATE,
            FavoriteFields.UPDATED,
            FavoriteFields.URL,
            FavoriteFields.DATA
    };

    public static final String[] mostPopularFields = {
            MostPopularFields.ID,
            MostPopularFields.TITLE,
            MostPopularFields.SOURCE,
            MostPopularFields.PUBLISHED_DATE,
            MostPopularFields.UPDATED,
            MostPopularFields.URL,
            MostPopularFields.TYPE
    };

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + FAVORITES_TABLE_NAME +
                "(" +
                FavoriteFields.ID + " integer primary key autoincrement," +
                FavoriteFields.TITLE + " TEXT NOT NULL," +
                FavoriteFields.SOURCE + " TEXT NOT NULL," +
                FavoriteFields.PUBLISHED_DATE + " TEXT NOT NULL," +
                FavoriteFields.UPDATED + " TEXT NOT NULL," +
                FavoriteFields.URL + " TEXT NOT NULL," +
                FavoriteFields.DATA + " TEXT" +
                ")");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + MOST_POPULAR_TABLE_NAME +
                "(" +
                MostPopularFields.ID + " integer primary key autoincrement," +
                MostPopularFields.TITLE + " TEXT NOT NULL," +
                MostPopularFields.SOURCE + " TEXT NOT NULL," +
                MostPopularFields.PUBLISHED_DATE + " TEXT NOT NULL," +
                MostPopularFields.UPDATED + " TEXT," +
                MostPopularFields.URL + " TEXT NOT NULL," +
                MostPopularFields.TYPE + " TEXT" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + FAVORITES_TABLE_NAME);
        db.execSQL("drop table if exists " + MOST_POPULAR_TABLE_NAME);
    }
}
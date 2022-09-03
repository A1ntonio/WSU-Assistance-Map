package com.puulapp.wsumap.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "campus_map.db";
    private  static final int DATABASE_VERSION = 2;

    //Constants for identifying table and columns
    public static final String TABLE_SAVE = "saved";
    public static final String TABLE_NOTIFICATION = "notification";

    public static final String ID = "_id";
    public static final String IMAGE = "image";
    public static final String NAME = "name";
    public static final String DESC = "description";
    public static final String LAT = "lat";
    public static final String LNG = "lng";
    public static final String CAMPUS = "campus";
    public static final String KEY = "item_key";

    public static final String NOT_ID = "not_id";
    public static final String NOT_KEY = "not_key";

    //SQL to create table
    private static final String TABLE_CREATE_SAVED = "CREATE TABLE " + TABLE_SAVE + " (" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + IMAGE + " TEXT, " + NAME + " TEXT, " + DESC + " TEXT, " + LAT + " TEXT, " + LNG + " TEXT, " + CAMPUS +" TEXT, " + KEY + " TEXT);";

    private static final String TABLE_CREATE_NOTIFICATION = "CREATE TABLE " + TABLE_NOTIFICATION + " (" + ID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + NOT_KEY + " TEXT);";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE_SAVED);
        db.execSQL(TABLE_CREATE_NOTIFICATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION);
        onCreate(db);
    }



}
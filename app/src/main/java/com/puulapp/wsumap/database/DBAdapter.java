package com.puulapp.wsumap.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter {

    Context c;
    static SQLiteDatabase db;
    static DBOpenHelper helper;

    public DBAdapter(Context c) {
        this.c = c;
        helper = new DBOpenHelper(c);
    }

    public void addNewItem(SaveModel spacecraft) {
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(DBOpenHelper.IMAGE, spacecraft.getItem_image());
            cv.put(DBOpenHelper.NAME, spacecraft.getItem_name());
            cv.put(DBOpenHelper.DESC, spacecraft.getItem_desc());
            cv.put(DBOpenHelper.LAT, spacecraft.getItem_lat());
            cv.put(DBOpenHelper.LNG, spacecraft.getItem_lng());
            cv.put(DBOpenHelper.KEY, spacecraft.getItem_key());
            cv.put(DBOpenHelper.CAMPUS, spacecraft.getItem_campus());

            long result = db.insert(DBOpenHelper.TABLE_SAVE, null, cv);
            if (result > 0) {
                Toast.makeText(c, "Saved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "Not Saved!", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
    }

    public void addNewKey(String key) {
        try {
            db = helper.getWritableDatabase();

            ContentValues cv = new ContentValues();

            cv.put(DBOpenHelper.NOT_KEY, key);

            db.insert(DBOpenHelper.TABLE_NOTIFICATION, null, cv);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
    }

    //
    public ArrayList<SaveModel> retrieveSaved() {
        ArrayList<SaveModel> spacecrafts = new ArrayList<>();

        try {
            db = helper.getWritableDatabase();

            Cursor c = db.rawQuery("SELECT * FROM " + DBOpenHelper.TABLE_SAVE + " ORDER BY " + DBOpenHelper.ID + " DESC;", null);

            SaveModel s;
            spacecrafts.clear();
            String image;
            String name, desc, lat, lng, campus, key;
            int id;
            while (c.moveToNext()) {
                id = c.getInt(0);
                image = c.getString(1);
                name = c.getString(2);
                desc = c.getString(3);
                lat = c.getString(4);
                lng = c.getString(5);
                campus = c.getString(6);
                key = c.getString(7);

                s = new SaveModel(id, image, name, desc, lat, lng, key, campus);

                spacecrafts.add(s);

            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }

        return spacecrafts;
    }

    public boolean check_exist(String key){
        boolean exist = false;
        try {
            db = helper.getWritableDatabase();

            Cursor c1 = db.rawQuery("SELECT * FROM " + DBOpenHelper.TABLE_SAVE + " WHERE " + DBOpenHelper.KEY + " = '" + key + "';", null);

            while (c1.moveToNext()) {
                exist = true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return exist;
    }

    public boolean check_key_exist(String key){
        boolean exist = false;
        try {
            db = helper.getWritableDatabase();

            Cursor c1 = db.rawQuery("SELECT * FROM " + DBOpenHelper.TABLE_NOTIFICATION + " WHERE " + DBOpenHelper.NOT_KEY + " = '" + key + "';", null);

            while (c1.moveToNext()) {
                exist = true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }
        return exist;
    }

    public boolean deleteSingleRow(int id) {

        try {
            db = helper.getWritableDatabase();

            String selection = DBOpenHelper.ID + " = " + id;

            long result = db.delete(DBOpenHelper.TABLE_SAVE, selection, null);
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }

        return false;
    }

}

package com.wordpress.senidigital.imagemachine.Entity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 5;

    static final String DATABASE_NAME = "ImageMachine.db";

    public static final String TABLE_SQLite = "tb_machine";

    public static final String COLUMN_SERIAL = "serial_number";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SPEC = "spesification";
    public static final String COLUMN_LAST_MAINTENANCE = "last_maintenance";
    public static final String COLUMN_SUM_IMAGE = "image_count";
    public static final String COLUMN_PIC_1 = "pic1";
    public static final String COLUMN_PIC_2 = "pic2";
    public static final String COLUMN_PIC_3 = "pic3";
    public static final String COLUMN_PIC_4 = "pic4";
    public static final String COLUMN_PIC_5 = "pic5";




    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + TABLE_SQLite + " (" +
                COLUMN_SERIAL + " TEXT PRIMARY KEY NOT NULL, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_SPEC + " TEXT NOT NULL," +
                COLUMN_LAST_MAINTENANCE + " TEXT NOT NULL," +
                COLUMN_SUM_IMAGE + " INT NOT NULL," +
                COLUMN_PIC_1 + " TEXT NOT NULL," +
                COLUMN_PIC_2 + " TEXT NOT NULL," +
                COLUMN_PIC_3 + " TEXT NOT NULL," +
                COLUMN_PIC_4 + " TEXT NOT NULL," +
                COLUMN_PIC_5 + " TEXT NOT NULL" +
                " )";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SQLite);
        onCreate(db);
    }

    public ArrayList<HashMap<String, String>> getAllData() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " ORDER BY last_maintenance DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_SERIAL, cursor.getString(0));
                map.put(COLUMN_NAME, cursor.getString(1));
                map.put(COLUMN_SPEC, cursor.getString(2));
                map.put(COLUMN_LAST_MAINTENANCE, cursor.getString(3));
                map.put(COLUMN_SUM_IMAGE, String.valueOf(cursor.getInt(4)));
                map.put(COLUMN_PIC_1, cursor.getString(5));
                map.put(COLUMN_PIC_2, cursor.getString(6));
                map.put(COLUMN_PIC_3, cursor.getString(7));
                map.put(COLUMN_PIC_4, cursor.getString(8));
                map.put(COLUMN_PIC_5, cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllData_sort_asc_date() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " ORDER BY last_maintenance ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_SERIAL, cursor.getString(0));
                map.put(COLUMN_NAME, cursor.getString(1));
                map.put(COLUMN_SPEC, cursor.getString(2));
                map.put(COLUMN_LAST_MAINTENANCE, cursor.getString(3));
                map.put(COLUMN_SUM_IMAGE, cursor.getString(4));
                map.put(COLUMN_PIC_1, cursor.getString(5));
                map.put(COLUMN_PIC_2, cursor.getString(6));
                map.put(COLUMN_PIC_3, cursor.getString(7));
                map.put(COLUMN_PIC_4, cursor.getString(8));
                map.put(COLUMN_PIC_5, cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllData_sort_asc_serial() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " ORDER BY serial_number ASC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_SERIAL, cursor.getString(0));
                map.put(COLUMN_NAME, cursor.getString(1));
                map.put(COLUMN_SPEC, cursor.getString(2));
                map.put(COLUMN_LAST_MAINTENANCE, cursor.getString(3));
                map.put(COLUMN_SUM_IMAGE, cursor.getString(4));
                map.put(COLUMN_PIC_1, cursor.getString(5));
                map.put(COLUMN_PIC_2, cursor.getString(6));
                map.put(COLUMN_PIC_3, cursor.getString(7));
                map.put(COLUMN_PIC_4, cursor.getString(8));
                map.put(COLUMN_PIC_5, cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }
    public ArrayList<HashMap<String, String>> getAllData_sort_desc_serial() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " ORDER BY serial_number DESC";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_SERIAL, cursor.getString(0));
                map.put(COLUMN_NAME, cursor.getString(1));
                map.put(COLUMN_SPEC, cursor.getString(2));
                map.put(COLUMN_LAST_MAINTENANCE, cursor.getString(3));
                map.put(COLUMN_SUM_IMAGE, cursor.getString(4));
                map.put(COLUMN_PIC_1, cursor.getString(5));
                map.put(COLUMN_PIC_2, cursor.getString(6));
                map.put(COLUMN_PIC_3, cursor.getString(7));
                map.put(COLUMN_PIC_4, cursor.getString(8));
                map.put(COLUMN_PIC_5, cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getDataMachine(String serial) {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM " + TABLE_SQLite + " WHERE serial_number = '" + serial +"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_SERIAL, cursor.getString(0));
                map.put(COLUMN_NAME, cursor.getString(1));
                map.put(COLUMN_SPEC, cursor.getString(2));
                map.put(COLUMN_LAST_MAINTENANCE, cursor.getString(3));
                map.put(COLUMN_SUM_IMAGE, cursor.getString(4));
                map.put(COLUMN_PIC_1, cursor.getString(5));
                map.put(COLUMN_PIC_2, cursor.getString(6));
                map.put(COLUMN_PIC_3, cursor.getString(7));
                map.put(COLUMN_PIC_4, cursor.getString(8));
                map.put(COLUMN_PIC_5, cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + wordList);

        database.close();
        return wordList;
    }

    /*public void insert(String name, String spesification, String last_maintenance, String pic1, String pic2, String pic3, String pic4, String pic5) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_SQLite + " (name, spesification, last_maintenance, pic1, pic2, pic3, pic4, pic5) " +
                "VALUES ('" + name + "', '" + spesification + "', '" + last_maintenance +"', '" + pic1 +
                "', '" + pic2 +"', '" + pic3 +"', '" + pic4 +"', '" + pic5 +"')";

        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }*/

    public void update(String id, String name, String spec, String last_main) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_SQLite + " SET "
                + COLUMN_NAME + "='" + name + "', "
                + COLUMN_SPEC + "='" + spec + "',"
                + COLUMN_LAST_MAINTENANCE + "='" + last_main + "'"
                //+ COLUMN_PIC_1 + "='" + pic1 + "',"
                //+ COLUMN_PIC_2 + "='" + pic2 + "',"
                //+ COLUMN_PIC_3 + "='" + pic3 + "',"
                //+ COLUMN_PIC_4 + "='" + pic4 + "',"
                //+ COLUMN_PIC_5 + "='" + pic5 + "'"
                + " WHERE " + COLUMN_SERIAL + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void delete(String id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_SQLite + " WHERE " + COLUMN_SERIAL + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

    public void insert(String serial, String name, String spesification, String last_maintenance, int img_count) {
        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_SQLite + " (serial_number,name, spesification, last_maintenance,image_count, pic1, pic2, pic3, pic4, pic5) " +
                "VALUES ('" + serial + "','" + name + "', '" + spesification + "', '" + last_maintenance +"','"+img_count+"','NO IMAGE','NO IMAGE','NO IMAGE','NO IMAGE','NO IMAGE')";

        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }
    public void upload_image(String id, String path, int no) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "UPDATE " + TABLE_SQLite + " SET "
                + "pic" + no + "='" + path + "'"
                + " WHERE " + COLUMN_SERIAL + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
}
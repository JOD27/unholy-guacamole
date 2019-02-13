package com.example.unholy_guacamole;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final String D_TAG = "D_TAG";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "swear_table.db";
    //table variables
    public static final String TABLE_NAME = "swear_table";
    public static final String KEY_ID = "id";
    public static final String SWEAR = "key_swear";
    public static final String REPLACEMENT = "replacement";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY, " + SWEAR + " TEXT, " + REPLACEMENT+ " TEXT " +")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);   //delete old table
        onCreate(db);   //recreate new table
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void deleteData(){
        //delete all data in table
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        Log.d(D_TAG, "deleted data");
        db.close();
    }

    public void add_kv(final String key, final String val){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new_row(key, val);
            }
        }).start();
    }

    public void new_row(final String key, final String val) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SWEAR, key);
        contentValues.put(REPLACEMENT, val);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

}
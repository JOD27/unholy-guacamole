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
    //table variables: S is for the swear table, and R is for replacement table
    public static final String S_TABLE_NAME = "swear_table";
    public static final String S_KEY_ID = "s_id";
    public static final String S_SWEAR = "swear";

    public static final String R_TABLE_NAME = "replacement_table";
    public static final String R_KEY_ID = "r_id";
    public static final String R_REPLACEMENT = "replacement";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String S_CREATE_TABLE = "CREATE TABLE " + S_TABLE_NAME + "("
                + S_KEY_ID + " INTEGER PRIMARY KEY, " + S_SWEAR + " TEXT" +")";
        db.execSQL(S_CREATE_TABLE);

        String R_CREATE_TABLE = "CREATE TABLE " + R_TABLE_NAME + "("
                + R_KEY_ID + " INTEGER PRIMARY KEY, " + R_REPLACEMENT + " TEXT" +")";
        db.execSQL(R_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + S_TABLE_NAME);   //delete old table
        db.execSQL("DROP TABLE IF EXISTS " + R_TABLE_NAME);   //delete old table
        onCreate(db);   //recreate new table
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void deleteData(){
        //delete all data in table
        SQLiteDatabase db= this.getWritableDatabase();
        db.execSQL("DELETE FROM " + R_TABLE_NAME);
        db.execSQL("DELETE FROM " + S_TABLE_NAME);
        Log.d(D_TAG, "deleted data");
        db.close();
    }

    public void add_entry(final String val, final String table_name,final String col){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new_row(val, table_name, col);
            }
        }).start();
    }

    public void new_row(final String val, String table_name, String col) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col, val);
        db.insert(table_name, null, contentValues);
        db.close();
    }

}
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
        String S_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + S_TABLE_NAME + "("
                + S_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + S_SWEAR + " TEXT" +")";
        db.execSQL(S_CREATE_TABLE);

        String R_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + R_TABLE_NAME + "("
                + R_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + R_REPLACEMENT + " TEXT" +")";
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(col, val);
        db.insert(table_name, null, contentValues);
        db.close();

    }

    public long check_table_size(){
        //returns the number of rows (notes) in the database
        SQLiteDatabase database = this.getReadableDatabase();
        long row_count_s = DatabaseUtils.queryNumEntries(database, S_TABLE_NAME);
        //long row_count_r = DatabaseUtils.queryNumEntries(database, R_TABLE_NAME);
        database.close();

        //Log.d("d_tag", "swears: "+String.valueOf(row_count_s) + "\nreplacesments: " + String.valueOf(row_count_r));
        return row_count_s;
    }

    public void return_replacement(int row_id){
        //returns a single replacement_word from the database.
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(
                "SELECT "+R_REPLACEMENT + " FROM " + R_TABLE_NAME + " WHERE " + R_KEY_ID + " = " + String.valueOf(row_id), null);
        if (cursor != null){
            cursor.moveToFirst();
            String rep = "empty cursor";
           try{
                rep =cursor.getString(0);
            }catch (Exception e){
                Log.d(D_TAG, e.toString());
            }
            cursor.close();
            database.close();
            Log.d("d_tag","found replacement: " + rep);
        }else{
            cursor.close();
            database.close();
            Log.d("d_tag"," blank cursor");
        }
    }


}
package com.example.unholy_guacamole;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Random;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "swear_table.db";
    //table variables: S is for the swear table, and R is for replacement table
    public static final String S_TABLE_NAME = "swear_table";
    public static final String S_KEY_ID = "s_id";
    public static final String S_SWEAR = "swear";

    public static final String R_TABLE_NAME = "replacement_table";
    public static final String R_KEY_ID = "r_id";
    public static final String R_REPLACEMENT = "replacement";

    public int r_table_size = -1;
    public SQLiteDatabase writedb;

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
        //Log.d(D_TAG, "deleted data");
        db.close();
    }

    public void add_entry(final String val, final String table_name,final String col){
        ContentValues contentValues = new ContentValues();
        contentValues.put(col, val);
        writedb.insert(table_name, null, contentValues);

    }

    public long get_r_table_size(){
        //returns the number of rows (notes) in the database
        SQLiteDatabase database = this.getReadableDatabase();
        long row_count_r = DatabaseUtils.queryNumEntries(database, R_TABLE_NAME);
        database.close();
        return row_count_r;
    }

    public String return_replacement(int row_id){
        //returns a single replacement_word from the table,
        // in the range from 1 to get_table_size().
        SQLiteDatabase database = this.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT "+R_REPLACEMENT+" FROM " + R_TABLE_NAME + " WHERE "
                        + R_KEY_ID + " = " + String.valueOf(row_id), null);
        String rep = "";
        if (cursor != null && cursor.moveToFirst()){
           try{
                rep =cursor.getString(0);
            }catch (Exception e){
                Log.d("d_tag", e.toString());
            }
            //Log.d("d_tag","found replacement: " + rep);
            cursor.close();
        }
        database.close();
        return rep;
    }

    public boolean search_swears(String swear){
        //searches the table of swears and returns true if the swear is found
        SQLiteDatabase db = this.getReadableDatabase();
        String querey =  "SELECT EXISTS (SELECT * FROM "+S_TABLE_NAME+" WHERE "+S_SWEAR+"='"+swear+"' LIMIT 1)";
        Cursor cursor = db.rawQuery(querey, null);
        boolean exists= false;
        if (cursor != null && cursor.moveToFirst() && cursor.getInt(0) == 1){
            //Log.d("d_tag", "found swear in table");
            exists = true;
            cursor.close();
        }
        db.close();
        return exists;
    }


    public String generate_random_word(){
        if (r_table_size == -1){
            r_table_size = (int) get_r_table_size();
        }
        int rand = new Random(System.currentTimeMillis()).nextInt(r_table_size) +1;
        // +1 is to get rid of case where 0 appears and has no index in table
        return return_replacement(rand);
    }



}
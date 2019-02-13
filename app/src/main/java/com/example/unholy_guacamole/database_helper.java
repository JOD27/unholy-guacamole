package com.example.unholy_guacamole;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class database_helper extends SQLiteOpenHelper{

    private final String D_TAG = "D_TAG";
    public static final int DATABASE_VERSION = 1;
    public static final String db_name = "database_test.db";
    //table variables build in already

    private final String dbpath = "data/data/com.example.unholy-guacamole/databases/";
    private final String db_filename = dbpath + db_name;
    private Context class_context;

    private SQLiteDatabase db;


    public database_helper(Context context) {
        super(context, db_name, null, DATABASE_VERSION);
        class_context = context;
    }

    private void copyDataBase(Context context){

        try {
            InputStream myInput = context.getAssets().open(db_name);
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(db_filename);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(db_filename, null,
                    SQLiteDatabase.OPEN_READWRITE);
            checkDB.close();
        } catch (Exception e) {

        }
        return checkDB != null;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
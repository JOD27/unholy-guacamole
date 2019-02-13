package com.example.unholy_guacamole;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String swearfilename = "loadfromfile.txt";
    private DatabaseHelper DBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText messageInput = (EditText) findViewById(R.id.editText);
        messageInput.getText().append(	"\uD83E\uDD14");

        DBhelper = new DatabaseHelper(this);
        DBhelper.deleteData();

        //if assets directory is not empty, read from file, generate table and delete from file.
        //else if asseets directory is emtpty, do nothing

        boolean dosetup = check_setupneeded(); // check if  we nneed to do setup

        if(dosetup){
            create_from_assets();
        }
    }

    private boolean check_setupneeded() {
        AssetManager manager = getResources().getAssets();
        try {
            InputStream is = manager.open(swearfilename);
            is.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    private void create_from_assets(){
        //create database from file
        //create swear table
        Thread create_s_table = new Thread(new Runnable() {
            @Override
            public void run() {
                read_file_to_db("swears.txt", DatabaseHelper.S_TABLE_NAME, DatabaseHelper.S_SWEAR);
            }
        });
        Thread create_r_table = new Thread(new Runnable() {
            @Override
            public void run() {
                read_file_to_db("replacements.txt", DatabaseHelper.R_TABLE_NAME, DatabaseHelper.R_REPLACEMENT);
            }
        });
        create_s_table.start();
        create_r_table.start();

    }

    private void read_file_to_db(String fname, String table_name, String col_name){
        //reads from a given txt file to a database
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader((getAssets().open(fname))));
            String line = reader.readLine();
            while(line != null){
                DBhelper.add_entry(line ,table_name, col_name);

                line = reader.readLine();
            }
            reader.close();
        }catch (Exception e){
            Log.d("d_tag", e.toString() + "filereader failed, file possibly doesnt exist?");
        }



    }



}

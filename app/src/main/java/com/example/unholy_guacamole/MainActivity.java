package com.example.unholy_guacamole;

import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
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
        boolean key = true;     //false means we have read a value
        BufferedReader reader = null;
        String key_swear = "empty";
        try{
            reader = new BufferedReader(new FileReader(swearfilename));
            String line = reader.readLine();
            while(line != null){
                if(!key){
                    // take current value from line, pair with with the key from the previous line,
                    // and aadd the two to a new row in the daatabase.
                    DBhelper.add_kv(key_swear, line);
                }else{
                    key_swear = line;
                }

                key = !key;
                line = reader.readLine();
            }
            reader.close();
        }catch (Exception e){
            Log.d("d_tag", e.toString() + "filereader failed, file possibly doesnt exist?");
        }

    }



}

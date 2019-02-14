package com.example.unholy_guacamole;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private String s_file = "swears.txt";   //file with swears
    private String r_file = "replacements.txt";   //file with replacements for swears
    private DatabaseHelper DBhelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText messageInput = (EditText) findViewById(R.id.editText);
        messageInput.getText().append(	"\uD83E\uDD14");

        DBhelper = new DatabaseHelper(this);
        //DBhelper.deleteData();    //used for testing creation and deletion of databsese

        //if assets directory is not empty, read from file, generate table and delete from file.
        //else if asseets directory is emtpty, do nothing

        if(DBhelper.check_table_size() == 0){
            Log.d("d_tag", "doing setup");
            create_from_assets();
        }
          //used for testing that all databases had been initialised

        //DBhelper.return_replacement(2);     //testing the database
    }


    private void create_from_assets(){
        //create database from file
        //create swear table
        read_file_to_db(s_file, DatabaseHelper.S_TABLE_NAME, DatabaseHelper.S_SWEAR);
        read_file_to_db(r_file, DatabaseHelper.R_TABLE_NAME, DatabaseHelper.R_REPLACEMENT);

    }

    private void read_file_to_db(String fname, String table_name, String col_name){
        //reads from a given txt file to a database
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(
                    (getResources().getAssets().open(fname))));
            String line = reader.readLine();
            while(line != null){
                DBhelper.add_entry(line ,table_name, col_name);

                line = reader.readLine();
            }
            reader.close();
        }catch (Exception e){
            Log.d("d_tag", e.toString()+
                    "filereader failed, file possibly doesnt exist?");
        }
    }

}

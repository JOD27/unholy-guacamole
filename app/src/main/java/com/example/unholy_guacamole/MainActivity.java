package com.example.unholy_guacamole;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MainActivity extends AppCompatActivity {

    private String s_file = "swears.txt";   //file with swears
    private String r_file = "replacements.txt";   //file with replacements for swears
    private DatabaseHelper DBhelper;
    private boolean pressed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void create_from_assets(){
        //create database from file
        //create swear table

        new Thread(new Runnable() {
            @Override
            public void run() {
                DBhelper.writedb = DBhelper.getWritableDatabase();
                read_file_to_db(s_file, DatabaseHelper.S_TABLE_NAME, DatabaseHelper.S_SWEAR);
                read_file_to_db(r_file, DatabaseHelper.R_TABLE_NAME, DatabaseHelper.R_REPLACEMENT);
                DBhelper.writedb.close();
            }
        }).start();

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

    public void setup_button(View v){
        DBhelper = new DatabaseHelper(this);
        //DBhelper.deleteData();    //used for testing creation and deletion of databsese

        TextView tv = (TextView)findViewById(R.id.textView3);
        if(DBhelper.get_r_table_size() == 0 && !pressed){
            Log.d("d_tag", "doing setup");
            create_from_assets();

        }
        pressed = true;
        tv.setText("setup complete");
        //used for testing that all databases had been initialised
        //String replacement = DBhelper.return_replacement(2);     //testing the database
        //boolean exists = DBhelper.search_swears("xxx");
        //boolean doesnt_exist = DBhelper.search_swears("xxas");
        //Log.d("d_tag", replacement + " 0: " + exists + " 1: " + doesnt_exist);
    }

}

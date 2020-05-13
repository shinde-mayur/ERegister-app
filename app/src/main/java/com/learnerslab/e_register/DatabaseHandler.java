package com.learnerslab.e_register;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Toast;


public class DatabaseHandler {
    SQLiteDatabase database;
    Activity activity;

    public DatabaseHandler(Activity activity) {
        this.activity = activity;
        database = activity.openOrCreateDatabase("ASSIST", activity.MODE_PRIVATE, null);
        createTable();
    }

    public void createTable() {
        try {
            String qu = "CREATE TABLE IF NOT EXISTS STUDENT(name varchar(1000)," +
                    "cl varchar(100), " +
                    "regno varchar(100) primary key, contact varchar(100),roll integer);";
            database.execSQL(qu);
        } catch (Exception e) {
            Toast.makeText(activity, "Error Occured for create table", Toast.LENGTH_LONG).show();
        }
        try {
            String qu = "CREATE TABLE IF NOT EXISTS ATTENDANCE(datex date," +
                    "hour int , " +
                    "register varchar(100) ,isPresent boolean,sub varchar(100));";
            database.execSQL(qu);
        } catch (Exception e) {
            Toast.makeText(activity, "Error Occured for create table", Toast.LENGTH_LONG).show();
        }
        try {
            String qu = "CREATE TABLE IF NOT EXISTS SCHEDULE(subject varchar(1000));";
            database.execSQL(qu);
        } catch (Exception e) {
            Toast.makeText(activity, "Error Occured for create table", Toast.LENGTH_LONG).show();
        }
    }

    public boolean execAction(String qu) {
        Log.i("databaseHandler", qu);
        try {
            database.execSQL(qu);
        } catch (Exception e) {
            Log.e("databaseHandler", qu);
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public Cursor execQuery(String qu) {
        try {
            //Toast.makeText(activity.getApplicationContext(),"Query executed",Toast.LENGTH_SHORT).show();
            return database.rawQuery(qu, null);
        } catch (Exception e) {
            Log.e("databaseHandler", qu);
              Toast.makeText(activity,"Error Occured for execAction",Toast.LENGTH_LONG).show();
        }
        return null;
    }

}

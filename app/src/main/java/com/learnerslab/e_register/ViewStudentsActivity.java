package com.learnerslab.e_register;

import android.app.Activity;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewStudentsActivity extends AppCompatActivity {

    Spinner subList;
    public static DatabaseHandler handler;
    public static Activity activity;
    public static ArrayList<String> students;
    ListView listView;
    String subName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);
        listView = (ListView) findViewById(R.id.studentsListView);
        handler= new DatabaseHandler(this);
        subList=(Spinner)findViewById(R.id.subList2);
        activity = this;
        students=new ArrayList<>();
        //loadStudents();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.subs);
        subList.setAdapter(adapter);
        subList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subName=subList.getSelectedItem().toString();
                loadStudents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadStudents() {
        students.clear();
        String qu = "SELECT * FROM STUDENT WHERE cl ='"+subName+"'ORDER BY roll";
        Toast.makeText(getApplicationContext(),subName,Toast.LENGTH_LONG).show();
        Cursor cursor = MainActivity.handler.execQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            students.add("No Students for selected subject");
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                students.add("Name: "+cursor.getString(0) + "\nClass: " + cursor.getString(1) + "\nRegister Number: " + cursor.getString(2) + "\nContact: " + cursor.getString(3));
                cursor.moveToNext();
            }
        }
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, students);
        listView.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);

    }
}

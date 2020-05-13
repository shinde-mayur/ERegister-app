package com.learnerslab.e_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewAttendanceActivity extends AppCompatActivity {

    Spinner subList;
    public static DatabaseHandler handler;
    public static ArrayList<String> students;
    ListView listView;
    AutoCompleteTextView studentName;
    String subName,studName;
    String reg="";
    ArrayList<String> name;
    Activity profileActivity = this;
    ProfileAdapter adapter;
    ArrayList<String> dates;
    ArrayList<String> datesALONE;
    ArrayList<Integer> hourALONE;
    ArrayList<Boolean> atts;
    Activity activity = this;
    TextView textView;
    private View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);
        listView = (ListView) findViewById(R.id.studentsListView1);
        textView = (TextView) findViewById(R.id.profileContentView);
        dates = new ArrayList<>();
        datesALONE = new ArrayList<>();
        hourALONE = new ArrayList<>();
        atts = new ArrayList<>();
        handler= new DatabaseHandler(this);
        subList=(Spinner)findViewById(R.id.subList3);
        studentName=(AutoCompleteTextView)findViewById(R.id.name);
        activity = this;
        students=new ArrayList<>();
        autoComplete();
        textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Delete Student");
                alert.setMessage("Are you sure ?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String qu = "DELETE FROM STUDENT WHERE REGNO = '" + reg + "'";
                        if (MainActivity.handler.execAction(qu)) {
                            Log.d("delete", "done from student");
                            String qa = "DELETE FROM ATTENDANCE WHERE register = '" + reg + "'";
                            if (MainActivity.handler.execAction(qa)) {
                                Toast.makeText(getBaseContext(), "Deleted", Toast.LENGTH_LONG).show();
                                Log.d("delete", "done from attendance");
                            }
                        }
                    }
                });
                alert.setNegativeButton("No", null);
                alert.show();
                return true;
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.subs);
        subList.setAdapter(adapter);
        subList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subName=subList.getSelectedItem().toString();
                //loadStudents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void autoComplete() {
        name=new ArrayList<>();
        name.clear();
        String qu = "SELECT name FROM STUDENT ORDER BY roll";
        Cursor cursor = MainActivity.handler.execQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            name.add("No Students");
            listView.setOnItemClickListener(null);
        }else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                name.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        final ArrayAdapter<String> adapterNAME=new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,name);
        studentName.setAdapter(adapterNAME);
        studentName.setThreshold(1);
        studentName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                studName=adapterNAME.getItem(position).toString();

                String qu = "SELECT * FROM STUDENT WHERE name = '" + studName + "'";
                Cursor cursorx = handler.execQuery(qu);
                cursorx.moveToFirst();
                while (!cursorx.isAfterLast()) {
                    reg=cursorx.getString(2);
                    cursorx.moveToNext();
                }
                Toast.makeText(getBaseContext(),reg,Toast.LENGTH_SHORT).show();
                find(v);

            }
        });
    }

    private void find(View v) {
        dates.clear();
        atts.clear();
        TextView textView = (TextView) findViewById(R.id.profileContentView);
        String qu = "SELECT * FROM STUDENT WHERE regno = '" + reg.toUpperCase() + "'";
        String qc = "SELECT * FROM ATTENDANCE WHERE register = '" + reg.toUpperCase() + "';";
        String qd = "SELECT * FROM ATTENDANCE WHERE register = '" + reg.toUpperCase() + "' AND isPresent = 1";
        Cursor cursor = handler.execQuery(qu);
        //Start Count Here
        float att = 0f;
        Cursor cur = handler.execQuery(qc);
        Cursor cur1 = handler.execQuery(qd);
        if (cur == null) {
            Log.d("profile", "cur null");
        }
        if (cur1 == null) {
            Log.d("profile", "cur1 null");
        }
        if (cur != null && cur1 != null) {
            cur.moveToFirst();
            cur1.moveToFirst();
            try {
                att = ((float) cur1.getCount() / cur.getCount()) * 100;
                if (att <= 0)
                    att = 0f;
                Log.d("profile_activity", "Total = " + cur.getCount() + " avail = " + cur1.getCount() + " per " + att);
            } catch (Exception e) {
                att = -1;
            }
        }


        if (cursor == null || cursor.getCount() == 0) {
            assert textView != null;
            textView.setText("No Data Available");
        } else {
            String attendance = "";
            if (att < 0) {
                attendance = "Attendance Not Available";
            } else
                attendance =att + " %";
            cursor.moveToFirst();
            String buffer = "";
            buffer += "Name: " + cursor.getString(0) + "\n";
            buffer += "Subject: " + cursor.getString(1) + "\n";
            buffer += "Class: " + cursor.getString(2) + "\n";
            buffer += "Conatct: " + cursor.getString(3) + "\n";
            buffer += "Roll Number: " + cursor.getInt(4) + "\n";
            buffer += "Attendance: " + attendance + "\n";
            textView.setText(buffer);

            String q = "SELECT * FROM ATTENDANCE WHERE register = '" + reg + "'";
            Cursor cursorx = handler.execQuery(q);
            if (cursorx == null || cursorx.getCount() == 0) {
                Toast.makeText(getBaseContext(), "Information not Available", Toast.LENGTH_LONG).show();
            } else {
                cursorx.moveToFirst();
                while (!cursorx.isAfterLast()) {
                    datesALONE.add(cursorx.getString(0));
                    hourALONE.add(cursorx.getInt(1));
                    dates.add(cursorx.getString(0) + " : " + "Lecture " + cursorx.getInt(1));
                    if (cursorx.getInt(3) == 1)
                        atts.add(true);
                    else {
                        Log.d("profile", cursorx.getString(0) + " -> " + cursorx.getInt(3));
                        atts.add(false);
                    }
                    cursorx.moveToNext();
                }
                adapter = new ProfileAdapter(dates, atts, profileActivity, datesALONE, hourALONE, reg);
                listView.setAdapter(adapter);
            }
        }
    }




    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);

    }
}

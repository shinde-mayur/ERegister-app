package com.learnerslab.e_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MarkAttendanceActivity extends AppCompatActivity {

    SimpleDateFormat sdf;
    Intent intent;
    static String subName;
    ListView listView;
    ListAdapter adapter;
    AlertDialog.Builder alertD;
    ArrayList<String> names;
    ArrayList<String> registers;
    ArrayList<String> data;
    Activity thisActivity = this;
    Button btnSave,btnmatkabsent;
    TextView tvdata;
    public static String period,date;
    String rno,reg;
    EditText atterno;
    static int lecno;
    Boolean att=true;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        load();
        loadData();
    }


    private void loadData() {
        names.clear();
        registers.clear();
        String qu = "SELECT * FROM STUDENT WHERE cl = '" + subName + "' ORDER BY ROLL";
        Cursor cursor = MainActivity.handler.execQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {

        }else
        {
            int ctr = 0;
            cursor.moveToFirst();
            while (!cursor.isAfterLast())
            {
                names.add("Name:- "+cursor.getString(0) + " Roll No:- " + cursor.getInt(4));
                registers.add(cursor.getString(2));
                cursor.moveToNext();
                ctr++;
            }
            if (ctr == 0) {
                Toast.makeText(getBaseContext(), "Students Not Found", Toast.LENGTH_LONG).show();
            }
        }
        for (int i = 0; i < names.size(); i++) {
            int sts = 1;
            String quer = "INSERT INTO ATTENDANCE VALUES('" + MarkAttendanceActivity.date + "'," +
                    "" + MarkAttendanceActivity.lecno + "," +
                    "'" + registers.get(i) + "'," +
                    "'" + sts + "'," +
                    "'" + MarkAttendanceActivity.subName + "'" + ");";
            MainActivity.handler.execAction(quer);
        }
    }

    private void load()
    {
        intent=getIntent();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        alertD = new AlertDialog.Builder(thisActivity);
        alertD.setTitle("ARE YOU SURE ?")
                .setMessage("No student is absent ?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        thisActivity.finish();
                    }
                });
        subName=intent.getStringExtra("subName");
        tvdata=(TextView)findViewById(R.id.data);
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        date = sdf.format(new Date());
        String qu = "SELECT MAX(hour) FROM ATTENDANCE WHERE sub='"+ subName +"'";
        Cursor cursor = MainActivity.handler.execQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(getBaseContext(), "empty", Toast.LENGTH_SHORT).show();
        } else {
            cursor.moveToFirst();
            period = Integer.toString(cursor.getInt(0));
            lecno =Integer.parseInt(period)+1;
            //Toast.makeText(getApplicationContext(), period, Toast.LENGTH_SHORT).show();
        }
        tvdata.setText("Date: "+date+"\nSubject: "+subName+"\nLecture Number: "+lecno);
        listView = (ListView) findViewById(R.id.markAttendanceListView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(thisActivity);
                alert.setTitle("Mark Present?");
                alert.setMessage("Do you want to mark this student as present ?");
                alert.setCancelable(false);
                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qu = "UPDATE ATTENDANCE SET ISPRESENT = " + 1 + " WHERE " +
                                " register = '" + reg + "' AND datex = '" + date+ "'"
                                + " AND hour = " + lecno;
                        if (MainActivity.handler.execAction(qu)) {
                            Toast.makeText(getBaseContext(), rno +" marked as prsent", Toast.LENGTH_LONG).show();
                            data.remove(position);
                            addAbsentInList();
                        } else {
                            Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
                            //addAbsentInList();
                        }
                        dialog.dismiss();
                    }
                });
                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                return false;
            }
        });
        names = new ArrayList<>();
        registers = new ArrayList<>();
        data=new ArrayList<>();
        data.clear();
        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave=(Button)findViewById(R.id.btnSave) ;
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(att)
                    alertD.show();
                else
                    thisActivity.finish();
            }
        });
        btnmatkabsent=(Button)findViewById(R.id.btnmarkabsent);
        atterno=(EditText)findViewById(R.id.atterno);
        atterno.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    switch (keyCode)
                    {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            markAbsent();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        //coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        btnmatkabsent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markAbsent();
            }
        });


    }

    private void markAbsent() {
        rno=atterno.getText().toString();
        String qe="SELECT regno FROM STUDENT WHERE cl = '" + subName + "' AND roll="+rno;
        Cursor c1= MainActivity.handler.execQuery(qe);
        if(c1 == null || c1.getCount() == 0)
        {
            Toast.makeText(getApplicationContext(),"No such student with roll no: "+rno,Toast.LENGTH_SHORT).show();
        }
        else {
            if(att) {
                att = false;
                Toast.makeText(getApplicationContext(),"Long press roll no for marking as present",Toast.LENGTH_SHORT).show();
            }
                c1.moveToFirst();
                reg = c1.getString(0);
                Toast.makeText(getApplicationContext(), reg, Toast.LENGTH_SHORT).show();
                updateAttend();
        }

        atterno.setText("");
    }

    private void addAbsentInList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    private void updateAttend() {

        String qu = "UPDATE ATTENDANCE SET ISPRESENT = " + 0 + " WHERE " +
                " register = '" + reg + "' AND datex = '" + date+ "'"
                + " AND hour = " + lecno;
        if (MainActivity.handler.execAction(qu)) {
            Toast.makeText(getApplicationContext(), rno +" is Absent", Toast.LENGTH_LONG).show();
        }
        atterno.requestFocus();
        data.add(rno+" is absent");
        addAbsentInList();
    }

    @Override
    public void onBackPressed() {
        alertD.show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
               alertD.show();
                break;
        }
        return true;
    }
}

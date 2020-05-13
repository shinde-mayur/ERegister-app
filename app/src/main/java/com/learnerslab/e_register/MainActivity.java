package com.learnerslab.e_register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.app.Activity;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener {

    public static DatabaseHandler handler;
    public static Activity activity;
    public static ArrayList<String> subs;
    ListView listView;
    Toolbar toolbar;
    String qu;
    public static String subName;
    boolean doubleBackToExitPressedOnce = false;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        load();
        loadSubs();
    }
    private void load() {

        // Get the layout inflater

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.attendanceListView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                alert.setTitle("Delete Schedule?");
                alert.setMessage("Do you want to delete this schedule ?");

                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String qu = "DELETE FROM SCHEDULE WHERE subject = '" + subs.get(position) + "'";
                        if (MainActivity.handler.execAction(qu)) {
                            Toast.makeText(getBaseContext(), "Deleted", Toast.LENGTH_LONG).show();
                            loadSubs();
                        } else {
                            Toast.makeText(getBaseContext(), "Failed", Toast.LENGTH_LONG).show();
                            loadSubs();
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
                return true;
            }

        });

        handler= new DatabaseHandler(this);
        activity = this;
        subs=new ArrayList<>();
        intent=new Intent(getApplicationContext(),MarkAttendanceActivity.class);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public  void loadSubs() {
        subs.clear();
        String qu = "SELECT * FROM SCHEDULE ORDER BY subject";
        Cursor cursor = MainActivity.handler.execQuery(qu);
        if (cursor == null || cursor.getCount() == 0) {
            subs.add("No Subjects");
            listView.setOnItemClickListener(null);
            listView.setOnItemLongClickListener(null);
        } else {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {

                subs.add(cursor.getString(0));
                cursor.moveToNext();
            }

            listView.setOnItemClickListener(this);

        }
        ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, subs);
        listView.setAdapter(adapter);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id)
        {
            case R.id.nav_add_subject:
                startActivity(new Intent(getApplicationContext(),AddSubjectActivity.class));
                break;
            case R.id.nav_add_students:
                startActivity(new Intent(getApplicationContext(),AddStudentsActivity.class));
                break;
            case R.id.nav_view_students:
                startActivity(new Intent(getApplicationContext(),ViewStudentsActivity.class));
                break;
            case R.id.nav_view_students_attendance:
                startActivity(new Intent(getApplicationContext(),ViewAttendanceActivity.class));
                break;
            case R.id.nav_settings:
               // startActivity(new Intent(getApplicationContext(),Settings.class));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        subName=subs.get(position);
        intent.putExtra("subName",subName);
        startActivity(intent);
    }
    @Override public void onResume(){
        loadSubs();
        super.onResume();
    }

}

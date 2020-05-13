package com.learnerslab.e_register;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddStudentsActivity extends AppCompatActivity {

    Spinner subList;
    Button btnSave;
    Activity activity = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_students);
        subList=(Spinner)findViewById(R.id.subList);
        btnSave=(Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToDatabase(v);
            }
        });
        addSubs();
    }
    public void saveToDatabase(View view) {
        EditText name = (EditText) findViewById(R.id.edit_name);
        EditText roll = (EditText) findViewById(R.id.roll);
        EditText register = (EditText) findViewById(R.id.register);
        EditText contact = (EditText) findViewById(R.id.contact);
        String classSelected = subList.getSelectedItem().toString();

        if (name.getText().length() < 2 || roll.getText().length() == 0 || register.getText().length() < 2 ||
                contact.getText().length() < 2 || classSelected.length() < 2) {
            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
            alert.setTitle("Invalid");
            alert.setMessage("Insufficient Data");
            alert.setPositiveButton("OK", null);
            alert.show();
            return;
        }

        String qu = "INSERT INTO STUDENT VALUES('" + name.getText().toString() + "'," +
                "'" + classSelected + "'," +
                "'" + register.getText().toString().toUpperCase() + "'," +
                "'" + contact.getText().toString() + "'," +
                "" + Integer.parseInt(roll.getText().toString()) + ");";
        Log.d("Student Reg", qu);
        MainActivity.handler.execAction(qu);
        qu = "SELECT * FROM STUDENT WHERE regno = '" + register.getText().toString() + "';";
        Log.d("Student Reg", qu);
        if (MainActivity.handler.execQuery(qu) != null) {
            Toast.makeText(getBaseContext(), name.getText().toString()+" added", Toast.LENGTH_LONG).show();
            name.setText("");
            roll.setText("");
            register.setText("");
            contact.setText("");
        }
    }
    void addSubs()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, MainActivity.subs);
        subList.setAdapter(adapter);
    }
    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);

    }
}

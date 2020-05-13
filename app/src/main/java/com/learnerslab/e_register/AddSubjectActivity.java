package com.learnerslab.e_register;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class AddSubjectActivity extends AppCompatActivity {

    EditText subName;
    String sub;
    Button btnSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        btnSave=(Button)findViewById(R.id.btnsave);
        subName=(EditText)findViewById(R.id.subName);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sub=subName.getText().toString();
                if (sub.length() <2) {
                    Toast.makeText(getBaseContext(), "Enter Valid Subject Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                String sql = "INSERT INTO SCHEDULE VALUES('" + sub + "');";
                Log.d("Schedule", sql);
                if (MainActivity.handler.execAction(sql))
                {
                    subName.setText("");
                    Toast.makeText(getBaseContext(), sub+" Added", Toast.LENGTH_LONG).show();
                } else
                    Toast.makeText(getBaseContext(), "Failed To Add "+sub, Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed() {

        NavUtils.navigateUpFromSameTask(this);
    }
}

package com.todolist.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.todolist.R;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vinayduvvada on 9/29/2016.
 */

public class AddItemActivity extends AppCompatActivity {
    DatePicker datePicker;
    private int year;
    private int month;
    private int day;
    SQLiteDatabase db;
    EditText title, notes;
    Spinner priority, status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        title = (EditText) findViewById(R.id.et_task_name);
        notes = (EditText) findViewById(R.id.et_task_notes);
        datePicker = (DatePicker) findViewById(R.id.dp_due_date);
        datePicker.setMinDate(new Date().getTime());
        priority = (Spinner) findViewById(R.id.sp_priority_level);
        status = (Spinner) findViewById(R.id.sp_status);
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        datePicker.init(year, month, day, null);
        db = openOrCreateDatabase("ToDoDB", MODE_PRIVATE, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                save();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void save() {
        String date = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
        String titleText = title.getText().toString();
        String notesText = notes.getText().toString();
        String priorityText = priority.getSelectedItem().toString();
        String statusText = status.getSelectedItem().toString();
        if (titleText.equals("") || notesText.equals("") || priorityText.equals("") || statusText.equals("")) {
            Toast.makeText(this, "All Fields are Mandatory", Toast.LENGTH_LONG).show();
        } else {
            Log.d("AddItem", date);
            String query = "INSERT INTO tasks(title,taskdate,tasknotes,priority,status) values('" + titleText + "','" + date + "','"
                    + notesText + "','" + priorityText + "','" + statusText + "')";
            db.execSQL(query);
            Toast.makeText(this, "Task Added Successfully!", Toast.LENGTH_SHORT).show();

            this.finish();
        }
    }
}

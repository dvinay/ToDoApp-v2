package com.todolist.activities;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.todolist.R;
import com.todolist.fragments.ViewTaskFragment;

public class ViewTaskActivity extends AppCompatActivity {
    int id;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_task);
        id = getIntent().getIntExtra("position", -1);
        db = openOrCreateDatabase("ToDoDB", MODE_PRIVATE, null);
        if (findViewById(R.id.task_fragment) != null) {
            if (savedInstanceState != null) {
                return;
            }
            ViewTaskFragment fragment = new ViewTaskFragment();
            fragment.setArguments(getIntent().getExtras());
            getFragmentManager().beginTransaction()
                    .add(R.id.task_fragment, fragment).commit();
        }
    }

}

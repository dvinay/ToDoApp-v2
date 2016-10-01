package com.todolist.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.todolist.R;
import com.todolist.adapters.TaskAdapter;
import com.todolist.models.Task;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Task> taskList;
    SQLiteDatabase db;
    String title, taskdate, notes, priority, status;
    int taskid;
    TaskAdapter taskAdapter;
    ListView taskListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taskList = new ArrayList<>();
        taskAdapter = new TaskAdapter(this, taskList);
        taskListView = (ListView) findViewById(R.id.lv_task);
        if (taskListView != null) {
            taskListView.setAdapter(taskAdapter);
            taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(MainActivity.this, ViewTaskActivity.class);
                    intent.putExtra("position", taskList.get(position).taskid);
                    startActivity(intent);
                }
            });
        }
        db = openOrCreateDatabase("ToDoDB", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS tasks(taskid integer primary key autoincrement,title varchar,taskdate varchar,tasknotes varchar,priority varchar,status varchar)");
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskList.clear();
        Cursor c = db.query("tasks", null, null, null, null, null, "taskid DESC");
        while (c.moveToNext()) {
            taskid = c.getInt(c.getColumnIndex("taskid"));
            title = c.getString(c.getColumnIndex("title"));
            taskdate = c.getString(c.getColumnIndex("taskdate"));
            notes = c.getString(c.getColumnIndex("tasknotes"));
            priority = c.getString(c.getColumnIndex("priority"));
            status = c.getString(c.getColumnIndex("status"));
            taskList.add(new Task(taskid, title, taskdate, notes, priority, status));
            Log.d("MainAct", taskid + " " + title + " " + taskdate + " " + notes + " " + priority + " " + status);
        }
        Log.d("MainAct", taskList.size() + "");
        taskAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_task:
                startActivity(new Intent(this, AddItemActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

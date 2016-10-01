package com.todolist.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.todolist.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Vinayduvvada on 9/29/2016.
 */

public class ViewTaskFragment extends Fragment {
    SQLiteDatabase db;
    int id;
    TextView taskName, dueDate, taskNotes, priority, status;

    public ViewTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ViewTask", "in resume");

        Bundle bundle = getArguments();
        id = bundle.getInt("position", -1);
        fetchRow(id);
    }

    void fetchRow(int id) {
        db = getActivity().openOrCreateDatabase("ToDoDB", MODE_PRIVATE, null);
        Cursor cursor = db.query("tasks", null, "taskid=" + id, null, null, null, null);
        if (cursor.moveToNext()) {
            taskName.setText(cursor.getString(cursor.getColumnIndex("title")));
            dueDate.setText(cursor.getString(cursor.getColumnIndex("taskdate")));
            taskNotes.setText(cursor.getString(cursor.getColumnIndex("tasknotes")));
            priority.setText(cursor.getString(cursor.getColumnIndex("priority")));
            status.setText(cursor.getString(cursor.getColumnIndex("status")));
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getActivity().getMenuInflater().inflate(R.menu.menu_view_task, menu);
//
//        return true;
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_view_task, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_task:
                edit();
                return true;
            case R.id.menu_delete_task:
                delete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void edit() {
        EditTaskFragment editTaskFragment = new EditTaskFragment();
        Bundle args = new Bundle();
        args.putInt("position", id);
        editTaskFragment.setTargetFragment(ViewTaskFragment.this, 100);
        editTaskFragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        editTaskFragment.show(fragmentManager, "Edit Fragment");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    id = data.getIntExtra("position", -1);
                    fetchRow(id);
                }
        }
    }

    void delete() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Warning")
                .setMessage("Do you really want to Delete Task?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        db.delete("tasks", "taskid=" + id, null);
                        Toast.makeText(getActivity(), "Task Deleted Successfully", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_view_task, container, false);
        taskName = (TextView) root.findViewById(R.id.tv_vt_title_value);
        dueDate = (TextView) root.findViewById(R.id.tv_vt_due_date_value);
        taskNotes = (TextView) root.findViewById(R.id.tv_vt_notes_value);
        priority = (TextView) root.findViewById(R.id.tv_vt_priority_value);
        status = (TextView) root.findViewById(R.id.tv_vt_status_value);

        return root;
    }
}

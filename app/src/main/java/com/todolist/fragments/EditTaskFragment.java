package com.todolist.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.todolist.R;

import java.util.Date;
import java.util.StringTokenizer;

import static android.widget.Toast.makeText;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditTaskFragment extends DialogFragment {

    EditText title, notes;
    DatePicker datePicker;
    Spinner priority, status;
    SQLiteDatabase db;
    Toolbar toolbar;
    int id;

    public EditTaskFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_Material_Light_Dialog_MinWidth);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_edit_task, container, false);
        getDialog().setTitle("Edit Task");
        db = getActivity().openOrCreateDatabase("ToDoDB", Context.MODE_PRIVATE, null);
        Bundle bundle = getArguments();
        id = bundle.getInt("position", -1);
        title = (EditText) rootView.findViewById(R.id.et_edit_task_name);
        notes = (EditText) rootView.findViewById(R.id.et_edit_task_notes);
        datePicker = (DatePicker) rootView.findViewById(R.id.dp_edit_due_date);
        datePicker.setMinDate(new Date().getTime());
        priority = (Spinner) rootView.findViewById(R.id.sp_edit_priority_level);
        status = (Spinner) rootView.findViewById(R.id.sp_edit_status);
        toolbar = (Toolbar) rootView.findViewById(R.id.edit_task_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_tick);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        Cursor cursor = db.query("tasks", null, "taskid=" + id, null, null, null, null);
        if (cursor.moveToNext()) {
            title.setText(cursor.getString(cursor.getColumnIndex("title")));
            String date = cursor.getString(cursor.getColumnIndex("taskdate"));
            StringTokenizer st = new StringTokenizer(date, "-");
            int dateInt = Integer.parseInt(st.nextToken());
            int monthInt = Integer.parseInt(st.nextToken());
            int yearInt = Integer.parseInt(st.nextToken());
            datePicker.init(yearInt, monthInt - 1, dateInt, null);
            notes.setText(cursor.getString(cursor.getColumnIndex("tasknotes")));
            String pr = cursor.getString(cursor.getColumnIndex("priority"));
            if (pr.equals("HIGH")) {
                priority.setSelection(0);
            } else if (pr.equals("MEDIUM")) {
                priority.setSelection(1);
            } else if (pr.equals("LOW")) {
                priority.setSelection(2);
            }
            String statusString = cursor.getString(cursor.getColumnIndex("status"));
            if (statusString.equals("Done")) {
                status.setSelection(0);
            } else if (statusString.equals("To-Do")) {
                status.setSelection(1);
            }
        }

        cursor.close();
        return rootView;
    }

    public void save() {
        String date = datePicker.getDayOfMonth() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getYear();
        String titleText = title.getText().toString();
        String notesText = notes.getText().toString();
        String priorityText = priority.getSelectedItem().toString();
        String statusText = status.getSelectedItem().toString();
        if (titleText.equals("") || notesText.equals("") || priorityText.equals("") || statusText.equals("")) {

            makeText(getActivity(), "All Fields are Mandatory", Toast.LENGTH_LONG).show();
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", title.getText().toString());
            contentValues.put("taskdate", date);
            contentValues.put("tasknotes", notes.getText().toString());
            contentValues.put("priority", priority.getSelectedItem().toString());
            contentValues.put("status", status.getSelectedItem().toString());
            db.update("tasks", contentValues, "taskid=" + id, null);
            Intent intent = new Intent();
            intent.putExtra("position", id);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
            this.dismiss();
        }
    }
}
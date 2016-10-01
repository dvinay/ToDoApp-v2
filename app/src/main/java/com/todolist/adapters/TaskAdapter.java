package com.todolist.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.todolist.R;
import com.todolist.models.Task;

import java.util.ArrayList;

/**
 * Created by Vinayduvvada on 9/29/2016.
 */
public class TaskAdapter extends BaseAdapter {
    ArrayList<Task> taskList;
    Context context;
    public TaskAdapter(Context context,ArrayList<Task> taskList)
    {
        this.context = context;
        this.taskList = taskList;
    }
    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Object getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 10*position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        if(convertView ==null) {
            convertView = layoutInflater.inflate(R.layout.task_single_row, parent, false);
        }
        TextView title = (TextView)convertView.findViewById(R.id.tv_sr_title);
        TextView priority = (TextView)convertView.findViewById(R.id.tv_sr_priority);
        title.setText(taskList.get(position).title);
        String priorityText = taskList.get(position).priority;
        if(priorityText.equalsIgnoreCase("high"))
        {
            priority.setTextColor(Color.RED);
        }
        else if(priorityText.equalsIgnoreCase("medium"))
        {
            priority.setTextColor(Color.BLUE);
        }
        if(priorityText.equalsIgnoreCase("low"))
        {
            priority.setTextColor(Color.GREEN);
        }
        priority.setText(priorityText);
        return convertView;
    }
}

package com.todolist.models;

/**
 * Created by Vinayduvvada on 9/29/2016.
 */

public class Task {

    public String title,date,notes,priority,status;
    public int taskid;
    public Task(int taskid,String title, String date, String notes, String priority, String status) {
        this.taskid = taskid;
        this.title = title;
        this.date = date;
        this.notes = notes;
        this.priority = priority;
        this.status = status;
    }
}

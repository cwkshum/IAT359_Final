package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import static com.example.finalproject.R.layout.activity_tasklayout;

import java.util.ArrayList;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder>{

    // Arraylist to store to do data
    public ArrayList<String> toDoData;
    public String username, checklistName;
    public static Context context;
    public static DetailChecklistActivity activity;

    public ToDoAdapter(ArrayList<String> toDoData, String username, String checklistName, Context context, DetailChecklistActivity activity) {
        this.toDoData = toDoData;
        this.username = username;
        this.checklistName = checklistName;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public ToDoAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // attach layout
        View v = LayoutInflater.from(parent.getContext()).inflate(activity_tasklayout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ToDoAdapter.MyViewHolder holder, int position) {

        // separate to do data
        String[] results = (toDoData.get(position).toString()).split(",");

        // set initial status value
        boolean status = false;

        // display task name
        holder.task.setText(results[0]);

        if (results[1].equals("1")){
            // set status to true (checked) if value in db is 1
            status = true;
        } else if (results[1].equals("0")){
            // set status to false (not checked) if value in db is 0
            status = false;
        }

        // set checkbox to the status from the db
        holder.task.setChecked(status);

        // attach listener for checkbox
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // update db to set the status of the to do item to 1 (checked)
                    ChecklistDatabase db = new ChecklistDatabase(context);
                    db.updateToDoStatus(username, checklistName, results[0], 1);
                } else {
                    // update db to set the status of the to do item to 0 (not checked)
                    ChecklistDatabase db = new ChecklistDatabase(context);
                    db.updateToDoStatus(username, checklistName, results[0], 0);
                }
            }
        });
    }

    public static Context getContext() {
        return activity;
    }

    public void deleteItem(int position) {
        // delete to do item from db
        String[] results = (toDoData.get(position).toString()).split(",");
        ChecklistDatabase db = new ChecklistDatabase(context);
        db.deleteToDo(username, checklistName, results[0]);

        // reloads the activity
        activity.finish();
        activity.startActivity(activity.getIntent());
    }

    public void editItem(int position) {
        // edit to do item in db
        String[] results = (toDoData.get(position).toString()).split(",");

        // start a display to allow users to edit the item
        Bundle bundle = new Bundle();
        bundle.putString("toDoItem", results[0]);
        bundle.putString("username", username);
        bundle.putString("checklistName", checklistName);
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    @Override
    public int getItemCount() {
        // size of arraylist containing to do data
        return toDoData.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public CheckBox task;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);

            task = (CheckBox) itemView.findViewById(R.id.todoCheckBox);
            context = itemView.getContext();

        }
    }
}
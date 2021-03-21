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
        View v = LayoutInflater.from(parent.getContext()).inflate(activity_tasklayout, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ToDoAdapter.MyViewHolder holder, int position) {


        String[] results = (toDoData.get(position).toString()).split(",");
        boolean status = false;

        holder.task.setText(results[0]);

        if (results[1].equals("1")){
            status = true;
        } else if (results[1].equals("0")){
            status = false;
        }

        holder.task.setChecked(status);
        holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // UPDATE DATABASE
                    ChecklistDatabase db = new ChecklistDatabase(context);
                    db.updateToDoStatus(username, checklistName, results[0], 1);
                } else {
                    // UPDATE DATABASE
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
        String[] results = (toDoData.get(position).toString()).split(",");
        ChecklistDatabase db = new ChecklistDatabase(context);
        db.deleteToDo(username, checklistName, results[0]);

        // reloads the activity
        activity.finish();
        activity.startActivity(activity.getIntent());
    }

    public void editItem(int position) {
        String[] results = (toDoData.get(position).toString()).split(",");
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
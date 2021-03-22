package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.example.finalproject.R.layout.activity_checklist_row;

import java.util.ArrayList;

public class ChecklistAdapter extends RecyclerView.Adapter<ChecklistAdapter.MyViewHolder> {

    // arraylist to store checklist data
    public static ArrayList<String> checklistData;
    Context context;
    static final int REQUEST_TODO = 1;
    static final int REQUEST_EDIT = 2;

    public ChecklistAdapter(ArrayList<String> checklistData, Context context) {
        this.checklistData = checklistData;
        this.context = context;
    }

    @Override
    public ChecklistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(activity_checklist_row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ChecklistAdapter.MyViewHolder holder, int position) {

        // Separate stored checklist data
        String[] results = (checklistData.get(position).toString()).split(",");

        // set the checklist number based on its position in the recyclerview
        int checklistNum = position + 1;
        String num = String.valueOf(checklistNum);
        holder.checklist_id_txt.setText(num);

        // Display the name of the checklist
        holder.checklistName_txt.setText(results[1]);

        // display the description of the checklist
        holder.checklistDescription_txt.setText(results[2]);

    }


    @Override
    public int getItemCount() {
        // the size of the arraylist storing the checklist data
        return checklistData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView checklist_id_txt, checklistName_txt, checklistDescription_txt;
        FloatingActionButton editChecklistButton;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;
            itemView.setOnClickListener(this);

            // initialize the text views
            checklist_id_txt = (TextView) itemView.findViewById(R.id.checklist_id_txt);
            checklistName_txt = (TextView) itemView.findViewById(R.id.checklistName_txt);
            checklistDescription_txt = (TextView) itemView.findViewById(R.id.checklistDescription_txt);

            // edit checklist button
            editChecklistButton = itemView.findViewById(R.id.editChecklistButton);
            editChecklistButton.setOnClickListener(this);

            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {

            // if edit checklist button was clicked
            if(view.getId() == R.id.editChecklistButton) {

                // get the checklist data from the arraylist based on the view position that was clicked
                String cData = checklistData.get(getLayoutPosition());

                // start an explicit intent to the edit checklist activity, sending the corresponding checklist data
                Intent editIntent = new Intent(context, EditChecklistActivity.class);
                editIntent.putExtra("checklistData", cData);
                ((Activity) context).startActivityForResult(editIntent, REQUEST_EDIT);

            } else{
                // if the view was clicked
                // get the checklist data from the arraylist based on the view position
                String cData = checklistData.get(getLayoutPosition());

                // start an explicit intent to the detailed checklist activity, sending the corresponding checklist data
                Intent toDoIntent = new Intent(context, DetailChecklistActivity.class);
                toDoIntent.putExtra("checklistData", cData);
                ((Activity) context).startActivityForResult(toDoIntent, REQUEST_TODO);

            }
        }
    }
}
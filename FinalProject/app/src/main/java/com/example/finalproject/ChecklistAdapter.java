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


        String[] results = (checklistData.get(position).toString()).split(",");

        int checklistNum = position + 1;
        String num = String.valueOf(checklistNum);
        holder.checklist_id_txt.setText(num);
        holder.checklistName_txt.setText(results[1]);
        holder.checklistDescription_txt.setText(results[2]);

    }


    @Override
    public int getItemCount() {
        return checklistData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView checklist_id_txt, checklistName_txt, checklistDescription_txt;
        FloatingActionButton editChecklistButton;
//        private Activity activity;

        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;
            itemView.setOnClickListener(this);

            checklist_id_txt = (TextView) itemView.findViewById(R.id.checklist_id_txt);
            checklistName_txt = (TextView) itemView.findViewById(R.id.checklistName_txt);
            checklistDescription_txt = (TextView) itemView.findViewById(R.id.checklistDescription_txt);
            editChecklistButton = itemView.findViewById(R.id.editChecklistButton);
            editChecklistButton.setOnClickListener(this);

            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {

            if(view.getId() == R.id.editChecklistButton) {

                String cData = checklistData.get(getLayoutPosition());

                Intent editIntent = new Intent(context, EditChecklistActivity.class);
                editIntent.putExtra("checklistData", cData);
                ((Activity) context).startActivityForResult(editIntent, REQUEST_EDIT);

            } else{
                String cData = checklistData.get(getLayoutPosition());
                Intent toDoIntent = new Intent(context, DetailChecklistActivity.class);
                toDoIntent.putExtra("checklistData", cData);

                ((Activity) context).startActivityForResult(toDoIntent, REQUEST_TODO);

            }

        }
    }
}
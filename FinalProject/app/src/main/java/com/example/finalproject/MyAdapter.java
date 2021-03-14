package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;
import static com.example.finalproject.R.layout.activity_checklist_row;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<String> list;
    Context context;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(activity_checklist_row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {

        String[]  results = (list.get(position).toString()).split(",");

        holder.nameTextView.setText(results[0]);
        holder.descriptionTextView.setText(results[1]);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView nameTextView;
        public TextView descriptionTextView;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            nameTextView = (TextView) itemView.findViewById(R.id.checklistNameEditText);
            descriptionTextView = (TextView) itemView.findViewById(R.id.checklistDescriptionEditText);


            itemView.setOnClickListener(this);
            context = itemView.getContext();

        }

      

        @Override
        public void onClick(View view) {
            Toast.makeText(context,
                    "You have clicked " + ((TextView)view.findViewById(R.id.checklistNameEditText)).getText().toString(),
                    Toast.LENGTH_SHORT).show();
//
//            if (view.getId() == R.id.checklistNameEditText) {
//                Intent i = new Intent(view.getContext(), EditDataActivity.class);
//                i.putExtra("id",itemID);
//                i.putExtra("name",name);
//                view.getContext().startActivity(i);
//            }
        }
    }
}
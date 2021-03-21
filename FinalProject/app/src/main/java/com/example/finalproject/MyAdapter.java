package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.content.ContentValues.TAG;
import static com.example.finalproject.R.layout.activity_checklist_row;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<String> list;
    Context context;
    private Activity activity;
    private ArrayList checklist_id, checklistName_id, checklistDescription_id;

    public MyAdapter(Activity activity, Context context,ArrayList checklist_id, ArrayList checklistName_id,
                     ArrayList checklistDescription_id
                    ){
        this.activity = activity;
        this.context = context;
        this.checklist_id = checklist_id;
        this.checklistName_id = checklistName_id;
        this.checklistDescription_id = checklistDescription_id;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(activity_checklist_row, parent, false);
        return new MyViewHolder(view);


    }
//    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(activity_checklist_row, parent, false);
//        MyViewHolder viewHolder = new MyViewHolder(v);
//        return viewHolder;
//    }

    @RequiresApi(api = Build.VERSION_CODES.M)

    @Override
    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
        holder.checklist_id_txt.setText(String.valueOf(checklist_id.get(position)));
        holder.checklistName_txt.setText(String.valueOf(checklistName_id.get(position)));
        holder.checklistDescription_txt.setText(String.valueOf(checklistDescription_id.get(position)));


        //Recyclerview onClickListener
        holder.myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditChecklistActivity.class);
                intent.putExtra("id", String.valueOf(checklist_id.get(position)));
                intent.putExtra("name", String.valueOf(checklistName_id.get(position)));
                intent.putExtra("description", String.valueOf(checklistDescription_id.get(position)));

                activity.startActivityForResult(intent, 1);
            }
        });

        // holder.gotoChecklist_button.findViewById(R.id.goToChecklistButton).setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View v) {
        //         Intent intent1 = new Intent(context, DetailChecklistActivity.class);
        //         activity.startActivityForResult(intent1, 1);

        //     }
        // });

    }

//    @Override
//    public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
//
//        String[]  results = (list.get(position).toString()).split(",");
//
//        holder.nameTextView.setText(results[0]);
//        holder.descriptionTextView.setText(results[1]);
//
//    }


    @Override
    public int getItemCount() {
        return checklist_id.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView checklist_id_txt;
        public TextView checklistName_txt;
        public TextView checklistDescription_txt;
        FloatingActionButton gotoChecklist_button;
        private Activity activity;


        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
//            myLayout = (LinearLayout) itemView;
            checklist_id_txt = itemView.findViewById(R.id.checklist_id_txt);
            checklistName_txt = itemView.findViewById(R.id.checklistName_txt);
            checklistDescription_txt = itemView.findViewById(R.id.checklistDescription_txt);
     //       gotoChecklist_button = itemView.findViewById(R.id.goToChecklistButton);



            myLayout = itemView.findViewById(R.id.listLayout);



//            nameTextView  = (TextView) itemView.findViewById(R.id.checklistNameEditText);
//            descriptionTextView = (TextView) itemView.findViewById(R.id.checklistDescriptionEditText);

//
//            itemView.setOnClickListener(this);
//            context = itemView.getContext();
        }



//        @Override
//        public void onClick(View view) {
//            Toast.makeText(context,
//                    "You have clicked " + ((TextView)view.findViewById(R.id.checklistNameEditText)).getText().toString(),
//                    Toast.LENGTH_SHORT).show();
////
////            if (view.getId() == R.id.checklistNameEditText) {
////                Intent i = new Intent(view.getContext(), EditDataActivity.class);
////                i.putExtra("id",itemID);
////                i.putExtra("name",name);
////                view.getContext().startActivity(i);
////            }
//        }
    }
}
package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ChecklistActivity extends Activity implements View.OnClickListener {

    // Recycler View
    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager myLayoutManager;
    //    private RecyclerView.Adapter adapter;
    MyAdapter myAdapter;


    MyDatabase db;

    public static final String DEFAULT = "not available";

    private String firstName;

    private TextView activityHeader;
    private Button addNewChecklistButton;
    private Button editChecklistButton;

    public EditText checklistNameEditText, checklistDescriptionEditText, checklistItemEditText;


    MyHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        checklistNameEditText = (EditText) findViewById(R.id.checklistNameEditText);
        checklistDescriptionEditText = (EditText) findViewById(R.id.checklistNameEditText);


        // Create recycler view
        myRecycler = (RecyclerView) findViewById(R.id.table);
        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);

        db = new MyDatabase(this);
        helper = new MyHelper(this);

        Cursor cursor = db.getData();

        int index1 = cursor.getColumnIndex(Constants.CLNAME);
        int index2 = cursor.getColumnIndex(Constants.CLDESCRIPTION);

        ArrayList<String> mArrayList = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String clName = cursor.getString(index1);
            String clDescription = cursor.getString(index2);
            String s = clName + "," + clDescription;
            mArrayList.add(s);
            cursor.moveToNext();
        }

        if(mArrayList.size() > 1){
          Toast.makeText(this, "value 1: " + mArrayList.get(0) + " value 2: " + mArrayList.get(1), Toast.LENGTH_SHORT).show();
        } else if (mArrayList.size() > 0){
          Toast.makeText(this, "value 1: " + mArrayList.get(0), Toast.LENGTH_SHORT).show();
        }

        myAdapter = new MyAdapter(mArrayList);
        myRecycler.setAdapter(myAdapter);

        activityHeader = (TextView) findViewById(R.id.activityHeader);
        activityHeader.setOnClickListener(this);

        addNewChecklistButton = (Button) findViewById(R.id.addNewChecklistButton);
        addNewChecklistButton.setOnClickListener(this);

        editChecklistButton = (Button) findViewById(R.id.editButton);
        getUserInfo();
    //    EditData();

    }

//    public void EditData(){
//        editChecklistButton.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        boolean isUpdate = db.updateData(checklistNameEditText.getText().toString(), checklistDescriptionEditText.getText().toString());
//                    }
//                }
//        );
//
//    }

    @Override
    public void onClick(View view) {
        // if add new checklist button was clicked
        if (view.getId() == R.id.addNewChecklistButton) {
            Intent i = new Intent(view.getContext(), NewChecklistActivity.class);
            view.getContext().startActivity(i);
        }
//
//        if (view.getId() == R.id.editButton) {
//            Intent i = new Intent(view.getContext(), NewChecklistActivity.class);
//            view.getContext().startActivity(i);
//        }
    }

    private void getUserInfo() {
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);

        activityHeader.setText(firstName + "'s Checklists");
    }
}
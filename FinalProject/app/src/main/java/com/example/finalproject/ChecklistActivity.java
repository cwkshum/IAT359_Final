package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChecklistActivity extends Activity implements View.OnClickListener {

    // Recycler View
    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter adapter;

    MyDatabase db;

    public static final String DEFAULT = "not available";

    private String firstName;

    private TextView activityHeader;
    private Button addNewChecklistButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        // Create recycler view
        myRecycler = (RecyclerView) findViewById(R.id.table);
        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);

        db = new MyDatabase(this);

        activityHeader = (TextView) findViewById(R.id.activityHeader);
        activityHeader.setOnClickListener(this);

        addNewChecklistButton = (Button) findViewById(R.id.addNewChecklistButton);
        addNewChecklistButton.setOnClickListener(this);

        getUserInfo();

    }

    @Override
    public void onClick(View view) {
        // if add new checklist button was clicked
        if(view.getId() == R.id.addNewChecklistButton) {
            Intent i = new Intent(view.getContext(), NewChecklistActivity.class);
            view.getContext().startActivity(i);
        }
    }

    private void getUserInfo(){
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);

        activityHeader.setText(firstName + "'s Checklists");
    }
}
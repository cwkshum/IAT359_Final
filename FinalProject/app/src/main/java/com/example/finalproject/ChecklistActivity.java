package com.example.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ChecklistActivity extends AppCompatActivity implements View.OnClickListener {

    // Recycler View
    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager myLayoutManager;
    private RecyclerView.Adapter adapter;
    private ChecklistAdapter myAdapter;

    private ChecklistDatabase db;

    public static final String DEFAULT = "not available";

    private String firstName, username;

    private TextView activityHeader;

    FloatingActionButton add_button;

    private Button mapNavigation, resourcesNavigation, accountNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

        accountNavigation = (Button) findViewById(R.id.accountNavigation);
        accountNavigation.setOnClickListener(this);

        // Create recycler view
        myRecycler = (RecyclerView) findViewById(R.id.table);
        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);

        activityHeader = (TextView) findViewById(R.id.activityHeader);

        add_button = findViewById(R.id.addNewChecklistButton);
        add_button.setOnClickListener(this);

        getUserInfo();

        db = new ChecklistDatabase(this);

        Cursor cursor = db.getChecklistData();

        int index1 = cursor.getColumnIndex(Constants.USERNAME);
        int index2 = cursor.getColumnIndex(Constants.CHECKLISTNAME);
        int index3 = cursor.getColumnIndex(Constants.DESCRIPTION);

        ArrayList<String> ChecklistData = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String username = cursor.getString(index1);
            String checklistName = cursor.getString(index2);
            String checklistDecription = cursor.getString(index3);
            String s = username + "," + checklistName + "," + checklistDecription;
            ChecklistData.add(s);
            cursor.moveToNext();
        }

        myAdapter = new ChecklistAdapter(ChecklistData, this);
        myRecycler.setAdapter(myAdapter);

    }

    @Override
    public void onClick(View view) {
        // if add new checklist button was clicked
        if(view.getId() == R.id.addNewChecklistButton) {
            Intent i = new Intent(view.getContext(), NewChecklistActivity.class);
            startActivityForResult(i, 2);
        }
        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to create route activity
            Intent i = new Intent(view.getContext(), AccountActivity.class);
            view.getContext().startActivity(i);
        }


        // If resources was clicked in the bottom nav
        if (view.getId() == R.id.resourcesNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), ResourcesActivity.class);
            view.getContext().startActivity(i);
        }

        if (view.getId() == R.id.mapNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), LandingActivity.class);
            view.getContext().startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            recreate();
        }
    }

    private void getUserInfo(){
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);
        username = sharedPrefs.getString("username", DEFAULT);

        activityHeader.setText(firstName + "'s Checklists");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All?");
        builder.setMessage("Are you sure you want to delete all checklists?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                db.deleteAllData(username);
                //Refresh Activity
                Intent intent = new Intent(ChecklistActivity.this, ChecklistActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}
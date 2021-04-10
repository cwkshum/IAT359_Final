package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    private ChecklistAdapter myAdapter;

    private ChecklistDatabase db;

    public static final String DEFAULT = "not available";

    private String firstName, username;

    private TextView activityHeader;

    private FloatingActionButton add_button;

    private Button mapNavigation, resourcesNavigation, accountNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        // Navigation Buttons
        // Map Button
        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        // Resources Button
        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

        // Account Button
        accountNavigation = (Button) findViewById(R.id.accountNavigation);
        accountNavigation.setOnClickListener(this);

        // Create recycler view
        myRecycler = (RecyclerView) findViewById(R.id.table);
        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);

        // Checklist Activity Title
        activityHeader = (TextView) findViewById(R.id.activityHeader);

        // Add Checklist Button
        add_button = findViewById(R.id.addNewChecklistButton);
        add_button.setOnClickListener(this);

        // get the user's information
        getUserInfo();

        // access the database
        db = new ChecklistDatabase(this);

        // get the data from the checklist table from the db that matches the user's username
        Cursor cursor = db.getChecklistData(username);

        int index1 = cursor.getColumnIndex(Constants.USERNAME);
        int index2 = cursor.getColumnIndex(Constants.CHECKLISTNAME);
        int index3 = cursor.getColumnIndex(Constants.DESCRIPTION);

        ArrayList<String> ChecklistData = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // get the username
            String username = cursor.getString(index1);

            // get the checklist name
            String checklistName = cursor.getString(index2);

            // get the checklist description
            String checklistDescription = cursor.getString(index3);

            // insert the retrieved data into the arraylist
            String s = username + "," + checklistName + "," + checklistDescription;
            ChecklistData.add(s);
            cursor.moveToNext();
        }

        // send the checklist data to the adapter to be attached to the recyclerview
        myAdapter = new ChecklistAdapter(ChecklistData, this);
        myRecycler.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View view) {

        // if add new checklist button was clicked
        if(view.getId() == R.id.addNewChecklistButton) {
            // start an explicit intent to the New Checklist Activity
            Intent i = new Intent(view.getContext(), NewChecklistActivity.class);
            startActivityForResult(i, 2);
        }

        // if map was clicked in the bottom navigation
        if (view.getId() == R.id.mapNavigation) {
            // start explicit intent to go to map activity
            Intent i = new Intent(view.getContext(), LandingActivity.class);
            view.getContext().startActivity(i);
        }

        // If resources was clicked in the bottom navigation
        if (view.getId() == R.id.resourcesNavigation) {
            // start explicit intent to go to resources activity
            Intent i = new Intent(view.getContext(), ResourcesActivity.class);
            view.getContext().startActivity(i);
        }

        // if account was clicked in the bottom navigation
        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to account activity
            Intent i = new Intent(view.getContext(), AccountActivity.class);
            view.getContext().startActivity(i);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2){
            // refresh the page to show the newly created checklist
            recreate();
        }
    }

    private void getUserInfo(){
        // get the user's first name and username
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);
        username = sharedPrefs.getString("username", DEFAULT);

        // set the activity title
        activityHeader.setText(firstName + "'s Checklists");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create top additional menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete_all){
            // call the confirm method to delete all checklists
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        // create an alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // ask user if they want to delete all checklists
        builder.setTitle(Html.fromHtml("<font color='#303030'>Delete All?</font>"));
        // builder.setTitle("Delete All?");
        //  builder.setMessage("Are you sure you want to delete all checklists?");
        builder.setMessage(Html.fromHtml("<font color='#303030'>Are you sure you want to delete all checklists?</font>"));


        // if the user selects "Yes"
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // delete the checklists from the database
                db.deleteAllData(username);

                // Refresh Activity show that the checklists have been cleared
                Intent intent = new Intent(ChecklistActivity.this, ChecklistActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // if the user selects "No", do not delete checklists from database
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // show alert
        builder.create().show();
    }
}
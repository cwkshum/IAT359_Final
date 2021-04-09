package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NewChecklistActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText checklistNameEditText, checklistDescriptionEditText;
    private Button createChecklist;

    public static final String DEFAULT = "not available";
    ChecklistDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchecklist);

        checklistNameEditText = (EditText) findViewById(R.id.checklistNameEditText);
        checklistDescriptionEditText = (EditText) findViewById(R.id.checklistDescriptionEditText);

        // create checklist button
        createChecklist = (Button) findViewById(R.id.createChecklist);
        createChecklist.setOnClickListener(this);

        // access database
        db = new ChecklistDatabase(this);
    }


    @Override
    public void onClick(View view) {

        // create checklist button
        if(view.getId() == R.id.createChecklist) {

            // get user's username
            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            String username = sharedPrefs.getString("username", DEFAULT);

            // get user input
            String checklistName = checklistNameEditText.getText().toString();
            String checklistDescription = checklistDescriptionEditText.getText().toString();

            // add to database
            if (checklistName.equals("") || checklistDescription.equals("")) {
                // user is missing data, do not enter data into the database
                Toast.makeText(this, "Please enter data for all fields", Toast.LENGTH_SHORT).show();

            } else{
                // insert inputted data into the database
                long id = db.insertCheckListData(username, checklistName, checklistDescription);

                if (id < 0) {
                    // insert failed, do not send user back to Checklist Activity
                    Toast.makeText(this, "Failed to create new checklist. Checklist name already exists.", Toast.LENGTH_SHORT).show();
                }
                else {
                    // new checklist has been inserted successfully into the db
                    // take user back to main checklist activity
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        }
    }
}
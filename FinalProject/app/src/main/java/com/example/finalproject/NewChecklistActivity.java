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

public class NewChecklistActivity extends Activity implements View.OnClickListener {

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

        createChecklist = (Button) findViewById(R.id.createChecklist);
        createChecklist.setOnClickListener(this);

        db = new ChecklistDatabase(this);
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.createChecklist) {

            // get user's username
            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            String username = sharedPrefs.getString("username", DEFAULT);

            // get user input
            String checklistName = checklistNameEditText.getText().toString();
            String checklistDescription = checklistDescriptionEditText.getText().toString();

            // add to database
            if (checklistNameEditText.equals("") || checklistDescriptionEditText.equals("")) {
                // user is missing data, do not enter data into the database
                Toast.makeText(this, "Please enter data for all fields", Toast.LENGTH_SHORT).show();

            } else{
                // insert inputted data into the database
                long id = db.insertCheckListData(username, checklistName, checklistDescription);

                if (id < 0) {
                    Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                }

                // go back to main checklist activity
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }

            // reset the text fields
            checklistNameEditText.setText("");
            checklistDescriptionEditText.setText("");
        }
    }
}
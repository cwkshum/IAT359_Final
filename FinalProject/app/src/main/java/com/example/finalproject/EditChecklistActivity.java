package com.example.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class EditChecklistActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText newChecklistNameEditText, newChecklistDescriptionEditText;

    private Button updateButton, deleteButton;

    private String username, checklistName, description, newChecklistName, newChecklistDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editchecklist);

        newChecklistNameEditText = (EditText) findViewById(R.id.newChecklistNameEditText);
        newChecklistDescriptionEditText = (EditText) findViewById(R.id.newChecklistDescriptionEditText);

        // update checklist button
        updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this);

        // delete checklist button
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        if(getIntent().hasExtra("checklistData")){
            // get checklist data from the intent
            String checklistData = getIntent().getExtras().getString("checklistData");

            // separate the data
            String[] results = (checklistData.split(","));
            username = results[0];
            checklistName = results[1];
            description = results[2];

            // set the new checklist name input text to be the current checklist name
            newChecklistNameEditText.setText(checklistName);

            // set the new checklist description input text to be the current checklist description
            newChecklistDescriptionEditText.setText(description);
        }
    }

    public void confirmDialog(){
        // create an alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // ask user if they want to delete the checklist
        builder.setTitle("Delete " + checklistName + " ?");
        builder.setMessage("Are you sure you want to delete " + checklistName + " ?");

        // if user selects "Yes"
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // delete the checklist from the database
                ChecklistDatabase db = new ChecklistDatabase(getApplicationContext());
                db.deleteChecklist(username, checklistName);

                // finish the intent and send user back to the Checklist Activity
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

        // if the user selects "No"
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // close the dialog
            }
        });

        // show the alert
        builder.create().show();
    }

    @Override
    public void onClick(View view) {

        // update button
        if(view.getId() == R.id.updateButton){
            ChecklistDatabase db = new ChecklistDatabase(getApplicationContext());

            newChecklistName = newChecklistNameEditText.getText().toString();
            newChecklistDescription = newChecklistDescriptionEditText.getText().toString();

            // if no values are entered, then do not update the database
            if (newChecklistName.equals("") || newChecklistDescription.equals("")) {
                Toast.makeText(this, "Please Enter Data for All Fields", Toast.LENGTH_SHORT).show();
            } else {
                if(db.updateChecklistData(username, checklistName, newChecklistName, newChecklistDescription)) {
                    // finish the intent if the checklist was updated successfully and return user to the checklist activity
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    // updating the db failed
                    Toast.makeText(this, "Failed to update, checklist name already exists", Toast.LENGTH_SHORT).show();
                }
            }

        }

        // delete button
        if(view.getId() == R.id.deleteButton){
            // show confirm dialog
            confirmDialog();
        }
    }
}
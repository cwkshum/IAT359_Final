package com.example.finalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
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

        updateButton = (Button) findViewById(R.id.updateButton);
        updateButton.setOnClickListener(this);

        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(this);

        //First we call this
//        getAndSetIntentData();

        if(getIntent().hasExtra("checklistData")){
            String checklistData = getIntent().getExtras().getString("checklistData");
            String[] results = (checklistData.split(","));
            username = results[0];
            checklistName = results[1];
            description = results[2];

            newChecklistNameEditText.setText(checklistName);
            newChecklistDescriptionEditText.setText(description);
        }

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(checklistName);
        }

//        update_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //And only then we call this
//                MyHelper myDB = new MyHelper(EditChecklistActivity.this);
//                name = checklistNameEditText.getText().toString().trim();
//                description = checklistDescriptionEditText.getText().toString().trim();
//                myDB.updateData(id, name, description);
//            }
//        });
//        delete_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                confirmDialog();
//            }
//        });

    }

//    void getAndSetIntentData(){
//        if(getIntent().hasExtra("id") && getIntent().hasExtra("name") &&
//                getIntent().hasExtra("description")){
//            //Getting Data from Intent
//            id = getIntent().getStringExtra("id");
//            name = getIntent().getStringExtra("name");
//            description = getIntent().getStringExtra("description");
//
//            //Setting Intent Data
//            checklistNameEditText.setText(name);
//            checklistDescriptionEditText.setText(description);
//
//            Log.d("stev", name+" "+description);
//        }else{
//            Toast.makeText(this, "No data.", Toast.LENGTH_SHORT).show();
//        }
//    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete " + checklistName + " ?");
        builder.setMessage("Are you sure you want to delete " + checklistName + " ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ChecklistDatabase db = new ChecklistDatabase(getApplicationContext());
                db.deleteChecklist(username, checklistName);
                setResult(Activity.RESULT_OK);
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

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.updateButton){
            ChecklistDatabase db = new ChecklistDatabase(getApplicationContext());

            newChecklistName = newChecklistNameEditText.getText().toString();
            newChecklistDescription = newChecklistDescriptionEditText.getText().toString();

            // if no start or end point has been entered, the activity is canceled
            if (newChecklistName.equals("") || newChecklistDescription.equals("")) {
                Toast.makeText(this, "Please Enter Data for All Fields", Toast.LENGTH_SHORT).show();
            } else {
                if(db.updateChecklistData(username, checklistName, newChecklistName, newChecklistDescription)) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(this, "Failed to update, checklist name already exists", Toast.LENGTH_SHORT).show();
                }
            }

        }

        if(view.getId() == R.id.deleteButton){
            confirmDialog();
        }
    }
}
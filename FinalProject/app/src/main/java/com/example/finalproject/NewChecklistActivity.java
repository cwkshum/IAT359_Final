package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;

public class NewChecklistActivity extends AppCompatActivity {

    EditText checklistNameEditText, checklistDescriptionEditText, checklistItemEditText;
    Button createChecklist;

  //  MyHelper helper;
 //   public static final String DEFAULT = "not available";
   //  MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newchecklist);
//
//        checklistNameEditText = (EditText) findViewById(R.id.checklistNameEditText);
//        checklistDescriptionEditText = (EditText) findViewById(R.id.checklistNameEditText);

        checklistNameEditText = findViewById(R.id.checklistNameEditText);
        checklistDescriptionEditText = findViewById(R.id.checklistNameEditText);
        createChecklist = findViewById(R.id.createChecklist);
        createChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyHelper db = new MyHelper(NewChecklistActivity.this);
                db.addList(checklistNameEditText.getText().toString().trim(),
                        checklistDescriptionEditText.getText().toString().trim());
            }
        });

//        createChecklist = (Button) findViewById(R.id.createChecklist);
//        createChecklist.setOnClickListener(this);

//        db = new MyDatabase(this);
//        helper = new MyHelper(this);
//
//        Cursor cursor = db.getData();
//
//        int index1 = cursor.getColumnIndex(Constants.CLNAME);
//        int index2 = cursor.getColumnIndex(Constants.CLDESCRIPTION);
//
//        ArrayList<String> mArrayList = new ArrayList<String>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            String clName = cursor.getString(index1);
//            String clDescription = cursor.getString(index2);
//            String s = clName + "," + clDescription;
//            mArrayList.add(s);
//            cursor.moveToNext();
//        }


    }

//    public void addList (View view)
//    {
//        String name = checklistNameEditText.getText().toString();
//        String description = checklistDescriptionEditText.getText().toString();
//
//
//        Toast.makeText(this, name + description, Toast.LENGTH_SHORT).show();
//        long id = db.insertData(name, description);
//        if (id < 0){
//            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent();
//            setResult(RESULT_OK, i);
//            finish();
//        }
//        checklistNameEditText.setText("");
//        checklistDescriptionEditText.setText("");
//
//    }



//    public void editList (View view)
//    {
//        String name = checklistNameEditText.getText().toString();
//        String description = checklistDescriptionEditText.getText().toString();
//
//
//        Toast.makeText(this, name + description, Toast.LENGTH_SHORT).show();
//        long id = db.insertData(name, description);
//        if (id < 0){
//            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
//            Intent i = new Intent();
//            setResult(RESULT_OK, i);
//            finish();
//        }
////        checklistNameEditText.setText("");
////        checklistDescriptionEditText.setText("");
//
//    }


//    public void viewLists(View view)
//    {
//        Intent intent = new Intent(this, ChecklistActivity.class);
//        startActivity(intent);
//    }

//    @Override
//    public void onClick(View view) {
        // if(view.getId() == R.id.createChecklist) {
            
        //     // get user's username
        //     SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        //     String username = sharedPrefs.getString("username", DEFAULT);
            
        //     // get user input
        //     String checklistName = checklistNameEditText.getText().toString();
        //     String checklistDescription = checklistDescriptionEditText.getText().toString();
        //     String item = checklistItemEditText.getText().toString();
            
        //     // add to database
        //     // if (checklistNameEditText.equals("") || checklistDescriptionEditText.equals("") || checklistItemEditText.equals("")) {
        //     //     // user is missing data, do not enter data into the database
        //     //     Toast.makeText(this, "Please enter data for all fields", Toast.LENGTH_SHORT).show();

        //     // } else{
        //     //     // insert inputted data into the database
        //     //     long id = db.insertData(username, checklistName, checklistDescription, item);

        //     //     if (id < 0) {
        //     //         Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        //     //     }
        //     //     else {
        //     //         Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        //     //     }

        //     //     // go back to main checklist activity
        //     //     Intent i = new Intent();
        //     //     setResult(RESULT_OK, i);
        //     //     finish();
        //     // }

        //     // reset the text fields
        //     checklistNameEditText.setText("");
        //     checklistDescriptionEditText.setText("");
        //     checklistItemEditText.setText("");
        // }
  //  }
}

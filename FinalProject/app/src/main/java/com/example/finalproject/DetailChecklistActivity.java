package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DetailChecklistActivity extends AppCompatActivity implements View.OnClickListener, DialogCloseListener{

    // Recycler View
    private RecyclerView tasksRecyclerView;
//    private RecyclerView.LayoutManager myLayoutManager;
    private ToDoAdapter tasksAdapter;

    private ChecklistDatabase db;

    private FloatingActionButton fab;

    private String username, checklistName, description;

    private TextView checklistNameHeading, checklistDescriptionHeading;
    private Button addToDoItemButton;
    private EditText toDoEditText;
    private String newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailchecklist);

        // Create recycler view
        tasksRecyclerView = (RecyclerView) findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        checklistNameHeading = (TextView) findViewById(R.id.checklistNameHeading);
        checklistDescriptionHeading = (TextView) findViewById(R.id.checklistDescriptionHeading);

        toDoEditText = (EditText) findViewById(R.id.toDoEditText);

        addToDoItemButton = (Button) findViewById(R.id.addToDoItemButton);
        addToDoItemButton.setOnClickListener(this);


//        fab = findViewById(R.id.fab);
//        fab.setOnClickListener(this);

        if(getIntent().hasExtra("checklistData")){
            String checklistData = getIntent().getExtras().getString("checklistData");
            String[] results = (checklistData.split(","));
            username = results[0];
            checklistName = results[1];
            description = results[2];

            checklistNameHeading.setText(checklistName);
            checklistDescriptionHeading.setText(description);

        }

        db = new ChecklistDatabase(this);

        Cursor cursor = db.getToDoData(username, checklistName);

        int index1 = cursor.getColumnIndex(Constants.TODO);
        int index2 = cursor.getColumnIndex(Constants.STATUS);

        ArrayList<String> toDoData = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String username = cursor.getString(index1);
            Integer status = cursor.getInt(index2);
            String s = username + "," + status + ",";
            toDoData.add(s);
            cursor.moveToNext();
        }

        tasksAdapter = new ToDoAdapter(toDoData, username, checklistName, this, DetailChecklistActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.addToDoItemButton) {
//            AddNewTask.newInstance().show(getSupportFragmentManager(), AddNewTask.TAG);
            newItem = toDoEditText.getText().toString();

            if(newItem.equals("")){
                Toast.makeText(this, "Please Enter an Item to add", Toast.LENGTH_SHORT).show();
            } else{
                long id = db.insertToDoData(username, checklistName, newItem, 0);

                if (id < 0) {
                    Toast.makeText(this, "Failed to create new item. Item name already exists.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

                    // clear the input text field
                    toDoEditText.setText("");

                    // reloads the activity
                    finish();
                    startActivity(getIntent());
                }
            }

        }
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        // reloads the activity
        finish();
        startActivity(getIntent());
    }
}
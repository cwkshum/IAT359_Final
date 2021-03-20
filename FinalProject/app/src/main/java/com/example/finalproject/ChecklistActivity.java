package com.example.finalproject;

import android.app.Activity;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ChecklistActivity extends AppCompatActivity {

//    // Recycler View
//    private RecyclerView myRecycler;
//    private RecyclerView.LayoutManager myLayoutManager;
//    //    private RecyclerView.Adapter adapter;
//    MyAdapter myAdapter;
//
//
//    MyDatabase db;
//
    public static final String DEFAULT = "not available";
//
    private String firstName;
//
    private TextView activityHeader;
//    private Button addNewChecklistButton;
//    private Button editChecklistButton;
//
//    public EditText checklistNameEditText, checklistDescriptionEditText, checklistItemEditText;
//
//
//    MyHelper helper;

    RecyclerView recyclerView;
    FloatingActionButton add_button;

 //   ImageView empty_imageview;
 //   TextView no_data;

    MyHelper db;
    ArrayList<String> checklist_id, checklist_name, checklist_description;
    MyAdapter myAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklist);

        recyclerView = findViewById(R.id.table);
        add_button = findViewById(R.id.addNewChecklistButton);




//        empty_imageview = findViewById(R.id.empty_imageview);
//        no_data = findViewById(R.id.no_data);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChecklistActivity.this, NewChecklistActivity.class);
                startActivity(intent);
//                getUserInfo();

//                Intent intent2 = new Intent(ChecklistActivity.this, DetailChecklistActivity.class);
//                startActivity(intent2);
//                getUserInfo();

            }
        });

//        detail_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2 = new Intent(ChecklistActivity.this, DetailChecklistActivity.class);
//                startActivity(intent2);
////                getUserInfo();
//
//            }
//        });

        db = new MyHelper(ChecklistActivity.this);
        checklist_id= new ArrayList<>();
        checklist_name = new ArrayList<>();
        checklist_description = new ArrayList<>();

        storeDataInArrays();

        myAdapter = new MyAdapter(ChecklistActivity.this,this, checklist_id,
                checklist_name, checklist_description);

        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ChecklistActivity.this));
        
//        activityHeader = (TextView) findViewById(R.id.activityHeader);
//        activityHeader.setOnClickListener(this);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = db.readAllData();
//        if(cursor.getCount() == 0){
//            empty_imageview.setVisibility(View.VISIBLE);
//            no_data.setVisibility(View.VISIBLE);
//        }else{
            while (cursor.moveToNext()){
                checklist_id.add(cursor.getString(0));
                checklist_name.add(cursor.getString(1));
                checklist_description.add(cursor.getString(2));
            }
//            empty_imageview.setVisibility(View.GONE);
//            no_data.setVisibility(View.GONE);
//        }
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
        builder.setMessage("Are you sure you want to delete all Data?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyHelper myDB = new MyHelper(ChecklistActivity.this);
                myDB.deleteAllData();
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
        private void getUserInfo() {
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);

//         activityHeader.setText(firstName + "'s Checklists");
    }
}
//        checklistNameEditText = (EditText) findViewById(R.id.checklistNameEditText);
//        checklistDescriptionEditText = (EditText) findViewById(R.id.checklistNameEditText);
//
//
//        // Create recycler view
//        myRecycler = (RecyclerView) findViewById(R.id.table);
//        myLayoutManager = new LinearLayoutManager(this);
//        myRecycler.setLayoutManager(myLayoutManager);
//
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
//
//        if(mArrayList.size() > 1){
//          Toast.makeText(this, "value 1: " + mArrayList.get(0) + " value 2: " + mArrayList.get(1), Toast.LENGTH_SHORT).show();
//        } else if (mArrayList.size() > 0){
//          Toast.makeText(this, "value 1: " + mArrayList.get(0), Toast.LENGTH_SHORT).show();
//        }
//
//        myAdapter = new MyAdapter(mArrayList);
//        myRecycler.setAdapter(myAdapter);
//
//        activityHeader = (TextView) findViewById(R.id.activityHeader);
//        activityHeader.setOnClickListener(this);
//
//        addNewChecklistButton = (Button) findViewById(R.id.addNewChecklistButton);
//        addNewChecklistButton.setOnClickListener(this);
//
//        editChecklistButton = (Button) findViewById(R.id.editButton);
//        getUserInfo();
//        EditData();


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

//    @Override
//    public void onClick(View view) {
//        // if add new checklist button was clicked
//        if (view.getId() == R.id.addNewChecklistButton) {
//            Intent i = new Intent(view.getContext(), NewChecklistActivity.class);
//            view.getContext().startActivity(i);
//        }
////
////        if (view.getId() == R.id.editButton) {
////            Intent i = new Intent(view.getContext(), NewChecklistActivity.class);
////            view.getContext().startActivity(i);
////        }
//    }
//
//    private void getUserInfo() {
//        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        firstName = sharedPrefs.getString("firstName", DEFAULT);
//
//        activityHeader.setText(firstName + "'s Checklists");
//    }
//}
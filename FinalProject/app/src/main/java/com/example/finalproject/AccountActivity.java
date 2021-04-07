package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    // Recycler View
    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager myLayoutManager;
    private FavouriteRouteAdapter myAdapter;

    private TextView nameHeader, userHeader, emailHeader, favouriteRoutesHeading;

    SwitchCompat switchCompat;

    private String firstName, lastName, username, email;

    private Boolean darkMode;

    private Button checklistNavigation, resourcesNavigation, mapNavigation, light, dark;

    public static final String DEFAULT = "not available";

    private final int EDIT_INFO = 3;
    private ChecklistDatabase db;

    private SwitchCompat cyclingAlerts;
    private Boolean alertPref;


    //private final int EDIT_INFO = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        light = findViewById(R.id.lightMode);
        dark= findViewById(R.id.darkMode);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);

        light.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                         SharedPreferences.Editor editor = sharedPrefs.edit();
                                         editor.putBoolean("darkMode",false);
                                         editor.commit();
                                     }
                                 });

        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("darkMode",true);
                editor.commit();
            }
        });
        nameHeader = (TextView) findViewById(R.id.name);
        userHeader = (TextView) findViewById(R.id.username);
        emailHeader = (TextView) findViewById(R.id.email);
        favouriteRoutesHeading = (TextView) findViewById(R.id.favouriteRoutesHeading);


        // Navigation Buttons
        // Map Navigation
        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        // Checklist Navigation
        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);

        // Resources Navigation
        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

        cyclingAlerts = (SwitchCompat) findViewById(R.id.cyclingAlerts);
        cyclingAlerts.setOnCheckedChangeListener(this);

        // Create recycler view
        myRecycler = (RecyclerView) findViewById(R.id.table);
        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);

        // Method to get user's information
        getUserInfo();

        // access the database
        db = new ChecklistDatabase(this);

        // get the data from the checklist table from the db
        Cursor cursor = db.getFavouriteData(username);

        int index1 = cursor.getColumnIndex(Constants.ROUTENAME);
        int index2 = cursor.getColumnIndex(Constants.ROUTETYPE);

        ArrayList<String> favouriteRoutesData = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // get the route name
            String routeName = cursor.getString(index1);

            // get the bikeway type
            String routeType = cursor.getString(index2);

            // insert the retrieved data into the arraylist
            String s = routeName + "," + routeType;
            favouriteRoutesData.add(s);
            cursor.moveToNext();
        }

        if(favouriteRoutesData.size() < 1){
            favouriteRoutesHeading.setText("No Favourited Routes");
        }

        // send the checklist data to the adapter to be attached to the recyclerview
        myAdapter = new FavouriteRouteAdapter(favouriteRoutesData, username,this);
        myRecycler.setAdapter(myAdapter);




        // switchCompat = (SwitchCompat) findViewById(R.id.switchCompat);
        // switchCompat.setOnCheckedChangeListener(this);


//        switchCompat = findViewById(R.id.switchCompat);

//        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        Boolean booleanvalue = sharedPrefs.getBoolean("darkMode", false);
//        if (booleanvalue) {
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            switchCompat.setChecked(true);
//        }
//        switchCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//               // switchCompat.setChecked(true);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode", true);
//                editor.commit();
//                Toast.makeText(this, "yeet", Toast.LENGTH_SHORT).show();
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
////                switchCompat.setChecked(false);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode", false);
//                Toast.makeText(this, " no yeet", Toast.LENGTH_SHORT).show();
//
//                editor.commit();
//            }
//        });
//    }
    }

//            if(!isChecked){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                switchCompat.setChecked(false);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode",false);
//                editor.commit();
//                Toast.makeText(this, "yeet" , Toast.LENGTH_SHORT).show();
//            }
//            if(darkMode == true && isChecked ){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                switchCompat.setChecked(false);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode",false);
//                editor.commit();
//                Toast.makeText(this, "skrr" , Toast.LENGTH_SHORT).show();
//
//            }
//            if(darkMode == false && isChecked ){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                switchCompat.setChecked(true);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode",true);
//                editor.commit();
//                Toast.makeText(this, "skrr" , Toast.LENGTH_SHORT).show();
//
//            }

//            if(isChecked && darkMode == false){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                switchCompat.setChecked(true);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode",true);
//                editor.commit();
//            }


//
//            if(!isChecked && darkMode == false){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                switchCompat.setChecked(false);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode",false);
//                editor.commit();
//                Toast.makeText(this, "oof" , Toast.LENGTH_SHORT).show();
//
//            }
//            else if(isChecked && darkMode == true){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                switchCompat.setChecked(false);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode",false);
//                Toast.makeText(this, " no yeet" , Toast.LENGTH_SHORT).show();
//                editor.commit();
//
//            }
//            else if(isChecked && darkMode == true){
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                switchCompat.setChecked(false);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode",false);
//                Toast.makeText(this, " no yeet" , Toast.LENGTH_SHORT).show();
//                editor.commit();
//
//            }

//            else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                switchCompat.setChecked(false);
//                SharedPreferences.Editor editor = sharedPrefs.edit();
//                editor.putBoolean("darkMode",false);
//                Toast.makeText(this, " no yeet" , Toast.LENGTH_SHORT).show();
//
//                editor.commit();
//            }
//        });



//        if (darkMode){
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            switchCompat.setChecked(true);
//        } else{
//             AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//            switchCompat.setChecked(false);
//        }

//        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//                if (isChecked){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                    // switchCompat.setChecked(true);
//
//                    if(!darkMode){
//                      SharedPreferences.Editor editor = sharedPrefs.edit();
//                      editor.putBoolean("darkMode", true);
//                      editor.commit();
//                    }
//
//                    getUserInfo();
//                }else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    // switchCompat.setChecked(false);
//
//                    if(darkMode){
//                      SharedPreferences.Editor editor = sharedPrefs.edit();
//                      editor.putBoolean("darkMode",false);
//                      editor.commit();
//                    }
//
//                    getUserInfo();
//
//                }
//            }
//        });




//  @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//    }


    private void getUserInfo(){
        // Get user's information from sharedpreferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);
        lastName = sharedPrefs.getString("lastName", DEFAULT);
        username = sharedPrefs.getString("username", DEFAULT);
        email = sharedPrefs.getString("email", DEFAULT);
        darkMode = sharedPrefs.getBoolean("darkMode",false);
        alertPref = sharedPrefs.getBoolean("cyclingAlerts", true);


        if(alertPref == true){
            cyclingAlerts.setChecked(true);
        } else{
            cyclingAlerts.setChecked(false);
        }

//       if(darkMode == true){
//              switchCompat.setChecked(true);
//          } else{
//              switchCompat.setChecked(false);
//          }


        Toast.makeText(this, "darkMode: " + darkMode, Toast.LENGTH_SHORT).show();


        // First name and last name
        nameHeader.setText(firstName + " " + lastName);

        // Username
        userHeader.setText(username);

        // Email
        emailHeader.setText(email);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // process the response from CreateRoute Activity
        if(requestCode == EDIT_INFO){
            // make sure that the request was successful
            if(resultCode == RESULT_OK){
                recreate();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create top additional menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.account_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.editAccount){
            // start explicit activity to go to edit account activity
            Intent i = new Intent(getApplicationContext(), EditAccountActivity.class);
            startActivityForResult(i, EDIT_INFO);
        }

        if(item.getItemId() == R.id.logout){
            // start explicit activity to go to login activity
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        // Navigation Buttons
        // Map Navigation button clicked in the bottom navigation
        if (view.getId() == R.id.mapNavigation) {
            // start explicit intent to go to map activity
            Intent i = new Intent(view.getContext(), LandingActivity.class);
            view.getContext().startActivity(i);
        }

        // Checklist button clicked in the bottom navigation
        if (view.getId() == R.id.checklistNavigation) {
            // start explicit intent to go to checklist activity
            Intent i = new Intent(view.getContext(), ChecklistActivity.class);
            view.getContext().startActivity(i);
        }

        // Resources button clicked in the bottom navigation
        if (view.getId() == R.id.resourcesNavigation) {
            // start explicit intent to go to resources activity
            Intent i = new Intent(view.getContext(), ResourcesActivity.class);
            view.getContext().startActivity(i);
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(buttonView.getId() == R.id.cyclingAlerts){
            if(isChecked){
                Toast.makeText(this, "Cycling alerts on", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("cyclingAlerts", true);
                editor.commit();


            } else{
                Toast.makeText(this, "Cycling alerts off", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putBoolean("cyclingAlerts", false);
                editor.commit();
            }
        }
    }

}

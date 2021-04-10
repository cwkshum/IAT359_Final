package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    // Recycler View
    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager myLayoutManager;
    private FavouriteRouteAdapter myAdapter;

    private TextView nameHeader, userHeader, emailHeader, favouriteRoutesHeading;

    private String firstName, lastName, username, email;

    private Boolean darkMode, alertPref;

    private Button checklistNavigation, resourcesNavigation, mapNavigation, light, dark;

    public static final String DEFAULT = "not available";

    private final int EDIT_INFO = 3;
    private ChecklistDatabase db;

    // toggle for cycling alerts
    private SwitchCompat cyclingAlerts;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        light = (Button) findViewById(R.id.lightMode);
        light.setOnClickListener(this);

        dark = (Button) findViewById(R.id.darkMode);
        dark.setOnClickListener(this);

        // Text view displays
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

        // Toggle for cycling alerts
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

        // get the favourite routes data from the favourite table from the db
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

        if (favouriteRoutesData.size() < 1) {
            // no favourite routes found in the database
            favouriteRoutesHeading.setText("No Favourite Routes Saved");
        }

        // send the favourite routes data to the adapter to be attached to the recyclerview
        myAdapter = new FavouriteRouteAdapter(favouriteRoutesData, username, this);
        myRecycler.setAdapter(myAdapter);
    }


    private void getUserInfo(){
        // Get user's information from sharedpreferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);
        lastName = sharedPrefs.getString("lastName", DEFAULT);
        username = sharedPrefs.getString("username", DEFAULT);
        email = sharedPrefs.getString("email", DEFAULT);
        darkMode = sharedPrefs.getBoolean("darkMode",false);
        alertPref = sharedPrefs.getBoolean("cyclingAlerts", true);

        setDarkModeButtonState();

        if(alertPref == true){
            // set cycling alert toggle to true
            cyclingAlerts.setChecked(true);
        } else{
            // set cycling alert toggle to false
            cyclingAlerts.setChecked(false);
        }

        // First name and last name
        nameHeader.setText(firstName + " " + lastName);

        // Username
        userHeader.setText("@" + username);

        // Email
        emailHeader.setText(email);

    }

    private void setDarkModeButtonState(){
        if(darkMode){
            // show that the dark mode button is enabled
            Drawable moon = getResources().getDrawable(R.drawable.moon).mutate();
            moon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            dark.setCompoundDrawablesWithIntrinsicBounds(moon, null, null, null);
            dark.setTextColor(getResources().getColor(R.color.white));
            dark.setBackgroundResource(R.drawable.primary_button);

            // show that the light mode button is inactive
            Drawable sun = getResources().getDrawable(R.drawable.sun_notactive).mutate();
            sun.setColorFilter(getResources().getColor(R.color.accent_green), PorterDuff.Mode.SRC_ATOP);
            light.setCompoundDrawablesWithIntrinsicBounds(sun, null, null, null);
            light.setTextColor(getResources().getColor(R.color.accent_green));
            light.setBackgroundResource(R.drawable.secondary_button);

        } else{
            // show that the light mode button is enabled
            Drawable sun = getResources().getDrawable(R.drawable.sun).mutate();
            sun.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            light.setCompoundDrawablesWithIntrinsicBounds(sun, null, null, null);
            light.setTextColor(getResources().getColor(R.color.white));
            light.setBackgroundResource(R.drawable.primary_button);

            // show that the dark mode button is inactive
            Drawable moon = getResources().getDrawable(R.drawable.darkmode_notactive).mutate();
            moon.setColorFilter(getResources().getColor(R.color.accent_green), PorterDuff.Mode.SRC_ATOP);
            dark.setCompoundDrawablesWithIntrinsicBounds(moon, null, null, null);
            dark.setTextColor(getResources().getColor(R.color.accent_green));
            dark.setBackgroundResource(R.drawable.secondary_button);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // process the response from EditAccount Activity
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

        if(view.getId() == R.id.lightMode){
            // update shared preferences with current app state
            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("darkMode", false);
            editor.commit();

            setDarkModeButtonState();

            // Set application to light mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        if(view.getId() == R.id.darkMode){

            // update shared preferences with current app state
            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("darkMode", true);
            editor.commit();

            setDarkModeButtonState();

            // Set application to dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }


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
                // update shared preferences with current toggle state
                SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                // cycling alerts are active
                editor.putBoolean("cyclingAlerts", true);
                editor.commit();


            } else{
                // update shared preferences with current toggle state
                SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                // cycling alerts are inactive
                editor.putBoolean("cyclingAlerts", false);
                editor.commit();
            }
        }
    }

}
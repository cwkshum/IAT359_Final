package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AccountActivity extends Activity implements View.OnClickListener {
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private TextView nameHeader, userHeader, emailHeader;

    private String firstName, username, email;

    private Button checklistNavigation, resourcesNavigation, mapNavigation, edit_button, logout;

    public static final String DEFAULT = "not available";

    private final int EDIT_INFO = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        nameHeader = (TextView) findViewById(R.id.name);
        userHeader = (TextView) findViewById(R.id.username);
        emailHeader = (TextView) findViewById(R.id.email);

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


        // Edit information button
        edit_button = (Button) findViewById(R.id.editinfo);
        edit_button.setOnClickListener(this);

        // Logout button
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(this);

        // Placeholder method for favourite routes to be implemented in milestone 3
        initImageBitmaps();

        // Method to get user's information
        getUserInfo();

    }

    private void getUserInfo(){
        // Get user's information from sharedpreferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);
        username = sharedPrefs.getString("username", DEFAULT);
        email = sharedPrefs.getString("email", DEFAULT);

        // First name
        nameHeader.setText(firstName);

        // Username
        userHeader.setText(username);

        // Email
        emailHeader.setText(email);

    }

    // Placeholder RecyclerView for Favourite Routes to be implemented for Milestone 3
    private void initImageBitmaps(){
        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");


        initRecyclerView();
    }

    // Placeholder RecyclerView for Favourite Routes to be implemented for Milestone 3
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FavouriteRouteAdapter adapter = new FavouriteRouteAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
    public void onClick(View view) {

        // Edit information button
        if(view.getId() == R.id.editinfo) {
            // start explicit activity to go to edit account activity
            Intent i = new Intent(view.getContext(), EditAccountActivity.class);
            startActivityForResult(i, EDIT_INFO);
        }

        // Logout button
        if(view.getId() == R.id.logout) {
            // start explicit activity to go to login activity
            Intent i = new Intent(view.getContext(), MainActivity.class);
            startActivity(i);
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
}

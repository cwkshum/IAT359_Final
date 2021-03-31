package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private TextView nameHeader, userHeader, emailHeader;

    private String firstName, lastName, username, email;

    private Button checklistNavigation, resourcesNavigation, mapNavigation;

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

        // Placeholder method for favourite routes to be implemented in milestone 3
        initImageBitmaps();

        // Method to get user's information
        getUserInfo();

    }

    private void getUserInfo(){
        // Get user's information from sharedpreferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);
        lastName = sharedPrefs.getString("lastName", DEFAULT);
        username = sharedPrefs.getString("username", DEFAULT);
        email = sharedPrefs.getString("email", DEFAULT);

        // First name and last name
        nameHeader.setText(firstName + " " + lastName);

        // Username
        userHeader.setText(username);

        // Email
        emailHeader.setText(email);

    }

    // Placeholder RecyclerView for Favourite Routes to be implemented for Milestone 3
    private void initImageBitmaps(){
        mImageUrls.add("https://cdn.shopify.com/s/files/1/0019/7073/3109/files/seawall_1024x1024.jpg?v=1528253110");
        mNames.add("Stanley Park");
        mDescription.add("Scenic Route that takes bikers across Stanley Park where they can view the beautiful Stanley Park Bike ways");

        mImageUrls.add("https://liv.rent/blog/wp-content/uploads/2019/06/2018-07-25-10.32.28-1-compressor.jpg");
        mNames.add("Sunset Beach");
        mDescription.add("Ride Along the shores of Sunset Beach! This bike route is a great start for first time riders with an amazing view of BC's Coast.");


        mImageUrls.add("https://www.letsgobiking.net/wp-content/uploads/2010/03/IMG_1660.jpg");
        mNames.add("Queen Elizabeth Park");
        mDescription.add("This route follows five Vancouver Greenways and is the highest spot in the city Making  it great for experienced bikers");



        initRecyclerView();
    }

    // Placeholder RecyclerView for Favourite Routes to be implemented for Milestone 3
    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FavouriteRouteAdapter adapter = new FavouriteRouteAdapter(this, mNames, mImageUrls, mDescription);
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

//        // Edit information button
//        if(view.getId() == R.id.editinfo) {
//            // start explicit activity to go to edit account activity
//            Intent i = new Intent(view.getContext(), EditAccountActivity.class);
//            startActivityForResult(i, EDIT_INFO);
//        }
//
//        // Logout button
//        if(view.getId() == R.id.logout) {
//            // start explicit activity to go to login activity
//            Intent i = new Intent(view.getContext(), MainActivity.class);
//            startActivity(i);
//        }

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

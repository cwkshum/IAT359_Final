package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PopularRoutesActivity extends AppCompatActivity implements View.OnClickListener{

    // Recycler View
    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager myLayoutManager;
    private PopularRoutesAdapter myAdapter;

    private EditText searchInput;

    private TextView popularRoutesHeading;

    private Button searchButton, mapNavigation, checklistNavigation, resourcesNavigation, accountNavigation;
    private Button allBikewaysFilterButton, localStreetFilterButton, paintedLanesFilterButton, protectedLanesFilterButton, sharedLanesFilterButton;
    private String userSearchInput, filterSelection;
    private Boolean filterBikeway = false;

    private BikewaysDatabaseAdapter mDbHelper;
    private ArrayList<String> popularRoutesInfoArrayList;
    public static final String ROUTE_POINTS = "ROUTE_POINTS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularroutes);

        if(getIntent().hasExtra("fromFavourite") && getIntent().hasExtra("popularRouteData")){
            String popularRouteData = getIntent().getExtras().getString("popularRouteData");
            if((popularRouteData != null)){
                // if the user came from the favourite route in the account activity, redirect them to the detailed view activity
                Intent popularRouteDetail = new Intent(this, PopularRouteDetailActivity.class);
                popularRouteDetail.putExtra("popularRouteData", popularRouteData);
                startActivityForResult(popularRouteDetail, PopularRoutesAdapter.REQUEST_POPULARROUTEDETAIL);
            } else{
                Toast.makeText(this, "There was an error starting route. Please try again.", Toast.LENGTH_SHORT).show();
            }
        }

        // Create recycler view
        myRecycler = (RecyclerView) findViewById(R.id.table);
        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);

        searchInput = (EditText) findViewById(R.id.searchInput);

        popularRoutesHeading = (TextView) findViewById(R.id.popularRoutesHeading);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        // access the bikeways database
        mDbHelper = new BikewaysDatabaseAdapter(this);
        mDbHelper.createDatabase();

        // bottom navigation buttons
        // map navigation button
        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        // checklist navigation button
        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);

        // resource navigation button
        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

        // account navigation button
        accountNavigation = (Button) findViewById(R.id.accountNavigation);
        accountNavigation.setOnClickListener(this);


        // filter buttons
        allBikewaysFilterButton = (Button) findViewById(R.id.allBikewaysFilterButton);
        allBikewaysFilterButton.setOnClickListener(this);

        localStreetFilterButton = (Button) findViewById(R.id.localStreetFilterButton);
        localStreetFilterButton.setOnClickListener(this);

        paintedLanesFilterButton = (Button) findViewById(R.id.paintedLanesFilterButton);
        paintedLanesFilterButton.setOnClickListener(this);

        protectedLanesFilterButton = (Button) findViewById(R.id.protectedLanesFilterButton);
        protectedLanesFilterButton.setOnClickListener(this);

        sharedLanesFilterButton = (Button) findViewById(R.id.sharedLanesFilterButton);
        sharedLanesFilterButton.setOnClickListener(this);

        // open the bikeway database
        mDbHelper.open();

        // get the route data from the db
        Cursor cursor = mDbHelper.getPopularRoutesData("");

        int index1 = cursor.getColumnIndex(Constants.POPULARNAME);
        int index2 = cursor.getColumnIndex(Constants.POPULARTYPE);
        int index3 = cursor.getColumnIndex(Constants.POPULARLENGTH);
        int index4 = cursor.getColumnIndex(Constants.POPULARCOORDINATES);

        popularRoutesInfoArrayList = new ArrayList<String>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // get the name of the popular route
            String name = cursor.getString(index1);

            // get the popular route type
            String type = cursor.getString(index2);

            // get the popular route length
            String length = cursor.getString(index3);

            // get the popular routes coordinates
            String coordinates = cursor.getString(index4);

            // insert the retrieved data into the arraylist
            String s = name + "&" + type + "&" + length + "&" + coordinates;
            popularRoutesInfoArrayList.add(s);
            cursor.moveToNext();
        }

        // close bikeways database
        mDbHelper.close();

        // set Adapter
        myAdapter = new PopularRoutesAdapter(popularRoutesInfoArrayList, getApplicationContext());
        myRecycler.setAdapter(myAdapter);

    }

    // Find a popular route using AsyncTask
    public class FindPopularRoute extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            // open the bikeway database
            mDbHelper.open();

            Cursor popularRoutesData;

            if(filterBikeway) {
                popularRoutesData = mDbHelper.popularRoutesFilterData(userSearchInput, filterSelection);
            } else {
                popularRoutesData = mDbHelper.getPopularRoutesData(userSearchInput);
            }

            int index1 = popularRoutesData.getColumnIndex(Constants.POPULARNAME);
            int index2 = popularRoutesData.getColumnIndex(Constants.POPULARTYPE);
            int index3 = popularRoutesData.getColumnIndex(Constants.POPULARLENGTH);
            int index4 = popularRoutesData.getColumnIndex(Constants.POPULARCOORDINATES);

            popularRoutesInfoArrayList = new ArrayList<String>();
            popularRoutesData.moveToFirst();
            while (!popularRoutesData.isAfterLast()) {
                // get the name of the popular route
                String name = popularRoutesData.getString(index1);

                // get the popular route type
                String type = popularRoutesData.getString(index2);

                // get the popular route length
                String length = popularRoutesData.getString(index3);

                // get the popular routes coordinates
                String coordinates = popularRoutesData.getString(index4);

                // insert the retrieved data into the arraylist
                String s = name + "&" + type + "&" + length + "&" + coordinates;
                popularRoutesInfoArrayList.add(s);
                popularRoutesData.moveToNext();
            }

            // close db
            mDbHelper.close();

            if(popularRoutesInfoArrayList.size() <= 0){
                // no landmarks were found in the db
                return "No Results Found";
            }

            // landmarks were found in the db
            return "Popular Routes Results";
        }

        @Override
        protected void onPostExecute(String results) { // called when doInBackground() is done
            super.onPostExecute(results);

            if(results == "No Results Found"){
                // show that no routes were found in the display
                popularRoutesHeading.setText(results);
                myAdapter = new PopularRoutesAdapter(popularRoutesInfoArrayList, getApplicationContext());
                myRecycler.setAdapter(myAdapter);

            } else{
                // show that that there are available routes in the display
                popularRoutesHeading.setText(results);
                myAdapter = new PopularRoutesAdapter(popularRoutesInfoArrayList, getApplicationContext());
                myRecycler.setAdapter(myAdapter);
            }


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // process the response from PopularRouteDetail Activity
        if(requestCode == PopularRoutesAdapter.REQUEST_POPULARROUTEDETAIL){
            // make sure that the request was successful
            if(resultCode == RESULT_OK){
                // make sure that the returned data has route points passed through
                if(data.hasExtra(PopularRouteDetailActivity.ROUTE_POINTS)){
                    ArrayList<LatLng> routePoints = data.getExtras().getParcelableArrayList(PopularRouteDetailActivity.ROUTE_POINTS);

                    Intent popularRouteIntent = new Intent();
                    // put the route coordinates in the intent
                    popularRouteIntent.putExtra(ROUTE_POINTS, routePoints);

                    // finish the intent and take the stored route information back to the map activity
                    setResult(Activity.RESULT_OK, popularRouteIntent);
                    finish();

                }
            }
        } else{
            Toast.makeText(this, "Points Not Received", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void hideSoftKeyboard(View v) {
        // hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void changeFilterButtons(int filterType){

        // set all filter buttons to the unselected state
        allBikewaysFilterButton.setTextColor(getResources().getColor(R.color.accent_green));
        allBikewaysFilterButton.setBackgroundResource(R.drawable.secondary_button);

        localStreetFilterButton.setTextColor(getResources().getColor(R.color.accent_green));
        localStreetFilterButton.setBackgroundResource(R.drawable.secondary_button);

        paintedLanesFilterButton.setTextColor(getResources().getColor(R.color.accent_green));
        paintedLanesFilterButton.setBackgroundResource(R.drawable.secondary_button);

        protectedLanesFilterButton.setTextColor(getResources().getColor(R.color.accent_green));
        protectedLanesFilterButton.setBackgroundResource(R.drawable.secondary_button);

        sharedLanesFilterButton.setTextColor(getResources().getColor(R.color.accent_green));
        sharedLanesFilterButton.setBackgroundResource(R.drawable.secondary_button);

        if(filterType == 1){
            // set the all bikeways filter button to be selected
            allBikewaysFilterButton.setTextColor(getResources().getColor(R.color.white));
            allBikewaysFilterButton.setBackgroundResource(R.drawable.primary_button);

        } else if(filterType == 2){
            // set the local street filter button to be selected
            localStreetFilterButton.setTextColor(getResources().getColor(R.color.white));
            localStreetFilterButton.setBackgroundResource(R.drawable.primary_button);

        } else if (filterType == 3){
            // set the painted lanes filter button to be selected
            paintedLanesFilterButton.setTextColor(getResources().getColor(R.color.white));
            paintedLanesFilterButton.setBackgroundResource(R.drawable.primary_button);

        } else if (filterType == 4){
            // set the protected lanes filter button to be selected
            protectedLanesFilterButton.setTextColor(getResources().getColor(R.color.white));
            protectedLanesFilterButton.setBackgroundResource(R.drawable.primary_button);

        } else if (filterType == 5){
            // set the shared lanes filter button to be selected
            sharedLanesFilterButton.setTextColor(getResources().getColor(R.color.white));
            sharedLanesFilterButton.setBackgroundResource(R.drawable.primary_button);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create top additional menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_subactivity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.clearSearch){
            // clear the search input
            searchInput.setText("");

            // rest the search input value
            userSearchInput = "";

            // change to all bikeways active
            filterBikeway = false;
            changeFilterButtons(1);
            filterSelection = "";

            // find popular route using AsyncTask
            PopularRoutesActivity.FindPopularRoute findPopularRoute = new PopularRoutesActivity.FindPopularRoute();
            findPopularRoute.execute();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.searchButton || view.getId() == R.id.allBikewaysFilterButton){
            // Get the search input entered by the user
            userSearchInput = searchInput.getText().toString();

            // hide keyboard
            hideSoftKeyboard(view);

            // change to all bikeways active
            filterBikeway = false;
            changeFilterButtons(1);
            filterSelection = "";

            // find popular route using AsyncTask
            PopularRoutesActivity.FindPopularRoute findPopularRoute = new PopularRoutesActivity.FindPopularRoute();
            findPopularRoute.execute();
        }

        if(view.getId() == R.id.localStreetFilterButton){

            // Get the end point entered by the user
            userSearchInput = searchInput.getText().toString();

            // change to local street filter active
            filterBikeway = true;
            changeFilterButtons(2);
            filterSelection = "Local Street";

            // find popular route using AsyncTask
            PopularRoutesActivity.FindPopularRoute findPopularRoute = new PopularRoutesActivity.FindPopularRoute();
            findPopularRoute.execute();

        }

        if(view.getId() == R.id.paintedLanesFilterButton){
            // Get the end point entered by the user
            userSearchInput = searchInput.getText().toString();

            // change to painted lanes filter active
            filterBikeway = true;
            changeFilterButtons(3);
            filterSelection = "Painted Lanes";

            // find popular route using AsyncTask
            PopularRoutesActivity.FindPopularRoute findPopularRoute = new PopularRoutesActivity.FindPopularRoute();
            findPopularRoute.execute();
        }

        if(view.getId() == R.id.protectedLanesFilterButton){
            // Get the end point entered by the user
            userSearchInput = searchInput.getText().toString();

            // change to protected bike lanes filter active
            filterBikeway = true;
            changeFilterButtons(4);
            filterSelection = "Protected Bike Lanes";

            // find popular route using AsyncTask
            PopularRoutesActivity.FindPopularRoute findPopularRoute = new PopularRoutesActivity.FindPopularRoute();
            findPopularRoute.execute();
        }

        if(view.getId() == R.id.sharedLanesFilterButton){
            // Get the end point entered by the user
            userSearchInput = searchInput.getText().toString();

            // change to shared lanes filter active
            filterBikeway = true;
            changeFilterButtons(5);
            filterSelection = "Shared Lanes";

            // find popular route using AsyncTask
            PopularRoutesActivity.FindPopularRoute findPopularRoute = new PopularRoutesActivity.FindPopularRoute();
            findPopularRoute.execute();
        }

        // Bottom Navigation
        // If Map was clicked in the bottom navigation
        if (view.getId() == R.id.mapNavigation) {
            // finish the explicit intent and take the user back to the map activity
            Intent mapIntent = new Intent();
            setResult(RESULT_OK, mapIntent);
            finish();
        }

        // If checklist was clicked in the bottom navigation
        if (view.getId() == R.id.checklistNavigation) {
            // start explicit intent to go to create route activity
            Intent i = new Intent(view.getContext(), ChecklistActivity.class);
            view.getContext().startActivity(i);
        }

        // If resources was clicked in the bottom navigation
        if (view.getId() == R.id.resourcesNavigation) {
            // start explicit intent to go to resources activity
            Intent i = new Intent(view.getContext(), ResourcesActivity.class);
            view.getContext().startActivity(i);
        }

        // If account was clicked in the bottom navigation
        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to account activity
            Intent i = new Intent(view.getContext(), AccountActivity.class);
            view.getContext().startActivity(i);
        }
    }
}
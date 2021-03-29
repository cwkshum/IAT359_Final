package com.example.finalproject;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class LandmarksActivity extends Activity implements View.OnClickListener{

    // Recycler View
    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager myLayoutManager;
    private LandmarksAdapter myAdapter;

    private EditText searchInput;

    private TextView landmarksHeading, createRouteNavigation, popularRoutesNavigation;

    private Button searchButton, mapNavigation, checklistNavigation, resourcesNavigation, accountNavigation;

    private BikewaysDatabaseAdapter mDbHelper;
    private ArrayList<String> landmarkInfoArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmarks);

        // Create recycler view
        myRecycler = (RecyclerView) findViewById(R.id.table);
        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);

        searchInput = (EditText) findViewById(R.id.searchInput);

        landmarksHeading = (TextView) findViewById(R.id.landmarksHeading);

        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(this);

        // access the bikeways database
        mDbHelper = new BikewaysDatabaseAdapter(this);
        mDbHelper.createDatabase();

        // top navigation
        // landmarks navigation
        createRouteNavigation = (TextView) findViewById(R.id.createRouteNavigation);
        createRouteNavigation.setOnClickListener(this);

        // popular routes navigation
        popularRoutesNavigation = (TextView) findViewById(R.id.popularRoutesNavigation);
        popularRoutesNavigation.setOnClickListener(this);


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


        // set Adapter
        myAdapter = new LandmarksAdapter(landmarkInfoArrayList, getApplicationContext());
        myRecycler.setAdapter(myAdapter);

    }

    // Find a landmark using AsyncTask
    public class FindLandmark extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            // open the bikeway database
            mDbHelper.open();

            String whereStatement = " ";
            Cursor landmarksData = mDbHelper.getBikewaysData(whereStatement);

            int index1 = landmarksData.getColumnIndex("Bikeway Type");

            landmarkInfoArrayList = new ArrayList<String>();
            landmarksData.moveToFirst();
            while (!landmarksData.isAfterLast()) {
                // add the landmark data retrieved from the db into the arraylist
                String bikewayType = landmarksData.getString(index1);
                landmarkInfoArrayList.add(bikewayType);
                landmarksData.moveToNext();
            }

            mDbHelper.close();

            if(landmarkInfoArrayList.size() <= 0){
                // no routes were found in the db
                return "No Results Found";
            }

            // routes were found in the db
            return "Landmark Results";
        }

        @Override
        protected void onPostExecute(String results) { // called when doInBackground() is done
            super.onPostExecute(results);

            if(results == "No Results Found"){

                // show that no routes were found in the display
                landmarksHeading.setText(results);
                myAdapter = new LandmarksAdapter(landmarkInfoArrayList, getApplicationContext());
                myRecycler.setAdapter(myAdapter);

            } else{
                // show that that there are available routes in the display
                landmarksHeading.setText(results);
                myAdapter = new LandmarksAdapter(landmarkInfoArrayList, getApplicationContext());
                myRecycler.setAdapter(myAdapter);
            }


        }
    }


    @Override
    public void onClick(View v) {

    }
}

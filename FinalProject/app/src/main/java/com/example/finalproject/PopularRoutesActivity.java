package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
    private String userSearchInput;

    private BikewaysDatabaseAdapter mDbHelper;
    private ArrayList<String> popularRoutesInfoArrayList;
    public static final String LANDMARK_POINT = "LANDMARK_POINT";
    public static final String ROUTE_POINTS = "ROUTE_POINTS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularroutes);

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

        // open the bikeway database
        mDbHelper.open();

        // get the data from the checklist table from the db
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

        mDbHelper.close();

        // set Adapter
        myAdapter = new PopularRoutesAdapter(popularRoutesInfoArrayList, getApplicationContext());
        myRecycler.setAdapter(myAdapter);

    }

    // Find a landmark using AsyncTask
//    public class FindLandmark extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            // open the bikeway database
//            mDbHelper.open();
//
//            Cursor landmarksData = mDbHelper.getLandmarksData(userSearchInput);
//
//            int index1 = landmarksData.getColumnIndex(Constants.LANDMARKSNAME);
//            int index2 = landmarksData.getColumnIndex(Constants.LANDMARKSADDRESS);
//            int index3 = landmarksData.getColumnIndex(Constants.LANDMARKSCATEGORY);
//            int index4 = landmarksData.getColumnIndex(Constants.LANDMARKSINFO);
//
//            landmarkInfoArrayList = new ArrayList<String>();
//            landmarksData.moveToFirst();
//            while (!landmarksData.isAfterLast()) {
//                // get the name of landmark
//                String name = landmarksData.getString(index1);
//
//                // get the landmark address
//                String address = landmarksData.getString(index2);
//
//                // get the landmark category
//                String category = landmarksData.getString(index3);
//
//                // get the landmark info
//                String info = landmarksData.getString(index4);
//
//                // insert the retrieved data into the arraylist
//                String s = name + "~" + address + "~" + category + "~" + info;
//                landmarkInfoArrayList.add(s);
//                landmarksData.moveToNext();
//            }
//
//            mDbHelper.close();
//
//            if(landmarkInfoArrayList.size() <= 0){
//                // no landmarks were found in the db
//                return "No Results Found";
//            }
//
//            // landmarks were found in the db
//            return "Landmark Results";
//        }
//
//        @Override
//        protected void onPostExecute(String results) { // called when doInBackground() is done
//            super.onPostExecute(results);
//
//            if(results == "No Results Found"){
//                // show that no routes were found in the display
//                landmarksHeading.setText(results);
//                myAdapter = new LandmarksAdapter(landmarkInfoArrayList, getApplicationContext());
//                myRecycler.setAdapter(myAdapter);
//
//            } else{
//                // show that that there are available routes in the display
//                landmarksHeading.setText(results);
//                myAdapter = new LandmarksAdapter(landmarkInfoArrayList, getApplicationContext());
//                myRecycler.setAdapter(myAdapter);
//            }
//
//
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // process the response from CreateRoute Activity
        if(requestCode == PopularRoutesAdapter.REQUEST_POPULARROUTEDETAIL){
            // make sure that the request was successful
            if(resultCode == RESULT_OK){
                // make sure that the returned data has a word passed through
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

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.searchButton){
            // Get the end point entered by the user
            userSearchInput = searchInput.getText().toString();

            hideSoftKeyboard(view);

            // find a landmark using AsyncTask
//            LandmarksActivity.FindLandmark findLandmark = new LandmarksActivity.FindLandmark();
//            findLandmark.execute();
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

        // If create route was clicked in the top navigation
        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to account activity
            Intent i = new Intent(view.getContext(), AccountActivity.class);
            view.getContext().startActivity(i);
        }
    }
}

package com.example.finalproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    GoogleMap myMap;

    private FusedLocationProviderClient fusedLocationClient;

    private TextView landmarksNavigation, createRouteNavigation, popularRoutesNavigation;

    private Button checklistNavigation, resourcesNavigation, accountNavigation;

    private Integer mapView;

    private String landmarkPoint;
    private ArrayList<LatLng> landmarkRoutePoints;

    // coordinates for Vancouver, BC
    private static final double VANCOUVER_LAT = 49.277549, VANCOUVER_LNG = -123.123921;

    static final int REQUEST_POINTS = 0;
    static final int REQUEST_LANDMARK = 1;
    static final int REQUEST_POPULAR = 2;

    PolylineOptions lineOptions = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);

        // Top Navigation
        landmarksNavigation = (TextView) findViewById(R.id.landmarksNavigation);
        landmarksNavigation.setOnClickListener(this);

        createRouteNavigation = (TextView) findViewById(R.id.createRouteNavigation);
        createRouteNavigation.setOnClickListener(this);

        popularRoutesNavigation = (TextView) findViewById(R.id.popularRoutesNavigation);
        popularRoutesNavigation.setOnClickListener(this);

        // Bottom Navigation
        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);

        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

        accountNavigation = (Button) findViewById(R.id.accountNavigation);
        accountNavigation.setOnClickListener(this);

        // Map fragment
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;

        getMapView();
        myMap.setMapType(mapView);

        // check if application has permission to use device location
        checkLocationPermission();

        // set the map starting point to Vancouver, BC
        gotoLocation(VANCOUVER_LAT, VANCOUVER_LNG, 12);

        // set listeners
        myMap.setMyLocationEnabled(true);
        myMap.setOnMyLocationButtonClickListener(this);
        myMap.setOnMyLocationClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // process the response from CreateRoute Activity
        if(requestCode == REQUEST_POINTS){
            // make sure that the request was successful
            if(resultCode == RESULT_OK){
                // make sure that the returned data has a word passed through
                if(data.hasExtra(CreateRouteActivity.START_POINT) && data.hasExtra(CreateRouteActivity.END_POINT) && data.hasExtra(CreateRouteActivity.ROUTE_POINTS)){
                    // get the starting/ending point and route coordinates that were received
                    String startPoint = data.getExtras().getString(CreateRouteActivity.START_POINT);
                    String endPoint = data.getExtras().getString(CreateRouteActivity.END_POINT);
                    ArrayList<LatLng> routePoints = data.getExtras().getParcelableArrayList(CreateRouteActivity.ROUTE_POINTS);

                    if((startPoint != null) && (endPoint != null) && (routePoints != null)){
                        // call method to draw markers and route
                        geolocate(startPoint, endPoint, routePoints);
                    } else{
                        Toast.makeText(this, "There was an error starting route. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } else if (requestCode == REQUEST_LANDMARK){
            // make sure that the request was successful
            if(resultCode == RESULT_OK){
                // make sure that the returned data has a word passed through
                if(data.hasExtra(LandmarksActivity.LANDMARK_POINT) && data.hasExtra(LandmarksActivity.ROUTE_POINTS)){
                    // get the landmark point that was received
                    landmarkPoint = data.getExtras().getString(LandmarksActivity.LANDMARK_POINT);
                    landmarkRoutePoints = new ArrayList<LatLng>();
                    landmarkRoutePoints = data.getExtras().getParcelableArrayList(LandmarksActivity.ROUTE_POINTS);

                    if((landmarkPoint != null) && (landmarkRoutePoints != null)){
                        // find a landmark using AsyncTask
                        LandingActivity.FindLandmark findLandmark = new LandingActivity.FindLandmark();
                        findLandmark.execute();
                    } else{
                        Toast.makeText(this, "There was an error starting route. Please try again.", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } else {
            Toast.makeText(this, "Points Not Received", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void geolocate(String startPoint, String endPoint, ArrayList<LatLng> routePoints) {
        Geocoder myGeocoder = new Geocoder(this);
        // clear the map
        myMap.clear();

        // find locations based on user input
        List<Address> startList = null;
        List<Address> endList = null;
        try {
            startList = myGeocoder.getFromLocationName(startPoint, 1);
            endList = myGeocoder.getFromLocationName(endPoint, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        double startLat = 0, startLng = 0, endLat = 0, endLng = 0;

        if (startList.size() > 0 || startList != null) {
            Address add = startList.get(0);

            // get coordinates of starting point
            startLat = add.getLatitude();
            startLng = add.getLongitude();
            gotoLocation(startLat, startLng, 12);

            // add marker at starting point
            MarkerOptions options = new MarkerOptions()
                    .title("Start: " + startPoint)
                    .position(new LatLng(startLat, startLng));
            myMap.addMarker(options);
        }

        if (endList.size() > 0 || endList != null) {
            Address add = endList.get(0);

            // get coordinates of ending point
            endLat = add.getLatitude();
            endLng = add.getLongitude();

            // add marker at ending point
            MarkerOptions options = new MarkerOptions()
                    .title("End: " + endPoint)
                    .position(new LatLng(endLat, endLng));
            myMap.addMarker(options);
        }


        Toast.makeText(getApplicationContext(), "Coordinates: " + routePoints.get(0) + " " + routePoints.get(1), Toast.LENGTH_SHORT).show();

        lineOptions = new PolylineOptions();
        // Adding all the points in the route to LineOptions
        lineOptions.addAll(routePoints);
        lineOptions.width(6);
        lineOptions.color(getResources().getColor(R.color.accent_blue));

        // Drawing polyline in the Google Map for the selected route
        myMap.addPolyline(lineOptions);
    }

    // Find a landmark using AsyncTask
    public class FindLandmark extends AsyncTask<String, Void, Address> {

        @Override
        protected Address doInBackground(String... params) {

            Geocoder myGeocoder = new Geocoder(getApplicationContext());

            // find locations based on user input
            List<Address> landmarkList = null;
            try {
                landmarkList = myGeocoder.getFromLocationName(landmarkPoint, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (landmarkList.size() > 0 || landmarkList != null) {
                Address add = landmarkList.get(0);
                return add;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Address results) { // called when doInBackground() is done
            super.onPostExecute(results);

            // clear the map
            myMap.clear();

            double landmarkLat = 0, landmarkLng = 0;
            if (!results.equals(null)) {
                // get coordinates of starting point
                landmarkLat = results.getLatitude();
                landmarkLng = results.getLongitude();
                gotoLocation(landmarkLat, landmarkLng, 12);

                // add marker at starting point
                MarkerOptions options = new MarkerOptions()
                        .title(landmarkPoint)
                        .position(new LatLng(landmarkLat, landmarkLng));
                myMap.addMarker(options);

                lineOptions = new PolylineOptions();
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(landmarkRoutePoints);
                lineOptions.width(6);
                lineOptions.color(getResources().getColor(R.color.accent_blue));

                // Drawing polyline in the Google Map for the selected route
                myMap.addPolyline(lineOptions);
            } else{
                Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        // go to the location of the starting point
        LatLng latlng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, zoom);
        myMap.moveCamera(update);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        // Current location marker was clicked
        Geocoder myGeocoder = new Geocoder(this);

        // Display current address
        try {
            List<Address> myLocation = myGeocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Toast.makeText(this, "Current Location: " + myLocation.get(0).getAddressLine(0), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Your Current Location", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    public boolean checkLocationPermission() {
        // check if we have permission to use the user's current location
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("loc1")
                        .setMessage("loc2")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LandingActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                    }

                } else {
                    // permission denied - Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create top additional menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.mapTypeHybrid){
            // change map type
            myMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            saveData(4);
        }

        if(item.getItemId() == R.id.mapTypeTerrain){
            // change map type
            myMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            saveData(3);
        }

        if(item.getItemId() == R.id.mapTypeSatellite){
            // change map type
            myMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            saveData(2);
        }

        if(item.getItemId() == R.id.mapTypeNormal){
            // change map type
            myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            saveData(1);
        }

        if(item.getItemId() == R.id.clearRoute){
            // call the confirm method
            confirmDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    void confirmDialog(){
        // create an alert
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // ask user if they want to clear route
        builder.setTitle("Delete Route?");
        builder.setMessage("Are you sure you want to delete route on map?");

        // if the user selects "Yes"
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // clear the map
                myMap.clear();

                // Refresh Activity show that the route has been cleared
                Intent intent = new Intent(LandingActivity.this, LandingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // if the user selects "No", do not delete clear route
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // show alert
        builder.create().show();
    }


    public void getMapView(){
        // retrieve data from preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        mapView = sharedPrefs.getInt("mapView", 1);
    }

    public void saveData(Integer mapView){
        // Save user information to preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("mapView", mapView);
        editor.commit();
    }

    @Override
    public void onClick(View view) {

        // Top Navigation
        // If landmarks was clicked in the top navigation
        if (view.getId() == R.id.landmarksNavigation) {
            // start explicit intent to go to landmarks activity
            Intent landmarkRouteIntent = new Intent(view.getContext(), LandmarksActivity.class);
            startActivityForResult(landmarkRouteIntent, REQUEST_LANDMARK);
        }

        // If create route was clicked in the top navigation
        if (view.getId() == R.id.createRouteNavigation) {
            // start explicit intent to go to create route activity
            Intent createRouteIntent = new Intent(view.getContext(), CreateRouteActivity.class);
            startActivityForResult(createRouteIntent, REQUEST_POINTS);
        }

        // If popular routes was clicked in the top navigation
        if (view.getId() == R.id.popularRoutesNavigation) {
            // start explicit intent to go to popular routes activity
            Intent popularRoutesIntent = new Intent(view.getContext(), PopularRoutesActivity.class);
            startActivityForResult(popularRoutesIntent, REQUEST_POPULAR);
        }

        // Bottom Navigation
        // If checklist was clicked in the bottom navigation
        if (view.getId() == R.id.checklistNavigation) {
            // start explicit intent to go to checklist activity
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

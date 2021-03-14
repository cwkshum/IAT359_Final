package com.example.finalproject;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    GoogleMap myMap;

    private TextView landmarksNavigation, createRouteNavigation, popularRoutesNavigation;

    private Button checklistNavigation, resourcesNavigation;

    private static final double
            VANCOUVER_LAT = 49.277549,
            VANCOUVER_LNG = -123.123921;

    static final int REQUEST_POINTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);

        landmarksNavigation = (TextView) findViewById(R.id.landmarksNavigation);
        landmarksNavigation.setOnClickListener(this);

        createRouteNavigation = (TextView) findViewById(R.id.createRouteNavigation);
        createRouteNavigation.setOnClickListener(this);

        popularRoutesNavigation = (TextView) findViewById(R.id.popularRoutesNavigation);
        popularRoutesNavigation.setOnClickListener(this);

        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);

        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        locationEntry = (EditText) findViewById(R.id.locationEditText);
//        latEntry = (EditText) findViewById(R.id.latEditText);
//        lngEntry = (EditText) findViewById(R.id.lngEditText);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        myMap = map;

        checkLocationPermission();

        gotoLocation(VANCOUVER_LAT, VANCOUVER_LNG, 12);

        myMap.setMyLocationEnabled(true);
        myMap.setOnMyLocationButtonClickListener(this);
        myMap.setOnMyLocationClickListener(this);

        // Set listeners for click events.
        myMap.setOnPolylineClickListener(this);
        myMap.setOnPolygonClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        // process the response from CreateRoute Activity
        if(requestCode == REQUEST_POINTS){
            // make sure that the request was successful
            if(resultCode == RESULT_OK){
                // make sure that the returned data has a word passed through
                if(data.hasExtra(CreateRouteActivity.START_POINT) && data.hasExtra(CreateRouteActivity.END_POINT)){
                    // get the starting and ending point that was received
                    Toast.makeText(this, "Points Received ", Toast.LENGTH_SHORT).show();
                    String startPoint = data.getExtras().getString(CreateRouteActivity.START_POINT);
                    String endPoint = data.getExtras().getString(CreateRouteActivity.END_POINT);

                    geolocate(startPoint, endPoint);

                }
            }
        } else{
            Toast.makeText(this, "Points Not Received ", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void geolocate(String startPoint, String endPoint) {
        Geocoder myGeocoder = new Geocoder(this);
        myMap.clear();
        Toast.makeText(this, "Searching for " + startPoint, Toast.LENGTH_SHORT).show();

        List<Address> startList = null;
        List<Address> endList = null;
        try {
            startList = myGeocoder.getFromLocationName(startPoint, 1);
            endList = myGeocoder.getFromLocationName(endPoint, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (startList.size() > 0) {
            Address add = startList.get(0);
            String locality = add.getLocality();
            Toast.makeText(this, "Found " + locality, Toast.LENGTH_SHORT).show();

            double lat = add.getLatitude();
            double lng = add.getLongitude();
            gotoLocation(lat, lng, 12);

            MarkerOptions options = new MarkerOptions()
                    .title(locality)
                    .position(new LatLng(lat, lng));
            myMap.addMarker(options);
        }

        if (endList.size() > 0) {
            Address add = endList.get(0);
            String locality = add.getLocality();
            Toast.makeText(this, "Found " + locality, Toast.LENGTH_SHORT).show();

            double lat = add.getLatitude();
            double lng = add.getLongitude();
//                gotoLocation(lat, lng, 12);

            MarkerOptions options = new MarkerOptions()
                    .title(locality)
                    .position(new LatLng(lat, lng));
            myMap.addMarker(options);

            Polyline polyline1 = myMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(lat, lng),
                            new LatLng(-34.747, 145.592)));

//            myMap.addPolyline(polyline1);
        }



//        }

//        if (v.getId() == R.id.latLngButton) {
//            latString = latEntry.getText().toString();
//            lngString = lngEntry.getText().toString();
//            Toast.makeText(this, "Searching for " + latString + " , " + lngString, Toast.LENGTH_SHORT).show();
//
//            List<Address> list = null;
//            try {
//                list = myGeocoder.getFromLocation(Double.parseDouble(latString), Double.parseDouble(lngString), 1);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (list.size() > 0) {
//                Address add = list.get(0);
//                String locality = add.getLocality();
//                Toast.makeText(this, "Found " + locality, Toast.LENGTH_SHORT).show();
//
//                double lat = add.getLatitude();
//                double lng = add.getLongitude();
//                gotoLocation(lat, lng, 15);
//
//                MarkerOptions options = new MarkerOptions()
//                        .title(locality)
//                        .position(new LatLng(lat, lng));
//                myMap.addMarker(options);
//            }
//        }
    }

    //lan lng and zoom
    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latlng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latlng, zoom);
        myMap.moveCamera(update);
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }



    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    public boolean checkLocationPermission() {
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
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
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
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        //  locationManager.requestLocationUpdates(provider, 400, 1, this);
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
    public void onClick(View view) {
        // If landmarks was clicked in the top nav
        if (view.getId() == R.id.landmarksNavigation) {
            // start explicit intent to go to landmarks activity
            Intent i = new Intent(view.getContext(), LandmarksActivity.class);
            view.getContext().startActivity(i);
        }

        // If create route was clicked in the top nav
        if (view.getId() == R.id.createRouteNavigation) {
            // start explicit intent to go to create route activity
            Intent createRouteIntent = new Intent(view.getContext(), CreateRouteActivity.class);
//            view.getContext().startActivity(i);
            startActivityForResult(createRouteIntent, REQUEST_POINTS);
        }

        // If popular routes was clicked in the top nav
        if (view.getId() == R.id.popularRoutesNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), PopularRoutesActivity.class);
            view.getContext().startActivity(i);
        }

        // If landmarks was clicked in the top nav
        if (view.getId() == R.id.ic_landscape) {
            // start explicit intent to go to landmarks activity
            Intent i = new Intent(view.getContext(), LandmarksActivity.class);
            view.getContext().startActivity(i);
        }

        // If create route was clicked in the top nav
        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to create route activity
            Intent i = new Intent(view.getContext(), CreateRouteActivity.class);
            view.getContext().startActivity(i);
        }

        // If create route was clicked in the bottom nav
        if (view.getId() == R.id.checklistNavigation) {
            // start explicit intent to go to create route activity
            Intent i = new Intent(view.getContext(), ChecklistActivity.class);
            view.getContext().startActivity(i);
        }


        // If resources was clicked in the bottom nav
        if (view.getId() == R.id.resourcesNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), ResourcesActivity.class);
            view.getContext().startActivity(i);
        }
    }

    @Override
    public void onPolygonClick(Polygon polygon) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }
}

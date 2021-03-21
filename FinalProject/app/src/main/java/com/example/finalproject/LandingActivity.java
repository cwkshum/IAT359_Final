package com.example.finalproject;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LandingActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener {

    GoogleMap myMap;

    private FusedLocationProviderClient fusedLocationClient;

    private TextView landmarksNavigation, createRouteNavigation, popularRoutesNavigation;

    private Button checklistNavigation, resourcesNavigation, accountNavigation;

    private static final double VANCOUVER_LAT = 49.277549, VANCOUVER_LNG = -123.123921;

    static final int REQUEST_POINTS = 0;

    private Polyline polyline1;
    PolylineOptions lineOptions = null;

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

        accountNavigation = (Button) findViewById(R.id.accountNavigation);
        accountNavigation.setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
                if(data.hasExtra(CreateRouteActivity.START_POINT) && data.hasExtra(CreateRouteActivity.END_POINT) && data.hasExtra(CreateRouteActivity.ROUTE_POINTS)){
                    // get the starting and ending point that was received
                    String startPoint = data.getExtras().getString(CreateRouteActivity.START_POINT);
                    String endPoint = data.getExtras().getString(CreateRouteActivity.END_POINT);
                    ArrayList<LatLng> routePoints = data.getExtras().getParcelableArrayList(CreateRouteActivity.ROUTE_POINTS);

                    geolocate(startPoint, endPoint, routePoints);

                }
            }
        } else{
            Toast.makeText(this, "Points Not Received ", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void geolocate(String startPoint, String endPoint, ArrayList<LatLng> routePoints) {
        Geocoder myGeocoder = new Geocoder(this);
        myMap.clear();

        List<Address> startList = null;
        List<Address> endList = null;
        try {
            startList = myGeocoder.getFromLocationName(startPoint, 1);
            endList = myGeocoder.getFromLocationName(endPoint, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        double startLat = 0, startLng = 0, endLat = 0, endLng = 0;

        if (startList.size() > 0) {
            Address add = startList.get(0);
            String locality = add.getLocality();
            Toast.makeText(this, "Found " + locality, Toast.LENGTH_SHORT).show();

            startLat = add.getLatitude();
            startLng = add.getLongitude();
            gotoLocation(startLat, startLng, 12);

            MarkerOptions options = new MarkerOptions()
                    .title(locality)
                    .position(new LatLng(startLat, startLng));
            myMap.addMarker(options);
        }

        if (endList.size() > 0) {
            Address add = endList.get(0);
            String locality = add.getLocality();
            Toast.makeText(this, "Found " + locality, Toast.LENGTH_SHORT).show();

            endLat = add.getLatitude();
            endLng = add.getLongitude();

            MarkerOptions options = new MarkerOptions()
                    .title(locality)
                    .position(new LatLng(endLat, endLng));
            myMap.addMarker(options);

        }

//        polyline1 = myMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(startLat, startLng),
//                        new LatLng(endLat, endLng)));


//        polyline1 = myMap.addPolyline(new PolylineOptions()
//                .clickable(true)
//                .add(
//                        new LatLng(49.229454995854425,-123.10581168254667),
//                        new LatLng(49.229004613988, -123.10583034532755),
//                        new LatLng(49.22855286327834, -123.10584772750627),
//                        new LatLng(49.22855283718706, -123.10584773294451)));

        lineOptions = new PolylineOptions();
        // Adding all the points in the route to LineOptions
                lineOptions.addAll(routePoints);
                lineOptions.width(6);
                lineOptions.color(Color.BLUE);

        // Drawing polyline in the Google Map for the i-th route
            myMap.addPolyline(lineOptions);
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

        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), AccountActivity.class);
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

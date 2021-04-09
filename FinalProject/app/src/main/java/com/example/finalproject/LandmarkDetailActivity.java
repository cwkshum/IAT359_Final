package com.example.finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LandmarkDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView landmarksHeading, addressHeading, infoHeading, categoryHeading;
    private ImageView landmarkImage;
    private Button startRouteButton;
    private String name, address, category, info, imageName, currentLatLng;
    public static final String LANDMARK = "LANDMARK";
    public static final String ROUTE_POINTS = "ROUTEPOINTS";
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmarkdetail);

        // Text view displays
        landmarksHeading = (TextView) findViewById(R.id.landmarksHeading);
        addressHeading = (TextView) findViewById(R.id.cityname);
        infoHeading = (TextView) findViewById(R.id.routedescription);
        categoryHeading = (TextView) findViewById(R.id.landmarktype);

        // Image
        landmarkImage = (ImageView) findViewById(R.id.landmarkImage);

        // add start route button
        startRouteButton = (Button) findViewById(R.id.startRouteButton);
        startRouteButton.setOnClickListener(this);

        // get current user's location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location
                        currentLatLng = location.getLatitude() + "," + location.getLongitude();
                        
                    }
                });

        if(getIntent().hasExtra("landmarkData")){
            // get landmark data from the intent
            String landmarkData = getIntent().getExtras().getString("landmarkData");

            // separate the data
            String[] results = (landmarkData.split("~"));
            name = results[0];
            address = results[1];
            category = results[2];
            info = results[3];

            // display the data in text views
            landmarksHeading.setText(name);
            addressHeading.setText(address);
            infoHeading.setText(info);
            categoryHeading.setText(category);

            // remove spaces from the landmark name
            imageName = results[0].replaceAll("\\s+", "_").toLowerCase();

            // set the image
            landmarkImage.setImageResource(getResources().getIdentifier(imageName, "drawable", getPackageName()));

        }

   }

    private class ReadDirectionsJSONDataTask extends AsyncTask<String, Void, String> {

        Exception exception = null;
        String data = "";
        protected String doInBackground(String... urls) {
            try{
                // read the JSON data
                data = readJSONData(urls[0]);
            }catch(IOException e){
                exception = e;
            }
            return data;
        }

        protected void onPostExecute(String result) {
            try {
                // Invokes the thread for parsing the JSON data
                LandmarkDetailActivity.ParserTask parserTask = new LandmarkDetailActivity.ParserTask();
                parserTask.execute(result);

            } catch (Exception e) {
                Log.d("ReadDirectionsJSON", e.getLocalizedMessage());
            }
        }
    }


    // parse the google places in json format using an async task
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>>
        doInBackground(String...jsonData) {
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                // Starts parsing data
                DirectionsJSONParser parser = new DirectionsJSONParser();
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;

            // quit if there are no results
            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        continue;
                    } else if(j==1){ // Get duration from the list
                        continue;
                    }

                    // get coordinates
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    // add coordinates to the arraylist
                    points.add(position);
                }

            }

            Intent landmarkIntent = new Intent();
            name = name + ", Vancouver, BC";
            // put the start point in the intent
            landmarkIntent.putExtra(LANDMARK, name);
            // put the route coordinates in the intent
            landmarkIntent.putExtra(ROUTE_POINTS, points);

            // finish the intent and take the stored route information back to the map activity
            setResult(Activity.RESULT_OK, landmarkIntent);
            finish();
        }
    }

    // get the json data from the url
    @SuppressLint("LongLogTag")
    private String readJSONData(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }



    @Override
    public void onClick(View view) {

        // start route button clicked
        if(view.getId() == R.id.startRouteButton) {

            if(!currentLatLng.equals(null)) {

                // request route through an AsyncTask
                LandmarkDetailActivity.ReadDirectionsJSONDataTask requestRoute = new LandmarkDetailActivity.ReadDirectionsJSONDataTask();

                // send the address to retrieve directions based on current location and landmark point
                requestRoute.execute(
                        "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                                currentLatLng + "&destination=" +
                                imageName + ",Vancouver,BC&mode=bicycling&key=AIzaSyCqydQlLuSgi0durCPpfKQLfLslEiefgis");
            } else{
                Toast.makeText(this, "There was an error getting your current location, please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
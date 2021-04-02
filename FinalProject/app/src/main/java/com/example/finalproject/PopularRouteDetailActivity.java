package com.example.finalproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.hardware.Sensor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class PopularRouteDetailActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    private TextView landmarksHeading, addressHeading, infoHeading, categoryHeading;
    private ImageView landmarkImage;
    private Button startRouteButton;
    private String name, type, length, imageName, startPoint, endPoint;
    public static final String LANDMARK = "LANDMARK";
    public static final String ROUTE_POINTS = "ROUTEPOINTS";
//    private ArrayList <LatLng> routePoints = new ArrayList<>();

    String[] users = { "Suresh Dasari", "Trishika Dasari", "Rohini Alavala", "Praveen Kumar", "Madhav Sai" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmarkdetail);

        landmarksHeading = (TextView) findViewById(R.id.landmarksHeading);
        addressHeading = (TextView) findViewById(R.id.cityname);
        infoHeading = (TextView) findViewById(R.id.routedescription);
        categoryHeading = (TextView) findViewById(R.id.landmarktype);

        landmarkImage = (ImageView) findViewById(R.id.landmarkImage);

        // add start route button
        startRouteButton = (Button) findViewById(R.id.startRouteButton);
        startRouteButton.setOnClickListener(this);


        Spinner spin = (Spinner) findViewById(R.id.bikewayTypeSelection);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, users);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);





        if(getIntent().hasExtra("popularRouteData")){
            // get popular route data from the intent
            String popularRouteData = getIntent().getExtras().getString("popularRouteData");

            // separate the data
            String[] results = (popularRouteData.split("&"));
            name = results[0];
            type = results[1];
            length = results[2];
            String[] coordinates = (results[3].split("~"));
            startPoint = coordinates[0];
            endPoint = coordinates[1];


//            for(int i = 0; i < coordinates.length; i++){
//                String [] latlng = (coordinates[i].split(","));
//                double lat = Double.parseDouble(latlng[0]);
//                double lng = Double.parseDouble(latlng[1]);
//                routePoints.add(new LatLng(lat, lng));
//            }

//            Toast.makeText(getApplicationContext(), "points: " + routePoints.get(0), Toast.LENGTH_LONG).show();

            landmarksHeading.setText(name);
            addressHeading.setText(length);
            infoHeading.setText(type);
//            categoryHeading.setText(info);

//            // remove spaces from the landmark name
//            imageName = results[0].replaceAll("\\s+", "_").toLowerCase();
//
//            // set the image
//            landmarkImage.setImageResource(getResources().getIdentifier(imageName, "drawable", getPackageName()));

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
                PopularRouteDetailActivity.ParserTask parserTask = new PopularRouteDetailActivity.ParserTask();
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

            // quite if there are no results
            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
                return;
            }

            // store arraylist of coordinates in an arraylist
//            ArrayList<ArrayList<LatLng>> routePointsArrayList =  new ArrayList<>();

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

                // add arraylist of the current route's coordinates into the arraylist
//                routePointsArrayList.add(points);

            }

            Intent popularRouteIntent = new Intent();
            // put the route coordinates in the intent
            popularRouteIntent.putExtra(ROUTE_POINTS, points);

            // finish the intent and take the stored route information back to the map activity
            setResult(Activity.RESULT_OK, popularRouteIntent);
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

        // add start route button
        if(view.getId() == R.id.startRouteButton) {

            if(!startPoint.equals(null) && !endPoint.equals(null)) {
//                Intent popularRouteIntent = new Intent();
//                // put the route coordinates in the intent
//                popularRouteIntent.putExtra(ROUTE_POINTS, routePoints);
//                // finish the intent and take the stored route information back to the map activity
//                setResult(Activity.RESULT_OK, popularRouteIntent);
//                finish();

                // request route through an AsyncTask
                PopularRouteDetailActivity.ReadDirectionsJSONDataTask requestRoute = new PopularRouteDetailActivity.ReadDirectionsJSONDataTask();

                // send the address to retrieve directions based on start and end point
                requestRoute.execute(
                        "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                                startPoint + "&destination=" +
                                endPoint + "&mode=bicycling&key=AIzaSyCqydQlLuSgi0durCPpfKQLfLslEiefgis");



            } else{
                Toast.makeText(this, "There was an error getting the route information, please try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

}
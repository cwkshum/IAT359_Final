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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.DrawableCompat;
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

public class PopularRouteDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView popularRouteHeading, bikewayType, routeLength;
    private ImageView popularRouteImage;
    private Button startRouteButton, favouriteRouteButton;
    private String name, type, imageName, startPoint, endPoint, username;
    private Float length;
    public static final String ROUTE_POINTS = "ROUTEPOINTS";

    private static final String DEFAULT = "not available";
    private ChecklistDatabase db;
    private Boolean inFavourite = false;
    private Boolean fromFavourite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popularroutedetail);

        // access database
        db = new ChecklistDatabase(this);

        popularRouteHeading = (TextView) findViewById(R.id.popularRouteHeading);
        bikewayType = (TextView) findViewById(R.id.bikewayType);
        routeLength = (TextView) findViewById(R.id.routeLength);

        popularRouteImage = (ImageView) findViewById(R.id.popularRouteImage);

        // add start route button
        startRouteButton = (Button) findViewById(R.id.startRouteButton);
        startRouteButton.setOnClickListener(this);

        // add start route button
        favouriteRouteButton = (Button) findViewById(R.id.favouriteRouteButton);
        favouriteRouteButton.setOnClickListener(this);

        // get the user's username
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        username = sharedPrefs.getString("username", DEFAULT);

        if(getIntent().hasExtra("popularRouteData")){
            // get popular route data from the intent
            String popularRouteData = getIntent().getExtras().getString("popularRouteData");

            // separate the data
            String[] results = (popularRouteData.split("&"));
            name = results[0];
            type = results[1];
            length = Float.parseFloat(results[2])/1000;
            String[] coordinates = (results[3].split("~"));
            startPoint = coordinates[0];
            endPoint = coordinates[1];


//            for(int i = 0; i < coordinates.length; i++){
//                String [] latlng = (coordinates[i].split(","));
//                double lat = Double.parseDouble(latlng[0]);
//                double lng = Double.parseDouble(latlng[1]);
//                routePoints.add(new LatLng(lat, lng));
//            }



            popularRouteHeading.setText(name);
            bikewayType.setText(type);
            routeLength.setText(length.toString());

            // remove spaces from the landmark name
            imageName = results[1].replaceAll("\\s+", "_").toLowerCase();

            // set the image
            popularRouteImage.setImageResource(getResources().getIdentifier(imageName, "drawable", getPackageName()));

        }

        if(getIntent().hasExtra("fromFavourite")){
            fromFavourite = true;
        }



        if(db.checkFavouriteData(username, name, type)){
            // Match found, set button to be favourited
            inFavourite = true;
            setFavouriteButtonState();
        } else{
            // No match found, set button to be not favourited
            inFavourite = false;
            setFavouriteButtonState();

        }

    }

    private void setFavouriteButtonState(){
        if(inFavourite){
            Drawable img = this.getResources().getDrawable(R.drawable.ic_favorite);
            favouriteRouteButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);

        } else{
            Drawable img = this.getResources().getDrawable(R.drawable.ic_outline_favorite);
            favouriteRouteButton.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
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

            if(fromFavourite){
                Intent popularRouteIntent = new Intent(getApplicationContext(), LandingActivity.class);
                popularRouteIntent.putExtra("routePoints", points);
                popularRouteIntent.putExtra("fromFavourite", true);
                startActivity(popularRouteIntent);

            } else {
                Intent popularRouteIntent = new Intent();
                // put the route coordinates in the intent
                popularRouteIntent.putExtra(ROUTE_POINTS, points);

                // finish the intent and take the stored route information back to the map activity
                setResult(Activity.RESULT_OK, popularRouteIntent);
                finish();
            }
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

        if(view.getId() == R.id.favouriteRouteButton){
            if(inFavourite){
                // remove
                db.deleteFavourite(username, name, type);
                inFavourite = false;
                setFavouriteButtonState();
            } else{
                // add

                // insert inputted data into the database
                long id = db.insertFavouriteData(username, name, type);

                if (id < 0) {
                    // insert failed, do not send user back to Checklist Activity
                    Toast.makeText(this, "Failed to save to favourites.", Toast.LENGTH_SHORT).show();
                }
                else {
                    inFavourite = true;
                    setFavouriteButtonState();
                }
            }
        }
    }

}
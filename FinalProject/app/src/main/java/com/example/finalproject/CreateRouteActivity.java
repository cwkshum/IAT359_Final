package com.example.finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreateRouteActivity extends Activity implements View.OnClickListener {

    // Recycler View
    private RecyclerView myRecycler;
    private RecyclerView.LayoutManager myLayoutManager;
    AvailableRoutesAdapter myAdapter;

    private EditText startPointInput, endPointInput;
    private TextView availableRoutesHeading;
    private Button findRouteButton;
    private String startPoint, endPoint;

    private TextView landmarksNavigation, popularRoutesNavigation;

    private Button mapNavigation, checklistNavigation, resourcesNavigation, accountNavigation;

    public static final String START_POINT = "STARTPOINT";
    public static final String END_POINT = "ENDPOINT";
    public static final String ROUTE_POINTS = "ROUTEPOINTS";


    private static String DB_NAME ="bikeways";

    private BikewaysDatabaseAdapter mDbHelper;
    private ArrayList<String> bikewayTypeArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroute);

        checkConnection();

        // Create recycler view
        myRecycler = (RecyclerView) findViewById(R.id.table);
        myLayoutManager = new LinearLayoutManager(this);
        myRecycler.setLayoutManager(myLayoutManager);

        startPointInput = (EditText) findViewById(R.id.startPointInput);
        endPointInput = (EditText) findViewById(R.id.endPointInput);

        availableRoutesHeading = (TextView) findViewById(R.id.availableRoutesHeading);

        findRouteButton = (Button) findViewById(R.id.findRouteButton);
        findRouteButton.setOnClickListener(this);

        mDbHelper = new BikewaysDatabaseAdapter(this);
        mDbHelper.createDatabase();

        landmarksNavigation = (TextView) findViewById(R.id.landmarksNavigation);
        landmarksNavigation.setOnClickListener(this);

        popularRoutesNavigation = (TextView) findViewById(R.id.popularRoutesNavigation);
        popularRoutesNavigation.setOnClickListener(this);

        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);

        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

        accountNavigation = (Button) findViewById(R.id.accountNavigation);
        accountNavigation.setOnClickListener(this);

    }

    public void checkConnection(){
        ConnectivityManager connectMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            //fetch data

            String networkType = networkInfo.getTypeName().toString();
            Toast.makeText(this, "connected to " + networkType, Toast.LENGTH_LONG).show();
        }
        else {
            //display error
            Toast.makeText(this, "no network connection", Toast.LENGTH_LONG).show();
        }
    }


    public class FindRoute extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            mDbHelper.open();

            String whereStatement = "`Street Name` = '" + startPoint + "' OR `Street Name` = '" + endPoint + "' GROUP BY `Bikeway Type`";
            Cursor bikewaysData = mDbHelper.getTestData(whereStatement);

            int bikewayTypeIndex = bikewaysData.getColumnIndex("Bikeway Type");

            bikewayTypeArrayList = new ArrayList<String>();
            bikewaysData.moveToFirst();
            while (!bikewaysData.isAfterLast()) {
                String bikewayType = bikewaysData.getString(bikewayTypeIndex);
                bikewayTypeArrayList.add(bikewayType);
                bikewaysData.moveToNext();
            }

            mDbHelper.close();

            if(bikewayTypeArrayList.size() <= 0){
                return "No Routes Found";
            }

            return "Available Routes";
        }

        @Override
        protected void onPostExecute(String results) { // called when doInBackground() is done
            super.onPostExecute(results);

            if(results == "No Routes Found"){

                ArrayList<String> routeInfoArrayList = new ArrayList<String>();
                ArrayList<ArrayList<LatLng>> routePointsArrayList =  new ArrayList<>();

                availableRoutesHeading.setText(results);
                myAdapter = new AvailableRoutesAdapter(routeInfoArrayList, routePointsArrayList, startPoint, endPoint, bikewayTypeArrayList, getApplicationContext());
                myRecycler.setAdapter(myAdapter);

            } else{
                availableRoutesHeading.setText(results);
                String startStreet = startPoint.replaceAll("\\s+", "_").toLowerCase();
                String endStreet = endPoint.replaceAll("\\s+", "_").toLowerCase();

                ReadDirectionsJSONDataTask requestRoute = new ReadDirectionsJSONDataTask();
                requestRoute.execute(
                        "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                                startStreet + ",Vancouver,BC&destination=" +
                                endStreet + ",Vancouver,BC&mode=bicycling&alternatives=true&key=AIzaSyCqydQlLuSgi0durCPpfKQLfLslEiefgis");


//            https://maps.googleapis.com/maps/api/directions/json?origin=Kerr,%20Vancouver,BC&destination=E_46th_Ave,Vancouver,BC&mode=bicycling&key=AIzaSyCqydQlLuSgi0durCPpfKQLfLslEiefgis

            }


        }
    }


    private class ReadDirectionsJSONDataTask extends AsyncTask<String, Void, String> {

        Exception exception = null;
        String data = "";
        protected String doInBackground(String... urls) {
            try{
                data = readJSONData(urls[0]);
            }catch(IOException e){
                exception = e;
            }
            return data;
        }

        protected void onPostExecute(String result) {
            try {

                ParserTask parserTask = new ParserTask();

                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);

            } catch (Exception e) {
                Log.d("ReadDirectionsJSON", e.getLocalizedMessage());
            }
        }
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>>
        doInBackground(String...
                               jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
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
            String distance = "";
            String duration = "";

            if(result.size()<1){
                Toast.makeText(getBaseContext(), "No Points",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> routeInfoArrayList = new ArrayList<String>();
            ArrayList<ArrayList<LatLng>> routePointsArrayList =  new ArrayList<>();

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    if(j==0){    // Get distance from the list
                        distance = (String)point.get("distance");
                        continue;
                    }else if(j==1){ // Get duration from the list
                        duration = (String)point.get("duration");
                        continue;
                    }

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                String currentRoute = distance + "," + duration;
                routeInfoArrayList.add(currentRoute);
                routePointsArrayList.add(points);

            }

            myAdapter = new AvailableRoutesAdapter(routeInfoArrayList, routePointsArrayList, startPoint, endPoint, bikewayTypeArrayList, getApplicationContext());
            myRecycler.setAdapter(myAdapter);
        }
    }

    /** A method to download json data from url */
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

            BufferedReader br = new BufferedReader(new
                    InputStreamReader(iStream));

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

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }


    @Override
    public void onClick(View view) {

        // If landmarks was clicked in the top nav
        if (view.getId() == R.id.landmarksNavigation) {
            // start explicit intent to go to landmarks activity
            Intent i = new Intent(view.getContext(), LandmarksActivity.class);
            view.getContext().startActivity(i);
        }

        // If popular routes was clicked in the top nav
        if (view.getId() == R.id.popularRoutesNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), PopularRoutesActivity.class);
            view.getContext().startActivity(i);
        }



        // If Map was clicked in the bottom navigation
        if (view.getId() == R.id.mapNavigation) {
            // start explicit intent to go to landmarks activity
//            Intent i = new Intent(view.getContext(), LandmarksActivity.class);
//            view.getContext().startActivity(i);
            Intent mapIntent = new Intent();
            setResult(RESULT_OK, mapIntent);
            finish();

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

        // If create route was clicked in the top nav
        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to create route activity
            Intent i = new Intent(view.getContext(), AccountActivity.class);
            view.getContext().startActivity(i);
        }

        if(view.getId() == R.id.findRouteButton) {

            // Get the start point entered by the user
            startPoint = startPointInput.getText().toString();

            // Get the end point entered by the user
            endPoint = endPointInput.getText().toString();

            // if no start or end point has been entered, the activity is canceled
            if (startPoint.equals("") || endPoint.equals("")) {
                Toast.makeText(this, "nothing entered", Toast.LENGTH_SHORT).show();
            } else {
                FindRoute findRoute = new FindRoute();
                findRoute.execute();
            }

        }
    }
}
package com.example.finalproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResourcesActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener, LocationListener {

    private Button mapNavigation, checklistNavigation, accountNavigation;

    private TextView tempValue;

    // Pressure sensors
    private SensorManager sensorManager = null;
    private Sensor pressureSensor = null;
    private boolean pressureSensorAvailable = true;

    // Sensor Text Display
    private TextView pressureSensorValue;

    // Resource Link Sections
    private TableRow bikeSafety, bikeLaws, cityVan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        // check if device is connected to the internet
        checkConnection();

        tempValue = (TextView)findViewById(R.id.temperatureTextView);

        // Sensor value text display
        pressureSensorValue = (TextView) findViewById(R.id.pressureSensorValue);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);


        // Get pressure sensor
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        } else{
            // pressure level is not available
            pressureSensorAvailable = false;
            pressureSensorValue.setText("Pressure Level Not Available");
        }

        //speedometer find location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            //start the program if permission is granted
            locationConnect();
        }

        // Resource Links display
        bikeSafety = (TableRow) findViewById(R.id.bikeSafety);
        bikeSafety.setOnClickListener(this);

        bikeLaws = (TableRow) findViewById(R.id.bikeLaws);
        bikeLaws.setOnClickListener(this);

        cityVan = (TableRow) findViewById(R.id.cityVan);
        cityVan.setOnClickListener(this);

        // Navigation buttons
        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);

        accountNavigation = (Button) findViewById(R.id.accountNavigation);
        accountNavigation.setOnClickListener(this);

        new ReadWeatherJSONDataTask().execute(
                "http://api.geonames.org/findNearByWeatherJSON?lat=49.2827&lng=-123.1207&username=cperera1978");

    }

    public void checkConnection(){
        // check if device is connected to the internet
        ConnectivityManager connectMgr =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
        }
        else {
            //display error
            Toast.makeText(this, "No network connection", Toast.LENGTH_LONG).show();
        }
    }

    private String readJSONData(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 2500;

        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();


        try {
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("tag", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private class ReadWeatherJSONDataTask extends AsyncTask<String, Void, String> {
        Exception exception = null;
        protected String doInBackground(String... urls) {
            try{
                // read the data from the url
                return readJSONData(urls[0]);

            }catch(IOException e){
                exception = e;
            }
            return null;
        }
        protected void onPostExecute(String result) {
            try {
                // get the json data
                JSONObject jsonObject = new JSONObject(result);
                JSONObject weatherObservationItems = new JSONObject(jsonObject.getString("weatherObservation"));

                // display temperature
                tempValue.setText(weatherObservationItems.getString("temperature") + "\u00B0" + "C Vancouver");

            } catch (Exception e) {
                Log.d("ReadWeatherJSONDataTask", e.getLocalizedMessage());
            }

        }
    }



    @Override
    protected void onResume() {
        super.onResume();

        if(pressureSensorAvailable) {
            // register pressure sensor
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {

        // release sensor
        sensorManager.unregisterListener(this);
        super.onPause();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] sensorValues = sensorEvent.values;
        // display current sensor data

         if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float pressureLevel = sensorValues[0];

            // display pressure level
            pressureSensorValue.setText(pressureLevel + "hPa");

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View view) {
        // Bike Laws
        if(view.getId() == R.id.bikeLaws) {
            // start the implicit intent
            Uri bikeLawsWebpage = Uri.parse("https://www2.gov.bc.ca/gov/content/transportation/driving-and-cycling/cycling/cycling-regulations-restrictions-rules");
            Intent i = new Intent(Intent.ACTION_VIEW, bikeLawsWebpage);
            startActivity(i);
        }

        // Bike Safety
        if(view.getId() == R.id.bikeSafety) {
            // start the implicit intent
            Uri bikeSafetyWebpage = Uri.parse("https://www.icbc.com/road-safety/sharing/Pages/cycling-safety.aspx");
            Intent i = new Intent(Intent.ACTION_VIEW, bikeSafetyWebpage);
            startActivity(i);
        }

        // City of Vancouver
        if(view.getId() == R.id.cityVan) {
            // start the implicit intent
            Uri cityVanWebpage = Uri.parse("https://vancouver.ca");
            Intent i = new Intent(Intent.ACTION_VIEW, cityVanWebpage);
            startActivity(i);
        }

        // Navigation
        // If map was clicked in the top navigation
        if (view.getId() == R.id.mapNavigation) {
            // start explicit intent to go to map activity
            Intent i = new Intent(view.getContext(), LandingActivity.class);
            view.getContext().startActivity(i);
        }

        // If checklist was clicked in the top navigation
        if (view.getId() == R.id.checklistNavigation) {
            // start explicit intent to go to checklist activity
            Intent i = new Intent(view.getContext(), ChecklistActivity.class);
            view.getContext().startActivity(i);
        }

        // If account was clicked in the top navigation
        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to account activity
            Intent i = new Intent(view.getContext(), AccountActivity.class);
            view.getContext().startActivity(i);
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        TextView txt = (TextView) this.findViewById(R.id.speedValue);

        if (location == null) {
            txt.setText("-.- km/h");
        } else {
            float nCurrentSpeed = location.getSpeed() * 3.6f;
            txt.setText(String.format("%.2f", nCurrentSpeed) + " km/h");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationConnect();
            } else {
                finish();
            }

        }

    }

    private void locationConnect() {
        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (lm != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }






}
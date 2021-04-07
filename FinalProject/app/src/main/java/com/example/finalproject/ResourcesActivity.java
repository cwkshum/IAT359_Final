package com.example.finalproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ResourcesActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener, LocationListener {

    private Button mapNavigation, checklistNavigation, accountNavigation;

    TextView txtTemp, txtWeather;




    // Sensors
    private SensorManager sensorManager = null;
  //  private Sensor tempSensor = null;
    private Sensor pressureSensor = null;

    private boolean tempSensorAvailable = true;
    private boolean pressureSensorAvailable = true;

    SwitchCompat switchCompat;

    ImageView image;

    // Sensor Text Display
    private TextView tempSensorValue, pressureSensorValue;

    // Resource Link Sections
    private TableRow bikeSafety, bikeLaws, cityVan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        checkConnection();



        txtWeather = (TextView)findViewById(R.id.textView3);
        txtTemp = (TextView)findViewById(R.id.textView4);

        new ReadWeatherJSONDataTask().execute(
                "http://api.geonames.org/findNearByWeatherJSON?lat=49.246292&lng=-123.116226&username=cperera1978");

        // Sensor value text display
     //   tempSensorValue = (TextView) findViewById(R.id.tempSensorValue);
        pressureSensorValue = (TextView) findViewById(R.id.pressureSensorValue);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get temperature sensor
//        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
//            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
//
//        } else{
//            // current temperature level is not available
//            tempSensorAvailable = false;
//            tempSensorValue.setText("Temperature Not Available");
//        }

        // Get pressure sensor
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        } else{
            // current acceleration level is not available
            pressureSensorAvailable = false;
            tempSensorValue.setText("Pressure Not Available");
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
    }

//    public void buttonGetWeather(View view) {
//
//        new ReadWeatherJSONDataTask().execute(
//                "http://api.geonames.org/findNearByWeatherJSON?lat=49.246292&lng=-123.116226&username=cperera1978");
//
//
//    }

    public void checkConnection(){
        ConnectivityManager connectMgr =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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

                return readJSONData(urls[0]);

            }catch(IOException e){
                exception = e;
            }
            return null;
        }
        protected void onPostExecute(String result) {
            try {

                JSONObject jsonObject = new JSONObject(result);
                JSONObject weatherObservationItems =
                        new JSONObject(jsonObject.getString("weatherObservation"));

//                Toast.makeText(getBaseContext(),
//                        weatherObservationItems.getString("temperature") +
//                                " - " + weatherObservationItems.getString("weatherCondition"),
//                        Toast.LENGTH_SHORT).show();
                txtWeather.setText("Vancouver");
                txtTemp.setText(weatherObservationItems.getString("temperature") + "\u00B0" + "C");

            } catch (Exception e) {
                Log.d("ReadWeatherJSONDataTask", e.getLocalizedMessage());
            }

        }
    }



    @Override
    protected void onResume() {
        super.onResume();

//        if (tempSensorAvailable) {
//            // register temperature sensor
//            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
//        }

        if(pressureSensorAvailable) {
            // register accelerometer sensor
            sensorManager.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {

        // release sensors
        sensorManager.unregisterListener(this);
        super.onPause();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float[] sensorValues = sensorEvent.values;
        // display current sensor data
//        if(sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
//            float tempLevel = sensorValues[0];
//
//            // display current temperature level
//            tempSensorValue.setText(tempLevel + " ºC");
//
//        }
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
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }

    }






}
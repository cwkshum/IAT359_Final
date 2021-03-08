package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

public class ResourcesActivity extends Activity implements SensorEventListener, View.OnClickListener {

    // Sensors
    private SensorManager sensorManager = null;
    private Sensor tempSensor = null;
    private Sensor accelSensor = null;

    private boolean tempSensorAvailable = true;
    private boolean accelSensorAvailable = true;

    // Sensor Text Display
    private TextView tempSensorValue, accelSensorValue;

    // Resource Link Sections
    private TableRow bikeSafety, bikeLaws, cityVan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        // Sensor value text display
        tempSensorValue = (TextView) findViewById(R.id.tempSensorValue);
        accelSensorValue = (TextView) findViewById(R.id.accelSensorValue);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get temperature sensor
        if(sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        } else{
            // current temperature level is not available
            tempSensorAvailable = false;
            tempSensorValue.setText("Temperature Not Available");
        }

        // Get accelerometer sensor
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        } else{
            // current acceleration level is not available
            accelSensorAvailable = false;
            tempSensorValue.setText("Speed Not Available");
        }

        // Resource Links display
        bikeSafety = (TableRow) findViewById(R.id.bikeSafety);
        bikeSafety.setOnClickListener(this);

        bikeLaws = (TableRow) findViewById(R.id.bikeLaws);
        bikeLaws.setOnClickListener(this);

        cityVan = (TableRow) findViewById(R.id.cityVan);
        cityVan.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (tempSensorAvailable) {
            // register temperature sensor
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(accelSensorAvailable) {
            // register accelerometer sensor
            sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        if(sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            float tempLevel = sensorValues[0];

            // display current temperature level
            tempSensorValue.setText(tempLevel + " ºC");

        } else if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float accelX = sensorValues[0];
            float accelY = sensorValues[1];
            float accelZ = sensorValues[2];

            float accelTot = (float) Math.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

            accelSensorValue.setText(accelTot + " m/s*s");
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

            // PackageManager packageManager = getPackageManager();
            // List<ResolveInfo> activities = packageManager.queryIntentActivities(i, 0);
            // boolean isIntentSafe = activities.size() > 0;
            // if(isIntentSafe){
            //     startActivity(i);
            // }

        }

        // Bike Safety
        if(view.getId() == R.id.bikeSafety) {
            // start the implicit intent
            Uri bikeSafetyWebpage = Uri.parse("https://www.icbc.com/road-safety/sharing/Pages/cycling-safety.aspx");
            Intent i = new Intent(Intent.ACTION_VIEW, bikeSafetyWebpage);
            startActivity(i);

            // PackageManager packageManager = getPackageManager();
            // List<ResolveInfo> activities = packageManager.queryIntentActivities(i, 0);
            // boolean isIntentSafe = activities.size() > 0;
            // if(isIntentSafe){
            //     startActivity(i);
            // }

        }

        // City of Vancouver
        if(view.getId() == R.id.cityVan) {
            // start the implicit intent
            Uri cityVanWebpage = Uri.parse("https://vancouver.ca");
            Intent i = new Intent(Intent.ACTION_VIEW, cityVanWebpage);
            startActivity(i);

            // PackageManager packageManager = getPackageManager();
            // List<ResolveInfo> activities = packageManager.queryIntentActivities(i, 0);
            // boolean isIntentSafe = activities.size() > 0;
            // if(isIntentSafe){
            //     startActivity(i);
            // }

        }


    }
}
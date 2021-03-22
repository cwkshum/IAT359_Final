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
import android.widget.Button;
import android.widget.TextView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.List;

public class ResourcesActivity extends Activity implements SensorEventListener, View.OnClickListener {

    private Button mapNavigation, checklistNavigation, accountNavigation;


    // Sensors
    private SensorManager sensorManager = null;
    private Sensor tempSensor = null;
    private Sensor pressureSensor = null;

    private boolean tempSensorAvailable = true;
    private boolean pressureSensorAvailable = true;

    // Sensor Text Display
    private TextView tempSensorValue, pressureSensorValue;

    // Resource Link Sections
    private TableRow bikeSafety, bikeLaws, cityVan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        // Sensor value text display
        tempSensorValue = (TextView) findViewById(R.id.tempSensorValue);
        pressureSensorValue = (TextView) findViewById(R.id.pressureSensorValue);

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
        if(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE) != null) {
            pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);

        } else{
            // current acceleration level is not available
            pressureSensorAvailable = false;
            tempSensorValue.setText("Pressure Not Available");
        }

        // Resource Links display
        bikeSafety = (TableRow) findViewById(R.id.bikeSafety);
        bikeSafety.setOnClickListener(this);

        bikeLaws = (TableRow) findViewById(R.id.bikeLaws);
        bikeLaws.setOnClickListener(this);

        cityVan = (TableRow) findViewById(R.id.cityVan);
        cityVan.setOnClickListener(this);

        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);

        accountNavigation = (Button) findViewById(R.id.accountNavigation);
        accountNavigation.setOnClickListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (tempSensorAvailable) {
            // register temperature sensor
            sensorManager.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if(pressureSensorAvailable) {
            // register accelerometer sensor
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
        if(sensorEvent.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE){
            float tempLevel = sensorValues[0];

            // display current temperature level
            tempSensorValue.setText(tempLevel + " ºC");

        } else if(sensorEvent.sensor.getType() == Sensor.TYPE_PRESSURE) {
            float pressureLevel = sensorValues[0];
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

        // If landmarks was clicked in the top nav
        if (view.getId() == R.id.mapNavigation) {
            // start explicit intent to go to landmarks activity
            Intent i = new Intent(view.getContext(), LandingActivity.class);
            view.getContext().startActivity(i);
        }

        // If create route was clicked in the bottom nav
        if (view.getId() == R.id.checklistNavigation) {
            // start explicit intent to go to create route activity
            Intent i = new Intent(view.getContext(), ChecklistActivity.class);
            view.getContext().startActivity(i);
        }

        // If create route was clicked in the top nav
        if (view.getId() == R.id.accountNavigation) {
            // start explicit intent to go to create route activity
            Intent i = new Intent(view.getContext(), AccountActivity.class);
            view.getContext().startActivity(i);
        }

    }
}
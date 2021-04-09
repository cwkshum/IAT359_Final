package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText usernameInput, passwordInput;
    private Button loginButton, signupButton;

    public static final String DEFAULT = "not available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // login button
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        // sign up button
        signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);

        // username input
        usernameInput = (EditText) findViewById(R.id.usernameInput);

        // password input
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        // username input
        usernameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                if(s.toString().equals("")){
                    // if noting has been entered, login button is not activated
                    loginButton.setEnabled(false);
                    loginButton.setTextColor(getResources().getColor(R.color.accent_green));
                    loginButton.setBackgroundResource(R.drawable.secondary_button);

                    signupButton.setTextColor(getResources().getColor(R.color.white));
                    signupButton.setBackgroundResource(R.drawable.primary_button);

                }
                else{
                    // if text has been entered, login button is activated
                    loginButton.setEnabled(true);
                    loginButton.setTextColor(getResources().getColor(R.color.white));
                    loginButton.setBackgroundResource(R.drawable.primary_button);

                    signupButton.setTextColor(getResources().getColor(R.color.accent_green));
                    signupButton.setBackgroundResource(R.drawable.secondary_button);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
            }
        });

        // password input
        passwordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fires right as the text is being changed (even supplies the range of text)
                if(s.toString().equals("")){
                    // if noting has been entered, login button is not activated
                    loginButton.setEnabled(false);
                    loginButton.setTextColor(getResources().getColor(R.color.accent_green));
                    loginButton.setBackgroundResource(R.drawable.secondary_button);

                    signupButton.setTextColor(getResources().getColor(R.color.white));
                    signupButton.setBackgroundResource(R.drawable.primary_button);

                }
                else{
                    // if text has been entered, login button is activated
                    loginButton.setEnabled(true);
                    loginButton.setTextColor(getResources().getColor(R.color.white));
                    loginButton.setBackgroundResource(R.drawable.primary_button);

                    signupButton.setTextColor(getResources().getColor(R.color.accent_green));
                    signupButton.setBackgroundResource(R.drawable.secondary_button);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Fires right after the text has changed
            }
        });

        // check if the application has permission to use the user's current location
        checkLocationPermission();
    }

    @Override
    public void onClick(View view) {
        // If login button was clicked
        if(view.getId() == R.id.loginButton) {
            if(checkInput()){
                // if login information is valid
                // start explicit intent to go to landing page activity
                Intent i = new Intent(view.getContext(), LandingActivity.class);
                view.getContext().startActivity(i);
            }
        }

        // If signup button was clicked
        if(view.getId() == R.id.signupButton) {
            // start explicit intent to go to signup activity
            Intent i = new Intent(view.getContext(), SignupActivity.class);
            view.getContext().startActivity(i);
        }
    }

    public boolean checkInput() {
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String username = sharedPrefs.getString("username", DEFAULT);
        String password = sharedPrefs.getString("password", DEFAULT);

        if (username.equals(DEFAULT) || password.equals(DEFAULT)) {
            // if no information is found in sharedpreferences
            Toast.makeText(this, "There is no username/password match", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            if ((username.equals(usernameInput.getText().toString())) && (password.equals(passwordInput.getText().toString()))) {
                // if username and password matches the information stored in sharedpreferences
                return true;

            } else {
                // if username or password does not match the information stored in sharedpreferences
                Toast.makeText(this, "Incorrect username/password", Toast.LENGTH_SHORT).show();
                return false;

            }
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    public boolean checkLocationPermission() {
        // check if user gives permission for the application to use current location
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
                                ActivityCompat.requestPermissions(MainActivity.this,
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
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    }

                } else {
                    // permission denied - Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }
}
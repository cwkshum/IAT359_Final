package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.os.Bundle;
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

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // If login button was clicked
        if(view.getId() == R.id.loginButton) {
            if(checkInput()){
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
            Toast.makeText(this, "Please enter login", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            if ((username.equals(usernameInput.getText().toString())) && (password.equals(passwordInput.getText().toString()))) {
                return true;

            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
                return false;

            }
        }
    }
}
package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText firstNameInput, lastNameInput, emailInput, usernameInput, passwordInput, confirmPasswordInput;
    private Button loginButton, signupButton;

    public static final String DEFAULT = "not available";

    private String firstName, lastName, email, username, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createaccount);

        // User input fields
        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        confirmPasswordInput = (EditText) findViewById(R.id.confirmPasswordInput);

        // login button
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        // sign up button
        signupButton = (Button) findViewById(R.id.signupButton);
        signupButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {

        // If signup button was clicked
        if(view.getId() == R.id.signupButton) {
            if(checkInput()){
                // start explicit intent to go to landing page activity
                Intent i = new Intent(view.getContext(), LandingActivity.class);
                view.getContext().startActivity(i);
            }

        }

        // Go back to login activity when login button has been clicked
        if(view.getId() == R.id.loginButton) {
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            finish();
        }
    }


    public boolean checkInput(){
        // get user input
        firstName = firstNameInput.getText().toString();
        lastName = lastNameInput.getText().toString();
        email = emailInput.getText().toString();
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        confirmPassword = confirmPasswordInput.getText().toString();

        // retrieve data from preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String usernamePref = sharedPrefs.getString("username", DEFAULT);
        String emailPref = sharedPrefs.getString("email", DEFAULT);

        if ((firstName.equals("")) || (lastName.equals("")) || (email.equals("")) || (username.equals("")) || (password.equals("")) || (confirmPassword.equals(""))){
            // if user did not enter all the required data
            Toast.makeText(this, "Please enter sign up information", Toast.LENGTH_SHORT).show();
            return false;

        } else if ((username.equals(usernamePref)) || (email.equals(emailPref))) {
            // if user input for username or email matches the data in preferences
            Toast.makeText(this, "Username or Email already in use", Toast.LENGTH_SHORT).show();
            return false;
        } else{
            if (password.equals(confirmPassword)) {
                // if the password and confirm password match, proceed to saving the user's data
                saveData();
                return true;

            } else {
                // password and confirm password do not match
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return false;

            }
        }

    }

    public void saveData(){
        // Save user information to preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString("firstName", firstName);
        editor.putString("lastName", lastName);
        editor.putString("email", email);
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putInt("mapView", 1);
        editor.putBoolean("darkMode", false);
        editor.putBoolean("cyclingAlerts", true);

        Toast.makeText(this, "Account Created Successfully! Welcome " + firstName, Toast.LENGTH_LONG).show();
        editor.commit();
    }

}

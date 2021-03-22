package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class EditAccountActivity extends Activity implements View.OnClickListener {

    private EditText firstNameInput, lastNameInput, emailInput, usernameInput, passwordInput, confirmPasswordInput;

    private Button updateButton, mapNavigation, resourcesNavigation, checklistNavigation;

    public static final String DEFAULT = "not available";

    private String firstName, lastName, email, username, password, confirmPassword, firstNamePref, lastNamePref, emailPref, usernamePref, passwordPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // Get user's current information
        retrieveData();

        // User input fields
        // First Name input
        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        firstNameInput.setText(firstNamePref);

        // Last Name input
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        lastNameInput.setText(lastNamePref);

        // Username input
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        usernameInput.setText(usernamePref);

        // Email input
        emailInput = (EditText) findViewById(R.id.emailInput);
        emailInput.setText(emailPref);

        // Password input
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        passwordInput.setText(passwordPref);

        // Confirm password input
        confirmPasswordInput = (EditText) findViewById(R.id.confirmPasswordInput);

        // update information button
        updateButton = (Button) findViewById(R.id.updateprofileButton);
        updateButton.setOnClickListener(this);

        // Navigation Buttons
        // Map Navigation
        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        // Checklist Button
        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);

        // Resources Button
        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);


    }

    public void retrieveData(){
        // Get user's current information from shared preferences
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstNamePref= sharedPrefs.getString("firstName", DEFAULT);
        lastNamePref = sharedPrefs.getString("lastName", DEFAULT);
        emailPref = sharedPrefs.getString("email", DEFAULT);
        usernamePref = sharedPrefs.getString("username", DEFAULT);
        passwordPref = sharedPrefs.getString("password", DEFAULT);
    }



    public boolean checkInput(){
        // get user's input from text fields
        firstName = firstNameInput.getText().toString();
        lastName = lastNameInput.getText().toString();
        email = emailInput.getText().toString();
        username = usernameInput.getText().toString();
        password = passwordInput.getText().toString();
        confirmPassword = confirmPasswordInput.getText().toString();

        if ((firstName.equals("")) || (lastName.equals("")) || (email.equals("")) || (username.equals("")) || (password.equals("")) || (confirmPassword.equals(""))){
            // if user did not enter all the required data
            Toast.makeText(this, "Please enter missing information", Toast.LENGTH_SHORT).show();
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

        Toast.makeText(this, "User information updated. Thank you " + firstName, Toast.LENGTH_SHORT).show();
        editor.commit();
    }

    @Override
    public void onClick(View view) {

        // Update user's information
        if (view.getId() == R.id.updateprofileButton) {
            if (checkInput()) {
                // finish activity if updated information has been saved correctly
                Intent accountIntent = new Intent();
                setResult(RESULT_OK, accountIntent);
                finish();
            }
        }

        // Navigation Buttons
        // Map Navigation button clicked in the bottom navigation
        if (view.getId() == R.id.mapNavigation) {
            // start explicit intent to go to map activity
            Intent i = new Intent(view.getContext(), LandingActivity.class);
            view.getContext().startActivity(i);
        }

        // Checklist button clicked in the bottom navigation
        if (view.getId() == R.id.checklistNavigation) {
            // start explicit intent to go to checklist activity
            Intent i = new Intent(view.getContext(), ChecklistActivity.class);
            view.getContext().startActivity(i);
        }

        // Resources button clicked in the bottom navigation
        if (view.getId() == R.id.resourcesNavigation) {
            // start explicit intent to go to resources activity
            Intent i = new Intent(view.getContext(), ResourcesActivity.class);
            view.getContext().startActivity(i);
        }
    }

}
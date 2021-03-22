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
    private Button updateButton;

    private Button mapNavigation, resourcesNavigation, checklistNavigation;


    public static final String DEFAULT = "not available";

    private String firstName, lastName, email, username, password, confirmPassword;

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.updateprofileButton) {
            if (checkInput()) {
                // // start explicit intent to go to landing page activity
                // Intent i = new Intent(view.getContext(), AccountActivity.class);
                // view.getContext().startActivity(i);
                Intent accountIntent = new Intent();
                setResult(RESULT_OK, accountIntent);
                finish();
            }
        }

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

        if (view.getId() == R.id.mapNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), LandingActivity.class);
            view.getContext().startActivity(i);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

        // User input fields
        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        usernameInput = (EditText) findViewById(R.id.usernameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        confirmPasswordInput = (EditText) findViewById(R.id.confirmPasswordInput);

        // update button
        updateButton = (Button) findViewById(R.id.updateprofileButton);
        updateButton.setOnClickListener(this);

        mapNavigation = (Button) findViewById(R.id.mapNavigation);
        mapNavigation.setOnClickListener(this);

        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

        checklistNavigation = (Button) findViewById(R.id.checklistNavigation);
        checklistNavigation.setOnClickListener(this);


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
        String emailPref = sharedPrefs.getString("password", DEFAULT);

        if ((firstName.equals("")) || (lastName.equals("")) || (email.equals("")) || (username.equals("")) || (password.equals("")) || (confirmPassword.equals(""))){
            // if user did not enter all the required data
            Toast.makeText(this, "Please enter missing information", Toast.LENGTH_SHORT).show();
            return false;

        } else if ((username.equals(usernamePref)) || (email.equals(emailPref))) {
            // if user input for username or email matches the data in preferences
            Toast.makeText(this, "Username or Email already in use", Toast.LENGTH_SHORT).show();
            return false;

//        } else if (usernamePref.equals(DEFAULT) || emailPref.equals(DEFAULT)){
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

        Toast.makeText(this, "User Information Updated " + firstName, Toast.LENGTH_LONG).show();
        editor.commit();
    }

}
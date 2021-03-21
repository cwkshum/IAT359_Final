package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class EditAccountActivity extends Activity implements View.OnClickListener {

    private EditText firstNameInput, lastNameInput, emailInput, usernameInput, passwordInput, confirmPasswordInput;
    private Button updateButton;

    public static final String DEFAULT = "not available";

    private String firstName, lastName, email, username, password, confirmPassword;

    @Override
    public void onClick(View v) {



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

        // login button
        updateButton = (Button) findViewById(R.id.updateprofileButton);
        updateButton.setOnClickListener(this);


    }
}
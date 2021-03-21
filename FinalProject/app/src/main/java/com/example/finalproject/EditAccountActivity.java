package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class EditAccountActivity extends Activity implements View.OnClickListener {
    Button edit_button;



    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.editinfo) {
            Intent i = new Intent(v.getContext(), EditAccountActivity.class);
            startActivityForResult(i, 2);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);

    }
}
package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LandingActivity extends Activity implements View.OnClickListener{

    private TextView landmarksNavigation, createRouteNavigation, popularRoutesNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingpage);

        landmarksNavigation = (TextView) findViewById(R.id.landmarksNavigation);
        landmarksNavigation.setOnClickListener(this);

        createRouteNavigation = (TextView) findViewById(R.id.createRouteNavigation);
        createRouteNavigation.setOnClickListener(this);

        popularRoutesNavigation = (TextView) findViewById(R.id.popularRoutesNavigation);
        popularRoutesNavigation.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        // If landmarks was clicked in the top nav
        if(view.getId() == R.id.landmarksNavigation) {
            // start explicit intent to go to landmarks activity
            Intent i = new Intent(view.getContext(), LandmarksActivity.class);
            view.getContext().startActivity(i);
        }

        // If create route was clicked in the top nav
        if(view.getId() == R.id.createRouteNavigation) {
            // start explicit intent to go to create route activity
            Intent i = new Intent(view.getContext(), CreateRouteActivity.class);
            view.getContext().startActivity(i);
        }

        // If popular routes was clicked in the top nav
        if(view.getId() == R.id.popularRoutesNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), PopularRoutesActivity.class);
            view.getContext().startActivity(i);
        }
    }
}

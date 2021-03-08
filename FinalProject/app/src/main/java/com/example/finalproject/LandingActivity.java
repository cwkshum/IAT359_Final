package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

//changed from extends activity to fix getSupportFragmentManager
public class LandingActivity extends AppCompatActivity implements View.OnClickListener{


    private TextView landmarksNavigation, createRouteNavigation, popularRoutesNavigation;

    private Button resourcesNavigation;

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

        resourcesNavigation = (Button) findViewById(R.id.resourcesNavigation);
        resourcesNavigation.setOnClickListener(this);

//        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
//        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

//    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
//            new BottomNavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                    Fragment selectedFragment = null;
//                    switch (item.getItemId()) {
//                        case R.id.ic_home:
//                            selectedFragment = new LandingActivity();
//                            break;
//                        case R.id.ic_favourite:
//                            selectedFragment = new PopularRoutesActivity();
//                            break;
//                        case R.id.ic_landscape:
//                            selectedFragment = new LandmarkActivity();
//                            break;
//                    }
//
//                    getSupportFragmentManager().beginTransaction().replace(R.id.tr_wrapper,
//                            selectedFragment).commit();
//                    return true;
//                }
//            };


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

        // If landmarks was clicked in the top nav
        if(view.getId() == R.id.ic_landscape) {
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


        // If resources was clicked in the bottom nav
        if(view.getId() == R.id.resourcesNavigation) {
            // start explicit intent to go to popular routes activity
            Intent i = new Intent(view.getContext(), ResourcesActivity.class);
            view.getContext().startActivity(i);
        }
    }
}

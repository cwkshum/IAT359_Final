package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AccountActivity extends Activity implements View.OnClickListener {
    Button edit_button;

    private String firstName, username;

    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mImageUrls = new ArrayList<>();

    private TextView nameHeader;


    public static final String DEFAULT = "not available";

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.editinfo) {
            Intent i = new Intent(v.getContext(), EditAccountActivity.class);
            startActivityForResult(i, 2);
        }

    }

    private void getUserInfo(){
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        firstName = sharedPrefs.getString("firstName", DEFAULT);
        username = sharedPrefs.getString("username", DEFAULT);

        nameHeader.setText(firstName);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        edit_button = findViewById(R.id.editinfo);
        edit_button.setOnClickListener(this);

        nameHeader = (TextView) findViewById(R.id.name);

        initImageBitmaps();
        getUserInfo();

    }

    private void initImageBitmaps(){
        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Havasu Falls");

        mImageUrls.add("https://i.redd.it/tpsnoz5bzo501.jpg");
        mNames.add("Trondheim");

        mImageUrls.add("https://i.redd.it/qn7f9oqu7o501.jpg");
        mNames.add("Portugal");


        initRecyclerView();
    }

    private void initRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        FavouriteRouteAdapter adapter = new FavouriteRouteAdapter(this, mNames, mImageUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}

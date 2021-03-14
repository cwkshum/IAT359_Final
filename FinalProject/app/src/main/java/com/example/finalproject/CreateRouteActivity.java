package com.example.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class CreateRouteActivity extends Activity implements View.OnClickListener {

    private EditText startPointInput, endPointInput;
    private Button findRouteButton;
    private String startPoint, endPoint;

    public static final String START_POINT = "STARTPOINT";
    public static final String END_POINT = "ENDPOINT";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroute);

        startPointInput = (EditText) findViewById(R.id.startPointInput);
        endPointInput = (EditText) findViewById(R.id.endPointInput);

        findRouteButton = (Button) findViewById(R.id.findRouteButton);
        findRouteButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        // Go back to map activity when find route button has been clicked
        if(view.getId() == R.id.findRouteButton) {

            // Get the start point entered by the user
            startPoint = startPointInput.getText().toString();

            // Get the end point entered by the user
            endPoint = endPointInput.getText().toString();

            Intent createRouteIntent = new Intent();
            createRouteIntent.putExtra(START_POINT, startPoint);
            createRouteIntent.putExtra(END_POINT, endPoint);

            // if no start or end point has been entered, the activity is canceled
            if (startPoint.equals("") || END_POINT.equals("")) {
                setResult(RESULT_CANCELED, createRouteIntent);
                Toast.makeText(this, "nothing entered", Toast.LENGTH_SHORT).show();
            } else {
                // start and end point has been entered by user, activity can continue
                setResult(RESULT_OK, createRouteIntent);
                Toast.makeText(this, "points saved", Toast.LENGTH_SHORT).show();
            }

            finish();

        }
    }
}

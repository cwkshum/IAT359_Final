package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import static com.example.finalproject.R.layout.available_routes;

import java.util.ArrayList;

public class AvailableRoutesAdapter extends RecyclerView.Adapter<AvailableRoutesAdapter.MyViewHolder> {

    // Array list to store route information
    public ArrayList<String> routeInfoList;

    // Array list to store coordinates
    public static ArrayList<ArrayList<LatLng>> routePointsList;

    // Array list to store the bikeway type
    public static ArrayList<String> bikewayType;

    public static String startPoint, endPoint;
    public static final String START_POINT = "STARTPOINT";
    public static final String END_POINT = "ENDPOINT";
    public static final String ROUTE_POINTS = "ROUTEPOINTS";
    Context context;

    public AvailableRoutesAdapter(ArrayList<String> routeInfoList, ArrayList<ArrayList<LatLng>> routePointsList, String startPoint, String endPoint, ArrayList<String> bikewayType, Context context) {
        this.routeInfoList = routeInfoList;
        this.routePointsList = routePointsList;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.bikewayType = bikewayType;
        this.context = context;
    }

    @Override
    public AvailableRoutesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Set the layout of each view
        View v = LayoutInflater.from(parent.getContext()).inflate(available_routes, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AvailableRoutesAdapter.MyViewHolder holder, int position) {

        // separate the stored route information
        String[] results = (routeInfoList.get(position).toString()).split(",");

        // Set the route option number
        holder.routeOptionNumber.setText("Route " + (position + 1));

        if (bikewayType.size() > 1) {
            // display more than one bikeway type
            String bikeways = "";
            for (int i = 0; i < bikewayType.size(); i++) {
                if (bikeways != "") {
                    bikeways += ", " + bikewayType.get(i);
                } else {
                    bikeways += bikewayType.get(i);
                }
            }
            holder.bikewayType.setText("Bikeway Type: " + bikeways);

        } else {
            // display one bikeway type
            holder.bikewayType.setText("Bikeway Type: " + bikewayType.get(0));
        }

        // display route distance
        holder.routeDistance.setText("Distance: " + results[0]);

        // display route duration
        holder.routeDuration.setText("Duration: " + results[1]);

    }


    @Override
    public int getItemCount() {
        // the size of the route info array
        return routeInfoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView routeOptionNumber, bikewayType, routeDistance, routeDuration;
        public Button startRouteButton;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            // Text views on the display
            routeOptionNumber = (TextView) itemView.findViewById(R.id.routeOptionNumber);
            bikewayType = (TextView) itemView.findViewById(R.id.bikewayType);
            routeDistance = (TextView) itemView.findViewById(R.id.routeDistance);
            routeDuration = (TextView) itemView.findViewById(R.id.routeDuration);

            // start route button
            startRouteButton = (Button) itemView.findViewById(R.id.startRouteButton);
            startRouteButton.setOnClickListener(this);

            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {

            // Start route button
            if(view.getId() == R.id.startRouteButton) {
                // store the coordinates of the route for the line in an array list
                ArrayList<LatLng> routePoints = routePointsList.get(getLayoutPosition());

                startPoint = startPoint + ", Vancouver, BC";
                endPoint = endPoint + ", Vancouver, BC";

                Intent createRouteIntent = new Intent();
                // put the start point in the intent
                createRouteIntent.putExtra(START_POINT, startPoint);

                // put the end point in the intent
                createRouteIntent.putExtra(END_POINT, endPoint);

                // put the route coordinates in the intent
                createRouteIntent.putExtra(ROUTE_POINTS, routePoints);

                // finish the intent and take the stored route information back to the map activity
                ((Activity)context).setResult(Activity.RESULT_OK, createRouteIntent);
                ((Activity)context).finish();

            }

        }
    }
}
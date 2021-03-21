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

    public ArrayList<String> routeInfoList;
    public static ArrayList<ArrayList<LatLng>> routePointsList;
    public static ArrayList<String> bikewayType;
    public static String startPoint;
    public static String endPoint;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(available_routes, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AvailableRoutesAdapter.MyViewHolder holder, int position) {


        String[] results = (routeInfoList.get(position).toString()).split(",");

        holder.routeOptionNumber.setText("Route " + (position + 1));
        if (bikewayType.size() > 1) {
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
            holder.bikewayType.setText("Bikeway Type: " + bikewayType.get(0));
        }
        holder.routeDistance.setText("Distance: " + results[0]);
        holder.routeDuration.setText("Duration: " + results[1]);

    }


    @Override
    public int getItemCount() {
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

            routeOptionNumber = (TextView) itemView.findViewById(R.id.routeOptionNumber);
            bikewayType = (TextView) itemView.findViewById(R.id.bikewayType);
            routeDistance = (TextView) itemView.findViewById(R.id.routeDistance);
            routeDuration = (TextView) itemView.findViewById(R.id.routeDuration);
            startRouteButton = (Button) itemView.findViewById(R.id.startRouteButton);
            startRouteButton.setOnClickListener(this);

            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {

            if(view.getId() == R.id.startRouteButton) {

                ArrayList<LatLng> routePoints = routePointsList.get(getLayoutPosition());

                startPoint = startPoint + ", Vancouver, BC";
                endPoint = endPoint + ", Vancouver, BC";

                Intent createRouteIntent = new Intent();
                createRouteIntent.putExtra(START_POINT, startPoint);
                createRouteIntent.putExtra(END_POINT, endPoint);
                createRouteIntent.putExtra(ROUTE_POINTS, routePoints);

                ((Activity) context).setResult(Activity.RESULT_OK, createRouteIntent);

                ((Activity)context).finish();

            }

        }
    }
}
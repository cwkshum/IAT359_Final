package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import static com.example.finalproject.R.layout.activity_landmarks_row;
import static com.example.finalproject.R.layout.available_routes;

import java.util.ArrayList;

public class LandmarksAdapter extends RecyclerView.Adapter<LandmarksAdapter.MyViewHolder> {

    // Array list to store landmark information
    public static ArrayList<String> landmarkInfoList;
    public static final int REQUEST_LANDMARKDETAIL = 4;
    Context context;

    public LandmarksAdapter(ArrayList<String> landmarkInfoList, Context context) {
        this.landmarkInfoList = landmarkInfoList;
        this.context = context;
    }

    @Override
    public LandmarksAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Set the layout of each view
        View v = LayoutInflater.from(parent.getContext()).inflate(activity_landmarks_row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(LandmarksAdapter.MyViewHolder holder, int position) {

        // separate the stored route information
        String[] results = (landmarkInfoList.get(position).toString()).split("~");

        // remove spaces from the landmark name
        String imageName = results[0].replaceAll("\\s+", "_").toLowerCase();

        // set the image
        holder.landmarkImage.setImageResource(context.getResources().getIdentifier(imageName, "drawable", context.getPackageName()));

        // Set the landmark name
        holder.landmarkName.setText(results[0]);

        // display the landmark info
        holder.landmarkInfo.setText(results[2]);

    }


    @Override
    public int getItemCount() {
        // the size of the landmark info array
        return landmarkInfoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView landmarkName, landmarkInfo;
        public ImageView landmarkImage;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;
            itemView.setOnClickListener(this);

            // Text views on the display
            landmarkName = (TextView) itemView.findViewById(R.id.landmarkName);
            landmarkInfo = (TextView) itemView.findViewById(R.id.landmarkInfo);

            landmarkImage = (ImageView) itemView.findViewById(R.id.landmarkImage);

            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {
            // if the view was clicked
            // get the landmark data from the arraylist based on the view position
            String landmarkData = landmarkInfoList.get(getLayoutPosition());

            // start an explicit intent to the detailed landmark activity, sending the corresponding landmark data
            Intent landmarkDetail = new Intent(context, LandmarkDetailActivity.class);
            landmarkDetail.putExtra("landmarkData", landmarkData);
            ((Activity) context).startActivityForResult(landmarkDetail, REQUEST_LANDMARKDETAIL);

        }
    }
}
package com.example.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import static com.example.finalproject.R.layout.activity_landmarks_row;

import java.util.ArrayList;

public class PopularRoutesAdapter extends RecyclerView.Adapter<PopularRoutesAdapter.MyViewHolder> {

    // Array list to store landmark information
    public static ArrayList<String> popularRoutesInfoList;
    public static final int REQUEST_POPULARROUTEDETAIL = 4;
    Context context;

    public PopularRoutesAdapter(ArrayList<String> popularRoutesInfoList, Context context) {
        this.popularRoutesInfoList = popularRoutesInfoList;
        this.context = context;
    }

    @Override
    public PopularRoutesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Set the layout of each view
        View v = LayoutInflater.from(parent.getContext()).inflate(activity_landmarks_row, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PopularRoutesAdapter.MyViewHolder holder, int position) {

        // separate the stored route information
        String[] results = (popularRoutesInfoList.get(position).toString()).split("&");

        // remove spaces from the landmark name
        String imageName = results[1].replaceAll("\\s+", "_").toLowerCase();

        // set the image
        holder.popularRouteImage.setImageResource(context.getResources().getIdentifier(imageName, "drawable", context.getPackageName()));

        // Set the landmark name
        holder.popularRouteName.setText(results[0]);

        // display the landmark info
        holder.popularRouteInfo.setText(results[1]);

    }


    @Override
    public int getItemCount() {
        // the size of the landmark info array
        return popularRoutesInfoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView popularRouteName, popularRouteInfo;
        public ImageView popularRouteImage;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;
            itemView.setOnClickListener(this);

            // Text views on the display
            popularRouteName = (TextView) itemView.findViewById(R.id.landmarkName);
            popularRouteInfo = (TextView) itemView.findViewById(R.id.landmarkInfo);

            popularRouteImage = (ImageView) itemView.findViewById(R.id.landmarkImage);

            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {
            // if the view was clicked
            // get the landmark data from the arraylist based on the view position
            String popularRouteData = popularRoutesInfoList.get(getLayoutPosition());

            // start an explicit intent to the detailed landmark activity, sending the corresponding landmark data
            Intent popularRouteDetail = new Intent(context, PopularRouteDetailActivity.class);
            popularRouteDetail.putExtra("popularRouteData", popularRouteData);
            ((Activity) context).startActivityForResult(popularRouteDetail, REQUEST_POPULARROUTEDETAIL);

        }
    }
}
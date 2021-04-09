package com.example.finalproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class FavouriteRouteAdapter extends RecyclerView.Adapter<FavouriteRouteAdapter.MyViewHolder>{

    private ArrayList<String> favouriteRoutesInfoList;
    private Context context;
    private String username;
    public static final int REQUEST_FAVOURITEROUTEDETAIL = 5;

    public FavouriteRouteAdapter(ArrayList<String> favouriteRoutesList, String username, Context context) {
        this.favouriteRoutesInfoList = favouriteRoutesList;
        this.username = username;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_favouriterouterow, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // separate the stored route information
        String[] results = (favouriteRoutesInfoList.get(position).toString()).split(",");

        // remove spaces from the bikeway type
        String imageName = results[1].replaceAll("\\s+", "_").toLowerCase();

        // set the image
        holder.favouriteRouteImage.setImageResource(context.getResources().getIdentifier(imageName, "drawable", context.getPackageName()));

        // Set the favourite route name
        holder.favouriteRouteName.setText(results[0]);

        // display the bikeway type
        holder.favouriteRouteInfo.setText(results[1]);
    }

    @Override
    public int getItemCount() {
        // size of favourite routes info list
        return favouriteRoutesInfoList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView favouriteRouteName, favouriteRouteInfo;
        public ImageView favouriteRouteImage;
        public FloatingActionButton deleteButton;
        public LinearLayout myLayout;
        private BikewaysDatabaseAdapter mDbHelper;
        private String routeName, routeType, popularRouteData;

        private ChecklistDatabase db;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;
            itemView.setOnClickListener(this);

            // Text views on the display
            favouriteRouteName = (TextView) itemView.findViewById(R.id.landmarkName);
            favouriteRouteInfo = (TextView) itemView.findViewById(R.id.landmarkInfo);

            // route image
            favouriteRouteImage = (ImageView) itemView.findViewById(R.id.landmarkImage);

            // delete button
            deleteButton = itemView.findViewById(R.id.deleteButton);
            deleteButton.setOnClickListener(this);

            context = itemView.getContext();

            // access the bikeways database
            mDbHelper = new BikewaysDatabaseAdapter(context);
            mDbHelper.createDatabase();

            // access database
            db = new ChecklistDatabase(context);

        }

        // Find a popular route using AsyncTask
        public class FindPopularRoute extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                // open the bikeway database
                mDbHelper.open();

                Cursor popularRoutesData = mDbHelper.getPopularRoutesData(routeName);

                int index1 = popularRoutesData.getColumnIndex(Constants.POPULARNAME);
                int index2 = popularRoutesData.getColumnIndex(Constants.POPULARTYPE);
                int index3 = popularRoutesData.getColumnIndex(Constants.POPULARLENGTH);
                int index4 = popularRoutesData.getColumnIndex(Constants.POPULARCOORDINATES);

                popularRoutesData.moveToFirst();
                while (!popularRoutesData.isAfterLast()) {
                    // get the name of the popular route
                    String name = popularRoutesData.getString(index1);

                    // get the popular route type
                    String type = popularRoutesData.getString(index2);

                    // get the popular route length
                    String length = popularRoutesData.getString(index3);

                    // get the popular routes coordinates
                    String coordinates = popularRoutesData.getString(index4);

                    // insert the retrieved data into the arraylist
                    popularRouteData = name + "&" + type + "&" + length + "&" + coordinates;
                    popularRoutesData.moveToNext();
                }

                // close the database connection
                mDbHelper.close();

                if(popularRouteData.isEmpty()){
                    // no popular routes were found in the db
                    return "No Results Found";
                }

                // route was found in the db
                return "Popular Route Result";
            }

            @Override
            protected void onPostExecute(String results) { // called when doInBackground() is done
                super.onPostExecute(results);

                if(results == "Popular Route Result"){
                    // start an explicit intent to the detailed popular route activity through the main map activity
                    // sending the corresponding route data
                    Intent favouriteRouteDetail = new Intent(context, LandingActivity.class);
                    favouriteRouteDetail.putExtra("popularRouteData", popularRouteData);
                    favouriteRouteDetail.putExtra("fromFavourite", true);
                    ((Activity) context).startActivityForResult(favouriteRouteDetail, REQUEST_FAVOURITEROUTEDETAIL);
                }


            }
        }

        void confirmDialog(String username, String name, String type){
            // create an alert
            AlertDialog.Builder builder = new AlertDialog.Builder(context);

            // ask user if they want to remove route from their favourites
            builder.setTitle("Remove route from your favourites?");
            builder.setMessage("Are you sure you want to remove " + name + "?");

            // if the user selects "Yes"
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // delete the checklists from the database
                    db.deleteFavourite(username, name, type);

                    // Refresh Activity show that the checklists have been cleared
                    Intent intent = new Intent(context, AccountActivity.class);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });

            // if the user selects "Cancel", do not remove route
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            // show alert
            builder.create().show();
        }

        @Override
        public void onClick(View view) {

            // get the route data from the arraylist based on the view position
            String favouriteRouteData = favouriteRoutesInfoList.get(getLayoutPosition());
            String[] results = (favouriteRouteData.toString()).split(",");
            routeName = results[0];
            routeType = results[1];

            if(view.getId() == R.id.deleteButton){
                // call the confirm method
                confirmDialog(username, routeName, routeType);
            } else{
                // if the view was clicked
                // find popular route using AsyncTask
                MyViewHolder.FindPopularRoute findPopularRoute = new MyViewHolder.FindPopularRoute();
                findPopularRoute.execute();
            }

        }
    }
}

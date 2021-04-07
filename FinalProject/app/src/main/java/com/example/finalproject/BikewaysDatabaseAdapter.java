package com.example.finalproject;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BikewaysDatabaseAdapter {

    // Tutorial from: https://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application

    protected static final String TAG = "DataAdapter";

    private final Context mContext;
    private SQLiteDatabase mDb;
    private BikewaysDatabaseHelper mDbHelper;

    public BikewaysDatabaseAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new BikewaysDatabaseHelper(mContext);
    }

    public BikewaysDatabaseAdapter createDatabase() throws SQLException {
        try {
            // create bikeways database
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public BikewaysDatabaseAdapter open() throws SQLException {
        try {
            // open database
            mDbHelper.openDataBase();

            // close database
            mDbHelper.close();

            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        // close database
        mDbHelper.close();
    }

    public Cursor getBikewaysData(String userInput) {
        try {
            String sql;

            // If user has inputted a street, refine the query to only show the data for the selected type
            if(userInput != null && !userInput.isEmpty()){
                sql ="SELECT `Bikeway Type` FROM bikeways WHERE " + userInput;
            } else {
                // default query
                sql = "SELECT `Bike Route Name`, `Street Name`, `Bikeway Type`, `Street Segment Types`, Geom FROM bikeways";
            }

            // execute query
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }

            // return query results
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getLandmarksData(String userInput) {
        try {
            String sql;

            // If user has inputted a search, refine the query to only show the data for the selected type
            if(!userInput.equals(null) && !userInput.isEmpty()){
                sql ="SELECT * FROM " + Constants.LANDMARKS_TABLE_NAME + " WHERE " + Constants.LANDMARKSNAME + " = '" + userInput + "'";
            } else {
                // default query
                sql = "SELECT * FROM " + Constants.LANDMARKS_TABLE_NAME;
            }

            // execute query
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }

            // return query results
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor getPopularRoutesData(String userInput) {
        try {
            String sql;

            // If user has inputted a search, refine the query to only show the data for the selected type
            if(!userInput.equals(null) && !userInput.isEmpty()){
                sql ="SELECT * FROM " + Constants.POPULAR_TABLE_NAME + " WHERE " + Constants.POPULARNAME + " = '" + userInput + "'";
            } else {
                // default query
                sql = "SELECT * FROM " + Constants.POPULAR_TABLE_NAME;
            }

            // execute query
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }

            // return query results
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }

    public Cursor popularRoutesFilterData(String userInput, String filter) {
        try {
            String sql;

            // If user has inputted a search, refine the query to only show the data for the selected type
            if(!userInput.equals(null) && !userInput.isEmpty() && !filter.equals(null) && !filter.isEmpty()){
                sql ="SELECT * FROM " + Constants.POPULAR_TABLE_NAME + " WHERE " + Constants.POPULARNAME + " = '" + userInput + "' AND " + Constants.POPULARTYPE + " = '" + filter + "'";
            } else if (!filter.equals(null) && !filter.isEmpty()){
                // default query
                sql ="SELECT * FROM " + Constants.POPULAR_TABLE_NAME + " WHERE " + Constants.POPULARTYPE + " = '" + filter + "'";
            } else {
                // default query
                sql = "SELECT * FROM " + Constants.POPULAR_TABLE_NAME;
            }

            // execute query
            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }

            // return query results
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}
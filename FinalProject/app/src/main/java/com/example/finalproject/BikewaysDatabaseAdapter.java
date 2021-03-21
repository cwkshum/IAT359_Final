package com.example.finalproject;

import java.io.IOException;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BikewaysDatabaseAdapter {

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
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public BikewaysDatabaseAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e(TAG, "open >>"+ mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    public Cursor getTestData(String userInput) {
        try {
//            String sql ="SELECT * FROM bikeways";

            String sql;

            // If user has inputted a type, refine the query to only show the selected type
            if(userInput != null && !userInput.isEmpty()){
//                sql ="SELECT `Bike Route Name`, `Street Name`, `Bikeway Type`, `Street Segment Types`, Geom FROM bikeways WHERE " + userInput;
                sql ="SELECT `Bikeway Type` FROM bikeways WHERE " + userInput;
            } else {
                sql = "SELECT `Bike Route Name`, `Street Name`, `Bikeway Type`, `Street Segment Types`, Geom FROM bikeways";
            }

            Cursor mCur = mDb.rawQuery(sql, null);
            if (mCur != null) {
                mCur.moveToNext();
            }
            return mCur;
        } catch (SQLException mSQLException) {
            Log.e(TAG, "getTestData >>"+ mSQLException.toString());
            throw mSQLException;
        }
    }
}

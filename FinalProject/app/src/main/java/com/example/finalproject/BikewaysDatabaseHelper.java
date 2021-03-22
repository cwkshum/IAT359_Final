package com.example.finalproject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BikewaysDatabaseHelper extends SQLiteOpenHelper {

    // Tutorial from: https://stackoverflow.com/questions/9109438/how-to-use-an-existing-database-with-an-android-application

    // LogCat Tag
    private static String TAG = "DataBaseHelper";

    // Imported database name
    private static String DB_NAME ="bikeways.sqlite";

    // Database version
    private static int DB_VERSION = 1;

    private final File DB_FILE;
    private SQLiteDatabase mDataBase;
    private final Context mContext;

    public BikewaysDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        DB_FILE = context.getDatabasePath(DB_NAME);
        this.mContext = context;
    }

    public void createDataBase() throws IOException {
        // If the database does not exist, copy it from the assets.
        boolean mDataBaseExist = checkDataBase();
        if(!mDataBaseExist) {
            this.getReadableDatabase();
            this.close();
            try {
                // Copy the database from assests
                copyDataBase();
                Log.e(TAG, "createDatabase database created");
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDataBase");
            }
        }
    }

    private boolean checkDataBase() {
        // Check that the database file exists in databases folder
        return DB_FILE.exists();
    }

    private void copyDataBase() throws IOException {
        // Copy the database from assets
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        OutputStream mOutput = new FileOutputStream(DB_FILE);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException {
        // Open the database, so we can query it
        mDataBase = SQLiteDatabase.openDatabase(String.valueOf(DB_FILE), null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if(mDataBase != null) {
            mDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
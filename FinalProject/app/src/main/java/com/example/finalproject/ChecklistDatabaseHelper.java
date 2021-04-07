package com.example.finalproject;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ChecklistDatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    // Create checklist table in the db
    private static final String CREATE_CHECKLIST_TABLE =
            "CREATE TABLE "+
                    Constants.CHECKLIST_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.USERNAME + " TEXT, " + Constants.CHECKLISTNAME + " TEXT, " + Constants.DESCRIPTION + " TEXT);";


    // Create to do table in the db
    private static final String CREATE_TODO_TABLE =
            "CREATE TABLE "+
                    Constants.TODO_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.USERNAME + " TEXT, " + Constants.CHECKLISTNAME + " TEXT, " + Constants.TODO + " TEXT, " +
                    Constants.STATUS + " INTEGER);" ;

    // Create to do table in the db
    private static final String CREATE_FAVOURITE_TABLE =
            "CREATE TABLE "+
                    Constants.FAVOURITE_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.USERNAME + " TEXT, " + Constants.ROUTENAME + " TEXT, " + Constants.ROUTETYPE + " TEXT);" ;

    private static final String DROP_CHECKLIST_TABLE = "DROP TABLE IF EXISTS " + Constants.CHECKLIST_TABLE_NAME;
    private static final String DROP_TODO_TABLE = "DROP TABLE IF EXISTS " + Constants.TODO_TABLE_NAME;
    private static final String DROP_FAVOURITE_TABLE = "DROP TABLE IF EXISTS " + Constants.FAVOURITE_TABLE_NAME;

    public ChecklistDatabaseHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            // create checklist and to do table in the db
            db.execSQL(CREATE_CHECKLIST_TABLE);
            db.execSQL(CREATE_TODO_TABLE);
            db.execSQL(CREATE_FAVOURITE_TABLE);
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // drop the checklist and to do table
            db.execSQL(DROP_CHECKLIST_TABLE);
            db.execSQL(DROP_TODO_TABLE);
            db.execSQL(DROP_FAVOURITE_TABLE);

            // recreate the tables
            onCreate(db);
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
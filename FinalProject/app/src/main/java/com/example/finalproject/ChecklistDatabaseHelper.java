package com.example.finalproject;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ChecklistDatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    // Create checklist table
    private static final String CREATE_CHECKLIST_TABLE =
            "CREATE TABLE "+
                    Constants.CHECKLIST_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.USERNAME + " TEXT, " + Constants.CHECKLISTNAME + " TEXT, " + Constants.DESCRIPTION + " TEXT);";


    // Create to do table
    private static final String CREATE_TODO_TABLE =
            "CREATE TABLE "+
                    Constants.TODO_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.USERNAME + " TEXT, " + Constants.CHECKLISTNAME + " TEXT, " + Constants.TODO + " TEXT, " +
                    Constants.STATUS + " INTEGER);" ;

    private static final String DROP_CHECKLIST_TABLE = "DROP TABLE IF EXISTS " + Constants.CHECKLIST_TABLE_NAME;
    private static final String DROP_TODO_TABLE = "DROP TABLE IF EXISTS " + Constants.TODO_TABLE_NAME;

    public ChecklistDatabaseHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // display toast message when onCreate() is called
        try {
            db.execSQL(CREATE_CHECKLIST_TABLE);
            db.execSQL(CREATE_TODO_TABLE);
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_CHECKLIST_TABLE);
            db.execSQL(DROP_TODO_TABLE);
            onCreate(db);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
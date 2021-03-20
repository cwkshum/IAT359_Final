package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class  MyHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Checklist.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_checklist";
    private static final String CL_ID = "_id";
    private static final String CLNAME = "checklist_name";
    private static final String CLDESCRIPTION = "checklist_description";

//    private Context context;
//
//    private static final String CREATE_TABLE =
//            "CREATE TABLE "+
//                    Constants.TABLE_NAME + " (" +
//                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                    Constants.CLNAME + " TEXT, " +
//                    Constants.CLDESCRIPTION + " TEXT);" ;
//
//    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;




    public MyHelper(@Nullable Context context){
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + CL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CLNAME + " TEXT, " +
                CLDESCRIPTION + " VARCHAR(255));";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    void addList(String checklist_name, String checklist_description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLNAME, checklist_name);
        cv.put(CLDESCRIPTION, checklist_description);

        long result = db.insert(TABLE_NAME,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

   public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id, String name, String description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(CLNAME, name);
        cv.put(CLDESCRIPTION, description);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);

    }
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        try {
//            db.execSQL(DROP_TABLE);
//            onCreate(db);
//            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
//        } catch (SQLException e) {
//            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
//        }
//    }
}
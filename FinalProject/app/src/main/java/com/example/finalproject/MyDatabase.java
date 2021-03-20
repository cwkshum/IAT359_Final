//package com.example.finalproject;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import static com.example.finalproject.Constants.TABLE_NAME;
//
//public class MyDatabase {
//    private SQLiteDatabase db;
//    private Context context;
//    private final MyHelper helper;
//
//    public MyDatabase (Context c){
//        context = c;
//        helper = new MyHelper(context);
//    }
//
//    public long insertData (String name, String description)
//    {
//        db = helper.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(Constants.CLNAME, name);
//        contentValues.put(Constants.CLDESCRIPTION, description);
//
//        long id = db.insert(TABLE_NAME, null, contentValues);
//        return id;
//    }
//
//    public static Cursor getData()
//    {
//        SQLiteDatabase db = helper.getWritableDatabase();
//
//        String[] columns = {Constants.UID, Constants.CLNAME, Constants.CLDESCRIPTION};
//        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);
//        return cursor;
//    }
//
////    public boolean updateData( String name, String description){
////        db = helper.getWritableDatabase();
////        ContentValues contentValues = new ContentValues();
////    //    contentValues.put(Constants.UID, id);
////        contentValues.put(Constants.CLNAME, name);
////        contentValues.put(Constants.CLDESCRIPTION, description);
////   //     db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
////        return true;
////
////    }
//
//
////    public String getSelectedData(String name)
////    {
////        SQLiteDatabase db = helper.getWritableDatabase();
////        String[] columns = {Constants.UID, Constants.CLNAME, Constants.CLDESCRIPTION};
////
////        String selection = Constants.CLNAME + "='" +name+ "'";  //Constants.CLNAME = 'name'
////        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);
////
////        StringBuffer buffer = new StringBuffer();
////        while (cursor.moveToNext()) {
////
////            int index1 = cursor.getColumnIndex(Constants.CLNAME);
////            int index2 = cursor.getColumnIndex(Constants.CLDESCRIPTION);
////            String checklistName = cursor.getString(index1);
////            String checklistDescription = cursor.getString(index2);
////
////            buffer.append(checklistName + " " + checklistDescription + "\n");
////        }
////        return buffer.toString();
////    }
////
//
//}
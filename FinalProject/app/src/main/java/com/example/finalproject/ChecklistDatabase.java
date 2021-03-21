package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChecklistDatabase {

    private SQLiteDatabase db;
    private Context context;
    private final ChecklistDatabaseHelper helper;

    public ChecklistDatabase (Context c){
        context = c;
        helper = new ChecklistDatabaseHelper(context);
    }

    public long insertCheckListData (String username, String checklistName, String description){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String sql = "SELECT " + Constants.CHECKLISTNAME + " FROM " + Constants.CHECKLIST_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() < 1) {
            contentValues.put(Constants.USERNAME, username);
            contentValues.put(Constants.CHECKLISTNAME, checklistName);
            contentValues.put(Constants.DESCRIPTION, description);

            // insert values
            long id = db.insert(Constants.CHECKLIST_TABLE_NAME, null, contentValues);
            return id;
        } else {
            return -1;
        }

    }

    public long insertToDoData (String username, String checklistName, String todo, Integer status){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String sql = "SELECT " + Constants.TODO + " FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.TODO + " = '" + todo + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() < 1) {
            contentValues.put(Constants.USERNAME, username);
            contentValues.put(Constants.CHECKLISTNAME, checklistName);
            contentValues.put(Constants.TODO, todo);
            contentValues.put(Constants.STATUS, status);

            // insert values
            long id = db.insert(Constants.TODO_TABLE_NAME, null, contentValues);
            return id;
        } else {
            return -1;
        }
    }

    public Cursor getChecklistData(){
        SQLiteDatabase db = helper.getWritableDatabase();

        // database query
        String[] columns = {Constants.UID, Constants.USERNAME, Constants.CHECKLISTNAME, Constants.DESCRIPTION};
        Cursor cursor = db.query(Constants.CHECKLIST_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getToDoData(String username, String checklistName){
        SQLiteDatabase db = helper.getWritableDatabase();

        // database query
        String sql = "SELECT " + Constants.TODO + ", " + Constants.STATUS + " FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null) {
            cursor.moveToNext();
        }

        return cursor;
    }

    public void deleteAllData(String username){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("DELETE FROM " + Constants.CHECKLIST_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "'");
        db.execSQL("DELETE FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "'");

    }

    public void deleteChecklist(String username, String checklistName){
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM " + Constants.CHECKLIST_TABLE_NAME + " WHERE " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.USERNAME + " = '" + username + "'");
        db.execSQL("DELETE FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.USERNAME + " = '" + username + "'");

    }

    public void deleteToDo(String username, String checklistName, String item){
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("DELETE FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.USERNAME + " = '" + username + "' AND " + Constants.TODO + " = '" + item + "'");

    }

    public boolean updateChecklistData(String username, String checklistName, String newChecklistName, String newChecklistDescription){
        SQLiteDatabase db = helper.getWritableDatabase();

        if(checklistName.equals(newChecklistName)){
            db.execSQL("UPDATE " + Constants.CHECKLIST_TABLE_NAME + " SET " + Constants.CHECKLISTNAME + " = '" + newChecklistName + "' , " + Constants.DESCRIPTION + " = '" + newChecklistDescription + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'");
            return true;
        } else {
            String sql = "SELECT " + Constants.CHECKLISTNAME + " FROM " + Constants.CHECKLIST_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + newChecklistName + "'";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                db.execSQL("UPDATE " + Constants.CHECKLIST_TABLE_NAME + " SET " + Constants.CHECKLISTNAME + " = '" + newChecklistName + "' , " + Constants.DESCRIPTION + " = '" + newChecklistDescription + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'");
                db.execSQL("UPDATE " + Constants.TODO_TABLE_NAME + " SET " + Constants.CHECKLISTNAME + " = '" + newChecklistName + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'");
                return true;
            } else {
                return false;
            }
        }
    }

    public void updateToDoStatus(String username, String checklistName, String toDoItem, Integer status){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("UPDATE " + Constants.TODO_TABLE_NAME + " SET " + Constants.STATUS + " = '" + status + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.TODO + " = '" + toDoItem + "'");
    }

    public boolean updateToDoData(String username, String checklistName, String toDoItem, String newToDoItem){
        SQLiteDatabase db = helper.getWritableDatabase();

        if(toDoItem.equals(newToDoItem)){
            return true;
        } else {
            String sql = "SELECT " + Constants.TODO + " FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.TODO + " = '" + newToDoItem + "'";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                db.execSQL("UPDATE " + Constants.TODO_TABLE_NAME + " SET " + Constants.TODO + " = '" + newToDoItem + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName  + "' AND " + Constants.TODO + " = '" + toDoItem + "'");
                return true;
            } else {
                return false;
            }
        }
    }

}
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
        // insert checklist data to the checklist table of the db
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // check to see if an existing checklist with the same name is in the database that corresponds to the current username
        String sql = "SELECT " + Constants.CHECKLISTNAME + " FROM " + Constants.CHECKLIST_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() < 1) {
            contentValues.put(Constants.USERNAME, username);
            contentValues.put(Constants.CHECKLISTNAME, checklistName);
            contentValues.put(Constants.DESCRIPTION, description);

            // If no matches in the db was found, insert the data of the new checklist into the db
            long id = db.insert(Constants.CHECKLIST_TABLE_NAME, null, contentValues);
            return id;
        } else {
            // a match was found in the database, do not insert the new checklist into the db
            return -1;
        }

    }

    public long insertToDoData (String username, String checklistName, String todo, Integer status){
        // insert to do item into the to do table of the db
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        // check to see if an existing to do item with the same name is in the database that corresponds to the current username and checklist
        String sql = "SELECT " + Constants.TODO + " FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.TODO + " = '" + todo + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount() < 1) {
            contentValues.put(Constants.USERNAME, username);
            contentValues.put(Constants.CHECKLISTNAME, checklistName);
            contentValues.put(Constants.TODO, todo);
            contentValues.put(Constants.STATUS, status);

            // If no matches in the db was found, insert the data of the new to do item into the db
            long id = db.insert(Constants.TODO_TABLE_NAME, null, contentValues);
            return id;
        } else {
            // a match was found in the database, do not insert the new to do item into the db
            return -1;
        }
    }

    public Cursor getChecklistData(){
        // retrieve the checklist data from the checklist table of the database
        SQLiteDatabase db = helper.getWritableDatabase();

        // database query
        String[] columns = {Constants.UID, Constants.USERNAME, Constants.CHECKLISTNAME, Constants.DESCRIPTION};
        Cursor cursor = db.query(Constants.CHECKLIST_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public Cursor getToDoData(String username, String checklistName){
        // retrieve the to do data from the to do table of the database
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
        // delete data from the database
        SQLiteDatabase db = helper.getWritableDatabase();

        // delete all checklists from the checklist table in the db that corresponds to the current user
        db.execSQL("DELETE FROM " + Constants.CHECKLIST_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "'");

        // delete all to do items from the to do table in the db that corresponds to the current user
        db.execSQL("DELETE FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "'");

    }

    public void deleteChecklist(String username, String checklistName){

        // delete a single checklist from the database
        SQLiteDatabase db = helper.getWritableDatabase();

        // delete the checklist from the checklist table in the db that corresponds to the current user and selected checklist
        db.execSQL("DELETE FROM " + Constants.CHECKLIST_TABLE_NAME + " WHERE " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.USERNAME + " = '" + username + "'");

        // delete all to do items from the to do table in the db that corresponds to the current user and the checklist that was deleted
        db.execSQL("DELETE FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.USERNAME + " = '" + username + "'");

    }

    public void deleteToDo(String username, String checklistName, String item){

        // delete single to do item
        SQLiteDatabase db = helper.getWritableDatabase();

        // delete to do item from the to do table in the database that matches the current checklist and user
        db.execSQL("DELETE FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.USERNAME + " = '" + username + "' AND " + Constants.TODO + " = '" + item + "'");

    }

    public boolean updateChecklistData(String username, String checklistName, String newChecklistName, String newChecklistDescription){

        // update the checklist table in the database
        SQLiteDatabase db = helper.getWritableDatabase();

        if(checklistName.equals(newChecklistName)){
            // update the checklist name and description if the old checklist name matches the new one
            db.execSQL("UPDATE " + Constants.CHECKLIST_TABLE_NAME + " SET " + Constants.CHECKLISTNAME + " = '" + newChecklistName + "' , " + Constants.DESCRIPTION + " = '" + newChecklistDescription + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'");
            return true;
        } else {
            // check if there are matches for the new checklist name in the db
            String sql = "SELECT " + Constants.CHECKLISTNAME + " FROM " + Constants.CHECKLIST_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + newChecklistName + "'";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                // if no matches are found, update the checklist name with the new checklist name in the checklist table of the db
                db.execSQL("UPDATE " + Constants.CHECKLIST_TABLE_NAME + " SET " + Constants.CHECKLISTNAME + " = '" + newChecklistName + "' , " + Constants.DESCRIPTION + " = '" + newChecklistDescription + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'");

                // update the checklist name with the new checklist name in the to do table of the db
                db.execSQL("UPDATE " + Constants.TODO_TABLE_NAME + " SET " + Constants.CHECKLISTNAME + " = '" + newChecklistName + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "'");
                return true;
            } else {
                // match found, do not update the db
                return false;
            }
        }
    }

    public void updateToDoStatus(String username, String checklistName, String toDoItem, Integer status){
        // update status of to do item
        SQLiteDatabase db = helper.getWritableDatabase();

        // update the status of the to do item in the to do table of the db based on whether or not it is checked or not checked
        db.execSQL("UPDATE " + Constants.TODO_TABLE_NAME + " SET " + Constants.STATUS + " = '" + status + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.TODO + " = '" + toDoItem + "'");
    }

    public boolean updateToDoData(String username, String checklistName, String toDoItem, String newToDoItem){
        // update to do data
        SQLiteDatabase db = helper.getWritableDatabase();

        if(toDoItem.equals(newToDoItem)){
            // nothing to update if the old to do name is the same as the new to do name
            return true;
        } else {
            // check if the new to do name matches a result in the db
            String sql = "SELECT " + Constants.TODO + " FROM " + Constants.TODO_TABLE_NAME + " WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName + "' AND " + Constants.TODO + " = '" + newToDoItem + "'";
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                // update the to do table in the db with the new to do item name if no matches were found
                db.execSQL("UPDATE " + Constants.TODO_TABLE_NAME + " SET " + Constants.TODO + " = '" + newToDoItem + "' WHERE " + Constants.USERNAME + " = '" + username + "' AND " + Constants.CHECKLISTNAME + " = '" + checklistName  + "' AND " + Constants.TODO + " = '" + toDoItem + "'");
                return true;
            } else {
                // match found, do not update the db
                return false;
            }
        }
    }

}
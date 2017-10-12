package com.example.huqicheng.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHandler extends SQLiteOpenHelper {

    //name of my database
    private static String DATABASE_NAME = "calender.db";
    //name of my calendar
    private static String TABLE_NAME = "CALENDER";
    //name of my four columns in the database
    private static String COL1 = "ID";
    private static String COL2 = "EVENTNAME";
    private static String COL3 = "EVENTLOCATION";
    private static String COL4 = "EVENTDISCRIPTION";

    public dbHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //creating the table
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY,EVENTNAME TEXT,EVENTLOCATION TEXT,EVENTDISCRIPTION TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //drop the pervious data and upgrade it to the new data.
        db.execSQL("DROP TABLE IF EXIST" + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(int ID, String EventName, String EventLocation, String EventDiscription) {
        //SQLiteDatabase is used to create a database if not exist and open one if exists.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues used to insert data into table
        contentValues.put(COL1, ID);
        contentValues.put(COL2, EventName);
        contentValues.put(COL3, EventLocation);
        contentValues.put(COL4, EventDiscription);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }
    //update my table according to the new table
    public boolean updateData(int id, String EventName, String EventLocation, String EventDiscription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, id);
        contentValues.put(COL2, EventName);
        contentValues.put(COL3, EventLocation);
        contentValues.put(COL4, EventDiscription);
        String Ind=Integer.toString(id);
        long result = db.update(TABLE_NAME,contentValues,"ID = ?",new String[] { Ind });

        db.close();
        if (result == -1)
            return false;
        else
            return true;
    }

    // delete the data entry
    public int deleteEvent(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Ind=Integer.toString(id);
        return db.delete(TABLE_NAME,"ID = ?",new String[] { Ind });
    }

    //return all the data,with a cursor pointing to it
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }
}

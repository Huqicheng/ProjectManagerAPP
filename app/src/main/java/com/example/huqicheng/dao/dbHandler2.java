package com.example.huqicheng.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHandler2 extends SQLiteOpenHelper{

    private static String DATABASE_NAME="attendees.db";
    private static String TABLENAME = "attendees";
    private static String COL1="ID";
    private static String COL2="ATTENDEES";

    public dbHandler2(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLENAME +" (ID INTEGER PRIMARY KEY,ATTENDEES TEXT );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("TABLE DROP IF EXISTS"+TABLENAME);
        onCreate(db);
    }

    public boolean insertAttendees(int ID,String Attendee){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,ID);
        contentValues.put(COL2,Attendee);
        long result=db.insert(TABLENAME,null,contentValues);
        if(result==-1)
            return false;
        else
            return true;
    }

    public boolean updateAttendee(int id,String Attendee){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,id);
        contentValues.put(COL2,Attendee);
        String Ind=Integer.toString(id);
        long result=db.update(TABLENAME,contentValues,"ID = ?",new String[] { Ind });
        if(result==-1)
            return false;
        else
            return true;
    }

    public int deleteEvent(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String Ind=Integer.toString(id);
        return db.delete(TABLENAME,"ID = ?",new String[] { Ind });
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLENAME,null);
        return res;
    }

}

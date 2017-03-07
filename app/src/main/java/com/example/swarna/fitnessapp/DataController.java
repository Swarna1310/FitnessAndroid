package com.example.swarna.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DataController
{
	public static final String MESSAGE="Message";
	public static final String TABLE_NAME="Workout_Table";
	public static final String DATABASE_NAME="Fitness.db";
	public static final int DATABASE_VERSION=2;
	public static final String TABLE_CREATE="create table if not exists Workout_Table (StartDate VARCHAR,Duration VARCHAR,Walksteps VARCHAR,Calories VARCHAR,Miles VARCHAR);";
	
	DataBaseHelper dbHelper;
	Context context;
	SQLiteDatabase db;
    SQLiteDatabase db2;
	
	public DataController(Context context)
	{
		this.context=context;
		dbHelper=new DataBaseHelper(context);
	}
	
	public DataController open()
	{
		db=dbHelper.getWritableDatabase();
        db2=dbHelper.getReadableDatabase();
		return this;
	}
	
	public void close()
	{
		dbHelper.close();
	}
	
	public boolean insert(String date,String duration,String steps,String cal,String mile)
	{
		ContentValues content=new ContentValues();
		//content.put(MESSAGE, message);
		content.put("StartDate",date);
        content.put("Duration",duration);
		content.put("Walksteps", steps);
		content.put("Calories", cal);
		content.put("Miles", mile);
        System.out.println("Item inserted: " + date + " " + duration + " " + steps + " " + cal + " " + mile);
		db.insert(TABLE_NAME, null, content);
        db.close();
		return true;
	}
	public ArrayList<WorkoutDialog> fetchdata()
    {
        ArrayList<WorkoutDialog> workoutlist = new ArrayList<WorkoutDialog>();
        WorkoutDialog session;
        Cursor cursor = getWorkouts();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            session = new WorkoutDialog();
            String date = cursor.getString(cursor.getColumnIndex("StartDate"));
            String duration = cursor.getString(cursor.getColumnIndex("Duration"));
            String steps = cursor.getString(cursor.getColumnIndex("Walksteps"));
            String cal = cursor.getString(cursor.getColumnIndex("Calories"));
            String mile = cursor.getString(cursor.getColumnIndex("Miles"));
            session.startdate = date;
            session.duration = duration;
            session.steps = steps;
            session.calburned = cal;
            session.mileswalked = mile;
            workoutlist.add(session);
            System.out.println("value fetched: " + date + duration + steps + cal + mile);
            cursor.moveToNext();
            db2.close();

        }


        cursor.close();
        return workoutlist;
    }
	public Cursor getWorkouts()
	{
        Cursor cursor = db2.rawQuery("SELECT * FROM " + TABLE_NAME, null);
		return cursor;
	}
	
	private static class DataBaseHelper extends SQLiteOpenHelper
	{

		public DataBaseHelper(Context context)
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			try
			{
				db.execSQL(TABLE_CREATE);
			}
			catch(SQLiteException e)
			{
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS Workout_Table");
			onCreate(db);
		}
		
	}
	
}
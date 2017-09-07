package com.mykola.schedule.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    
    private static final String DB_NAME = "LESSONS_TABLE";
    
    public static final String TABLE_NAME = "TABLE_NAME";

    private static final String INTEGER = "integer";
    private static final String TEXT = "text";

    public static final String DAY_NUMBER = "day_number";
    public static final String LESSON_NAME = "lesson_name";
    public static final String LESSON_NUMBER = "lesson_number";
    public static final String LESSON_TYPE = "lesson_type";
    public static final String LESSON_ROOM = "lesson_room";
    public static final String TEACHER_NAME = "teacher_name";
    public static final String LESSON_WEEK = "lesson_week";
    
    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + TABLE_NAME + " ("
                + LESSON_WEEK + " " + INTEGER + ","
                + LESSON_NAME + " " + TEXT + ","
                + LESSON_NUMBER + " " + INTEGER + ","
                + LESSON_ROOM + " " + TEXT + ","
                + LESSON_TYPE + " " + TEXT + ","
                + TEACHER_NAME + " " + TEXT + ","
                + DAY_NUMBER + " " + INTEGER +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}


package com.mykola.schedule.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mykola.schedule.utils.Constants;
import com.mykola.schedule.data.storage.models.LessonDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mykola on 16.01.17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + Constants.TABLE_NAME + " ("
                + Constants.LESSON_WEEK + " " + Constants.INTEGER + ","
                + Constants.LESSON_NAME + " " + Constants.TEXT + ","
                + Constants.LESSON_NUMBER + " " + Constants.INTEGER + ","
                + Constants.LESSON_ROOM + " " + Constants.TEXT + ","
                + Constants.LESSON_TYPE + " " + Constants.TEXT + ","
                + Constants.TEACHER_NAME + " " + Constants.TEXT + ","
                + Constants.TIME_START + " " + Constants.TEXT + ","
                + Constants.TIME_END + " " + Constants.TEXT + ","
                + Constants.DAY_NUMBER + " " + Constants.INTEGER +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}


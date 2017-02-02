package com.mykola.schedule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mykola on 16.01.17.
 */

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, Constants.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // создаем таблицу с полями
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


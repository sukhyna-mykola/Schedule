package com.mykola.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

class DBHelper extends SQLiteOpenHelper {

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

    /**
     * Зчитує всі предмети по номеру тижня
     * @param weekNumber = номер тижня
     * @return HashMap предметів, де key-номер дня в тижні, value-список предметів в цей день
     */
    public HashMap<Integer, List<Lesson>> readLessonsFromDB(int weekNumber) {

        HashMap<Integer, List<Lesson>> lessonsOfWeek = new HashMap<>();
        List<Lesson> lessonsOfDay;

        SQLiteDatabase db = getWritableDatabase();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//
        for (int i = 1; i <= 6; i++) {

            lessonsOfDay = new ArrayList<>();

            Cursor c = db.query(Constants.TABLE_NAME, null, Constants.DAY_NUMBER + "=? AND " + Constants.LESSON_WEEK + "=?",
                    new String[]{String.valueOf(i), String.valueOf(weekNumber)}, null, null, null);

            // int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

            if (c.moveToFirst()) {

                int dayColIndex = c.getColumnIndex(Constants.DAY_NUMBER);
                int nameColIndex = c.getColumnIndex(Constants.LESSON_NAME);
                int teacherlColIndex = c.getColumnIndex(Constants.TEACHER_NAME);
                int typeColIndex = c.getColumnIndex(Constants.LESSON_TYPE);
                int roomColIndex = c.getColumnIndex(Constants.LESSON_ROOM);
                int numberColIndex = c.getColumnIndex(Constants.LESSON_NUMBER);
                int weekColIndex = c.getColumnIndex(Constants.LESSON_WEEK);
                int startColIndex = c.getColumnIndex(Constants.TIME_START);
                int endlIndex = c.getColumnIndex(Constants.TIME_END);


                do {

                    String name = c.getString(nameColIndex);
                    String type = c.getString(typeColIndex);
                    String teacher = c.getString(teacherlColIndex);
                    String room = c.getString(roomColIndex);
                    String number = c.getString(numberColIndex);
                    String day = c.getString(dayColIndex);
                    String week = c.getString(weekColIndex);
                    String start = c.getString(startColIndex);
                    String end = c.getString(endlIndex);

                    Lesson lesson = new Lesson(name, type, teacher, room, number, day, week, start, end);
                    lessonsOfDay.add(lesson);
                    try {
                        Date startTime = df.parse(lesson.getTimeStart());
                        Date endTime = df.parse(lesson.getTimeEnd());
                        Date thisTime = df.parse(df.format(calendar.getTime()));
/*
                    if ((day == Integer.parseInt(lesson.getDayNumber())) && (Integer.parseInt(lesson.getLessonWeek()) == currentWeek)) {
                        if (thisTime.after(start) && thisTime.before(end)) {
                            lesson.setCurrentLesson(true);
                        }

                        lesson.setCurrentDay(true);
                    }*/
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                } while (c.moveToNext());
            }
            c.close();
            if (lessonsOfDay.size() > 0)
                lessonsOfWeek.put(i, lessonsOfDay);
        }

        db.close();

        return lessonsOfWeek;

    }

    /**
     * Додає новий запис в базу даних
     *
     * @param lesson - дані для запису
     */
    public void putLessonIntoDB(Lesson lesson) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put(Constants.TEACHER_NAME, lesson.getTeacherName());
        cv.put(Constants.LESSON_NAME, lesson.getLessonName());
        cv.put(Constants.LESSON_NUMBER, lesson.getLessonNumber());
        cv.put(Constants.LESSON_ROOM, lesson.getLessonRoom());
        cv.put(Constants.LESSON_TYPE, lesson.getLessonType());
        cv.put(Constants.DAY_NUMBER, lesson.getDayNumber());
        cv.put(Constants.LESSON_WEEK, lesson.getLessonWeek());
        cv.put(Constants.TIME_END, lesson.getTimeEnd());
        cv.put(Constants.TIME_START, lesson.getTimeStart());

        db.insert(Constants.TABLE_NAME, null, cv);
        db.close();
    }

    /**
     * Видаляє всі записи з бази даних
     */
    public void clearDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Constants.TABLE_NAME, null, null);
        db.close();
    }
}


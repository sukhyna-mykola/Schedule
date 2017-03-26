package com.mykola.schedule.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mykola.schedule.data.database.DBHelper;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mykola on 14.03.17.
 */

public class DBManager {

    private DBHelper dbHelper;

    public DBManager(Context context){
        dbHelper = new DBHelper(context);
    }

    /**
     * Зчитує всі предмети по номеру тижня
     * @param weekNumber = номер тижня
     * @return HashMap предметів, де key-номер дня в тижні, value-список предметів в цей день
     */
    public HashMap<Integer, List<LessonDTO>> readLessonsFromDB(int weekNumber) {

        HashMap<Integer, List<LessonDTO>> lessonsOfWeek = new HashMap<>();
        List<LessonDTO> lessonsOfDay;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        for (int i = 1; i <= 6; i++) {

            lessonsOfDay = new ArrayList<>();

            Cursor c = db.query(Constants.TABLE_NAME, null, Constants.DAY_NUMBER + "=? AND " + Constants.LESSON_WEEK + "=?",
                    new String[]{String.valueOf(i), String.valueOf(weekNumber)}, null, null, null);


            if (c.moveToFirst()) {

                int dayColIndex = c.getColumnIndex(Constants.DAY_NUMBER);
                int nameColIndex = c.getColumnIndex(Constants.LESSON_NAME);
                int teacherlColIndex = c.getColumnIndex(Constants.TEACHER_NAME);
                int typeColIndex = c.getColumnIndex(Constants.LESSON_TYPE);
                int roomColIndex = c.getColumnIndex(Constants.LESSON_ROOM);
                int numberColIndex = c.getColumnIndex(Constants.LESSON_NUMBER);
                int weekColIndex = c.getColumnIndex(Constants.LESSON_WEEK);

                do {

                    String name = c.getString(nameColIndex);
                    String type = c.getString(typeColIndex);
                    String teacher = c.getString(teacherlColIndex);
                    String room = c.getString(roomColIndex);
                    String number = c.getString(numberColIndex);
                    String day = c.getString(dayColIndex);
                    String week = c.getString(weekColIndex);

                    LessonDTO lesson = new LessonDTO(name, type, teacher, room, number, day, week);
                    lessonsOfDay.add(lesson);

                } while (c.moveToNext());
            }
            c.close();
            if (lessonsOfDay.size() > 0){
                Collections.sort(lessonsOfDay);
                lessonsOfWeek.put(i, lessonsOfDay);
            }
        }

        db.close();

        return lessonsOfWeek;

    }

    /**
     * Додає новий запис в базу даних
     *
     * @param lesson - дані для запису
     */
    public void putLessonIntoDB(LessonDTO lesson) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put(Constants.TEACHER_NAME, lesson.getTeacherName());
        cv.put(Constants.LESSON_NAME, lesson.getLessonName());
        cv.put(Constants.LESSON_NUMBER, lesson.getLessonNumber());
        cv.put(Constants.LESSON_ROOM, lesson.getLessonRoom());
        cv.put(Constants.LESSON_TYPE, lesson.getLessonType());
        cv.put(Constants.DAY_NUMBER, lesson.getDayNumber());
        cv.put(Constants.LESSON_WEEK, lesson.getLessonWeek());

        db.insert(Constants.TABLE_NAME, null, cv);
        db.close();
    }

    /**
     * Видаляє всі записи з бази даних
     */
    public void clearDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, null, null);
        db.close();
    }

    /**
     * Видаляє запис з бази даних по фільтру
     */
    public void removeLessonFromDB(LessonDTO lesson) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_NAME,Constants.LESSON_WEEK+"=? AND "+Constants.DAY_NUMBER +
                "=? AND "+Constants.LESSON_NUMBER+"=?",new String[]{lesson.getLessonWeek(),lesson.getDayNumber(),lesson.getLessonNumber()});
        db.close();
    }

    /**
     * Оновлює запис в базі по фільтру
     * @param lesson
     */
    public void updateLessonInDB(LessonDTO lesson) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put(Constants.TEACHER_NAME, lesson.getTeacherName());
        cv.put(Constants.LESSON_NAME, lesson.getLessonName());
        cv.put(Constants.LESSON_NUMBER, lesson.getLessonNumber());
        cv.put(Constants.LESSON_ROOM, lesson.getLessonRoom());
        cv.put(Constants.LESSON_TYPE, lesson.getLessonType());
        cv.put(Constants.DAY_NUMBER, lesson.getDayNumber());
        cv.put(Constants.LESSON_WEEK, lesson.getLessonWeek());

        db.update(Constants.TABLE_NAME,cv,Constants.LESSON_WEEK+"=? AND "+Constants.DAY_NUMBER +
                "=? AND "+Constants.LESSON_NUMBER+"=?",new String[]{lesson.getLessonWeek(),lesson.getDayNumber(),lesson.getLessonNumber()});
        db.close();
    }
}

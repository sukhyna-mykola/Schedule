package com.mykola.schedule.data.managers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mykola.schedule.data.database.DBHelper;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.mykola.schedule.data.database.DBHelper.DAY_NUMBER;
import static com.mykola.schedule.data.database.DBHelper.LESSON_NAME;
import static com.mykola.schedule.data.database.DBHelper.LESSON_NUMBER;
import static com.mykola.schedule.data.database.DBHelper.LESSON_ROOM;
import static com.mykola.schedule.data.database.DBHelper.LESSON_TYPE;
import static com.mykola.schedule.data.database.DBHelper.LESSON_WEEK;
import static com.mykola.schedule.data.database.DBHelper.TABLE_NAME;
import static com.mykola.schedule.data.database.DBHelper.TEACHER_NAME;

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

        for (int i = 1; i <= Constants.DAYS_NUMBER; i++) {

            lessonsOfDay = new ArrayList<>();

            Cursor c = db.query(TABLE_NAME, null, DAY_NUMBER + "=? AND " + LESSON_WEEK + "=?",
                    new String[]{String.valueOf(i), String.valueOf(weekNumber)}, null, null, null);


            if (c.moveToFirst()) {

                int dayColIndex = c.getColumnIndex(DAY_NUMBER);
                int nameColIndex = c.getColumnIndex(LESSON_NAME);
                int teacherlColIndex = c.getColumnIndex(TEACHER_NAME);
                int typeColIndex = c.getColumnIndex(LESSON_TYPE);
                int roomColIndex = c.getColumnIndex(LESSON_ROOM);
                int numberColIndex = c.getColumnIndex(LESSON_NUMBER);
                int weekColIndex = c.getColumnIndex(LESSON_WEEK);

                do {

                    String name = c.getString(nameColIndex);
                    String type = c.getString(typeColIndex);
                    String teacher = c.getString(teacherlColIndex);
                    String room = c.getString(roomColIndex);
                    int number = c.getInt(numberColIndex);
                    int day = c.getInt(dayColIndex);
                    int week = c.getInt(weekColIndex);

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

        cv.put(TEACHER_NAME, lesson.getTeacherName());
        cv.put(LESSON_NAME, lesson.getLessonName());
        cv.put(LESSON_NUMBER, lesson.getLessonNumber());
        cv.put(LESSON_ROOM, lesson.getLessonRoom());
        cv.put(LESSON_TYPE, lesson.getLessonType());
        cv.put(DAY_NUMBER, lesson.getDayNumber());
        cv.put(LESSON_WEEK, lesson.getLessonWeek());

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    /**
     * Видаляє всі записи з бази даних
     */
    public void clearDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    /**
     * Видаляє запис з бази даних по фільтру
     */
    public void removeLessonFromDB(LessonDTO lesson) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TABLE_NAME,LESSON_WEEK+"=? AND "+DAY_NUMBER +
                "=? AND "+LESSON_NUMBER+"=?",new String[]{String.valueOf(lesson.getLessonWeek()),String.valueOf(lesson.getDayNumber()), String.valueOf(lesson.getLessonNumber())});
        db.close();
    }

    /**
     * Оновлює запис в базі по фільтру
     * @param lesson
     */
    public void updateLessonInDB(LessonDTO lesson) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put(TEACHER_NAME, lesson.getTeacherName());
        cv.put(LESSON_NAME, lesson.getLessonName());
        cv.put(LESSON_NUMBER, lesson.getLessonNumber());
        cv.put(LESSON_ROOM, lesson.getLessonRoom());
        cv.put(LESSON_TYPE, lesson.getLessonType());
        cv.put(DAY_NUMBER, lesson.getDayNumber());
        cv.put(LESSON_WEEK, lesson.getLessonWeek());

        db.update(TABLE_NAME,cv,LESSON_WEEK+"=? AND "+DAY_NUMBER +
                "=? AND "+LESSON_NUMBER+"=?",new String[]{String.valueOf(lesson.getLessonWeek()),String.valueOf(lesson.getDayNumber()), String.valueOf(lesson.getLessonNumber())});
        db.close();
    }
}

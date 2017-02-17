package com.mykola.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mykola on 17.02.17.
 */

public class ScheduleManager {

    private ArrayList<ArrayList<Lesson>> lessons;
    private int weekNumber;
    private int currentWeek;

    private DBHelper dbHelper;
    private SharedPreferences sPref;
    private Context context;

    private String groupName;

    private static ScheduleManager manager;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public ArrayList<ArrayList<Lesson>> getLessons() {
        return lessons;
    }


    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }


    private ScheduleManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        setGroupName(readGroupName());
    }

    public static ScheduleManager get(Context context) {
        if (manager == null)
            manager = new ScheduleManager(context);
        return manager;
    }

    public void logOut() {
        clearDB();
        saveStatusLogin(false);
    }


    public void setWeek(int week) {
        setWeekNumber(week);
        if (week == Constants.FIRST_WEEK)
            saveParityWeek(false);
        else saveParityWeek(true);
    }

    public void logIn(String name, List<Lesson> lessons) {
        for (Lesson lessson : lessons) {
            putDataToDB(lessson);
        }
        saveGroupName(name);
        setGroupName(name);
        saveStatusLogin(true);
    }

    public void loadSchedule() {
        loadNumberWeek();
        readDataFromDB();
    }

    public void showScheduleOfWeek() {
        readDataFromDB();
    }

    private void clearDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, null, null);
        db.close();
    }

    private void readDataFromDB() {
        initLessons();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(Constants.TABLE_NAME, null, Constants.LESSON_WEEK + "=?", new String[]{String.valueOf(weekNumber)}, null, null, null);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        int day = calendar.get(Calendar.DAY_OF_WEEK) - 1;

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

                Lesson lesson = new Lesson(c.getString(nameColIndex), c.getString(typeColIndex),
                        c.getString(teacherlColIndex), c.getString(roomColIndex), c.getString(numberColIndex),
                        c.getString(dayColIndex), c.getString(weekColIndex), c.getString(startColIndex), c.getString(endlIndex));
                try {
                    Date start = df.parse(lesson.getTimeStart());
                    Date end = df.parse(lesson.getTimeEnd());
                    Date thisTime = df.parse(df.format(calendar.getTime()));

                    if ((day == Integer.parseInt(lesson.getDayNumber())) && (Integer.parseInt(lesson.getLessonWeek()) == currentWeek)) {
                        if (thisTime.after(start) && thisTime.before(end)) {
                            lesson.setCurrentLesson(true);
                        }

                        lesson.setCurrentDay(true);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                lessons.get(c.getInt(dayColIndex) - 1).add(lesson);

            } while (c.moveToNext());
        }

        c.close();
        db.close();


    }

    private void saveStatusLogin(boolean statusLogin) {
        sPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.LOGIN_KEY, statusLogin);
        ed.commit();

    }

    private void saveGroupName(String name) {
        sPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.GROUP_KEY, name);
        ed.commit();
    }

    public boolean checkStatusLogin() {
        sPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sPref.getBoolean(Constants.LOGIN_KEY, false);
    }


    private void saveParityWeek(boolean parity) {
        sPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.PARITY_WEEK_KEY, parity);
        ed.commit();

    }


    private void loadNumberWeek() {
        boolean parity = checkParityWeek();
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_YEAR);
        if (parity) {
            if (week % 2 == 0) {
                weekNumber = Constants.SECOND_WEEK;
            } else weekNumber = Constants.FIRST_WEEK;
        } else {
            if (week % 2 == 0) {
                weekNumber = Constants.FIRST_WEEK;
            } else weekNumber = Constants.SECOND_WEEK;
        }
        currentWeek = weekNumber;
    }


    private void putDataToDB(Lesson lesson) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

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
    }

    private void initLessons() {
        lessons = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lessons.add(new ArrayList<Lesson>());
        }
    }

    private boolean checkParityWeek() {
        sPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sPref.getBoolean(Constants.PARITY_WEEK_KEY, false);
    }

    private String readGroupName() {
        sPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.GROUP_KEY, "");
    }
}

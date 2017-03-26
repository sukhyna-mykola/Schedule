package com.mykola.schedule.utils;

import java.util.HashMap;

/**
 * Created by mykola on 16.01.17.
 */

public final class Constants {

    public static final String SHARED_PREFERENCES = "schedule.preferences";
    public static final String LOGIN_KEY = "LOGIN_KEY";
    public static final String GROUP_KEY = "GROUP_KEY";


    public static final String DAY_NUMBER = "day_number";
    public static final String LESSON_NAME = "lesson_name";
    public static final String LESSON_NUMBER = "lesson_number";
    public static final String LESSON_TYPE = "lesson_type";
    public static final String LESSON_ROOM = "lesson_room";
    public static final String TEACHER_NAME = "teacher_name";
    public static final String LESSON_WEEK = "lesson_week";

    public static final String DAY_NUMBER_KEY = "DAY_NUMBER_KEY";

    public static final String DB_NAME = "LESSONS_TABLE";
    public static final String TABLE_NAME = "TABLE_NAME";

    public static final String INTEGER = "integer";
    public static final String TEXT = "text";

    public static final int FIRST_WEEK = 1;
    public static final int SECOND_WEEK = 2;

    public static final String PARITY_WEEK_KEY = "PARITY_WEEK_KEY";

    public static final String LECTURE_POSITION = "LECTURE_POSITION";
    public static final String DIALOG_PROGRESS = "DOALOG_PROGRESS";

    public static final int LESSONS_NUMBER = 5;
    public static final int DAYS_NUMBER = 6;

    public static final HashMap<Integer, String> times = new HashMap<>();

    static {
        times.put(1, "8:30-10:05");
        times.put(2, "10:25-12:00");
        times.put(3, "12:20-13:55");
        times.put(4, "14:15-15:50");
        times.put(5, "16:10-17:45");

    }
}


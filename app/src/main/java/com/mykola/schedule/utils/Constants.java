package com.mykola.schedule.utils;


import java.util.HashMap;

public final class Constants {

    public static final int FIRST_WEEK = 1;
    public static final int SECOND_WEEK = 2;

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


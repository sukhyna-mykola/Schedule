package com.mykola.schedule;

/**
 * Created by mykola on 16.01.17.
 */

public class Lesson {

    private String name;
    private String type;
    private String teatherName;
    private String room;

    private int number;
    private int day;
    private int week;

    public Lesson(String name, String type, String teatherName, String room, int number, int day, int week) {
        this.name = name;
        this.type = type;
        this.teatherName = teatherName;
        this.room = room;
        this.number = number;
        this.day = day;
        this.week = week;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getTeatherName() {
        return teatherName;
    }

    public String getRoom() {
        return room;
    }

    public int getNumber() {
        return number;
    }

    public int getDay() {
        return day;
    }

    public int getWeek() {
        return week;
    }




}

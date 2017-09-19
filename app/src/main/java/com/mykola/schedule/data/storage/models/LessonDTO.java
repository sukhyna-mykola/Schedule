package com.mykola.schedule.data.storage.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LessonDTO implements Comparable<LessonDTO> {

    @SerializedName("day_number")
    @Expose
    private int dayNumber;
    @SerializedName("lesson_name")
    @Expose
    private String lessonName;
    @SerializedName("lesson_number")
    @Expose
    private int lessonNumber;
    @SerializedName("lesson_room")
    @Expose
    private String lessonRoom;
    @SerializedName("lesson_type")
    @Expose
    private String lessonType;
    @SerializedName("teacher_name")
    @Expose
    private String teacherName;
    @SerializedName("lesson_week")
    @Expose
    private int lessonWeek;


    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getLessonName() {
        return lessonName;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonRoom() {
        return lessonRoom;
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public int getLessonWeek() {
        return lessonWeek;
    }

    public void setLessonWeek(int lessonWeek) {
        this.lessonWeek = lessonWeek;
    }


    public LessonDTO(String lessonName, String lessonType, String teacherName, String lessonRoom,
                     int lessonNumber, int dayNumber, int lessonWeek) {
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.teacherName = teacherName;
        this.lessonRoom = lessonRoom;
        this.lessonNumber = lessonNumber;
        this.lessonWeek = lessonWeek;
        this.dayNumber = dayNumber;
    }


    @Override
    public int compareTo(LessonDTO o) {
        return lessonNumber - o.getLessonNumber();
    }
}
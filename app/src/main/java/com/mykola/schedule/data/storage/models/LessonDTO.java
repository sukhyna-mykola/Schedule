package com.mykola.schedule.data.storage.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mykola on 31.01.17.
 */

public class LessonDTO {


    @SerializedName("day_number")
    @Expose
    private String dayNumber;

    @SerializedName("lesson_name")
    @Expose
    private String lessonName;

    @SerializedName("lesson_number")
    @Expose
    private String lessonNumber;
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
    private String lessonWeek;
    @SerializedName("time_start")
    @Expose
    private String timeStart;
    @SerializedName("time_end")
    @Expose
    private String timeEnd;


    private boolean currentLesson;
    private boolean currentDay;

    public boolean isCurrentLesson() {
        return currentLesson;
    }

    public void setCurrentLesson(boolean currentLesson) {
        this.currentLesson = currentLesson;
    }

    public boolean isCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(boolean currentDay) {
        this.currentDay = currentDay;
    }


    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }


    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }


    public String getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(String lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

    public String getLessonRoom() {
        return lessonRoom;
    }

    public void setLessonRoom(String lessonRoom) {
        this.lessonRoom = lessonRoom;
    }

    public String getLessonType() {
        return lessonType;
    }

    public void setLessonType(String lessonType) {
        this.lessonType = lessonType;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getLessonWeek() {
        return lessonWeek;
    }

    public void setLessonWeek(String lessonWeek) {
        this.lessonWeek = lessonWeek;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }


    public LessonDTO(String lessonName, String lessonType, String teacherName, String lessonRoom,
                     String lessonNumber, String dayNumber, String lessonWeek,
                     String startTime, String endTime) {
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.teacherName = teacherName;
        this.lessonRoom = lessonRoom;
        this.lessonNumber = lessonNumber;
        this.lessonWeek = lessonWeek;
        this.dayNumber = dayNumber;
        this.timeStart = startTime;
        this.timeEnd = endTime;
    }
}
package com.mykola.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mykola on 31.01.17.
 */

public class Lesson {

    @SerializedName("lesson_id")
    @Expose
    private String lessonId;
    @SerializedName("group_id")
    @Expose
    private String groupId;
    @SerializedName("day_number")
    @Expose
    private String dayNumber;
    @SerializedName("day_name")
    @Expose
    private String dayName;
    @SerializedName("lesson_name")
    @Expose
    private String lessonName;
    @SerializedName("lesson_full_name")
    @Expose
    private String lessonFullName;
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
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("teachers")
    @Expose
    private List<Teacher> teachers = null;
    @SerializedName("rooms")
    @Expose
    private List<Room> rooms = null;

    public String getLessonId() {
        return lessonId;
    }

    public void setLessonId(String lessonId) {
        this.lessonId = lessonId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(String dayNumber) {
        this.dayNumber = dayNumber;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getLessonFullName() {
        return lessonFullName;
    }

    public void setLessonFullName(String lessonFullName) {
        this.lessonFullName = lessonFullName;
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

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Lesson(String lessonName, String lessonType, String teacherName, String lessonRoom, String lessonNumber, String dayNumber, String lessonWeek) {
        this.lessonName = lessonName;
        this.lessonType = lessonType;
        this.teacherName = teacherName;
        this.lessonRoom = lessonRoom;
        this.lessonNumber = lessonNumber;
        this.lessonWeek = lessonWeek;
        this.dayNumber = dayNumber;
    }
}
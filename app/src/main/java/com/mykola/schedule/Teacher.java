package com.mykola.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mykola on 01.02.17.
 */

public class Teacher {
    @SerializedName("teacher_id")
    @Expose
    private String teacherId;
    @SerializedName("teacher_name")
    @Expose
    private String teacherName;
    @SerializedName("teacher_full_name")
    @Expose
    private String teacherFullName;
    @SerializedName("teacher_short_name")
    @Expose
    private String teacherShortName;
    @SerializedName("teacher_url")
    @Expose
    private String teacherUrl;
    @SerializedName("teacher_rating")
    @Expose
    private String teacherRating;

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getTeacherFullName() {
        return teacherFullName;
    }

    public void setTeacherFullName(String teacherFullName) {
        this.teacherFullName = teacherFullName;
    }

    public String getTeacherShortName() {
        return teacherShortName;
    }

    public void setTeacherShortName(String teacherShortName) {
        this.teacherShortName = teacherShortName;
    }

    public String getTeacherUrl() {
        return teacherUrl;
    }

    public void setTeacherUrl(String teacherUrl) {
        this.teacherUrl = teacherUrl;
    }

    public String getTeacherRating() {
        return teacherRating;
    }

    public void setTeacherRating(String teacherRating) {
        this.teacherRating = teacherRating;
    }

}
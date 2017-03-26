package com.mykola.schedule.data.storage.models;

/**
 * Created by mykola on 24.03.17.
 */

public class EditLecture {

    private LessonDTO lesson;

    private int dayNumber;
    private int weekNumber;
    private  int lessonNumber;

    public EditLecture(LessonDTO lesson, int weekNumber, int dayNumber, int lessonNumber) {
        this.lesson = lesson;
        this.dayNumber = dayNumber;
        this.weekNumber = weekNumber;
        this.lessonNumber = lessonNumber;
    }


    public LessonDTO getLesson() {
        return lesson;
    }

    public void setLesson(LessonDTO lesson) {
        this.lesson = lesson;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public int getLessonNumber() {
        return lessonNumber;
    }

    public void setLessonNumber(int lessonNumber) {
        this.lessonNumber = lessonNumber;
    }

}

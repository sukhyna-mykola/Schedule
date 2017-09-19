package com.mykola.schedule.data.managers;

import android.content.Context;

import com.mykola.schedule.data.storage.models.EditLecture;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class EditSheduleManager {

    private List<EditLecture> lessons;
    private static EditSheduleManager editManager;
    private DBManager dbManager;
    private boolean moveState;
    private LessonDTO movedLesson;
    private int week;

    public static EditSheduleManager getManager(Context context) {
        if (editManager == null)
            editManager = new EditSheduleManager(context);
        return editManager;
    }

    public List<EditLecture> getLessons() {
        return lessons;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeek() {
        return week;
    }

    private EditSheduleManager(Context context) {
        dbManager = new DBManager(context);

    }

    public DBManager getDbManager() {
        return dbManager;
    }


    public void genareteLessonsByWeek() {
        HashMap<Integer, List<LessonDTO>> lessonsOfWeek = dbManager.readLessonsFromDB(week);
        generateLessons(lessonsOfWeek);
    }

    private void generateLessons(HashMap<Integer, List<LessonDTO>> realLessons) {
        lessons = new ArrayList<>();

        for (int i = 0; i < Constants.LESSONS_NUMBER; i++) {
            for (int j = 0; j < Constants.DAYS_NUMBER; j++) {
                lessons.add(new EditLecture(null, week, j + 1, i + 1));

                if (realLessons.containsKey(j + 1)) {
                    List<LessonDTO> tmpLessons = realLessons.get(j + 1);
                    for (LessonDTO tmpLesson : tmpLessons) {
                        if (tmpLesson.getLessonNumber() == i + 1) {
                            lessons.get(lessons.size() - 1).setLesson(tmpLesson);
                            break;
                        }
                    }
                }
            }
        }
    }

    public boolean isMoveState() {
        return moveState;
    }

    public void startMove(int position) {
        moveState = true;
        movedLesson = lessons.get(position).getLesson();
        lessons.get(position).setLesson(null);

    }


    public void endMove(int position) {

        dbManager.removeLessonFromDB(movedLesson);

        EditLecture newLecture = lessons.get(position);
        movedLesson.setDayNumber(newLecture.getDayNumber());
        movedLesson.setLessonWeek(newLecture.getWeekNumber());
        movedLesson.setLessonNumber(newLecture.getLessonNumber());

        dbManager.putLessonIntoDB(movedLesson);
        lessons.get(position).setLesson(movedLesson);

        movedLesson = null;
        moveState = false;
    }

    public void removeLesson(EditLecture lecture){
        lecture.setLesson(null);
    }
}

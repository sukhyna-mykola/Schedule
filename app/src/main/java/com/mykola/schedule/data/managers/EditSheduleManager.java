package com.mykola.schedule.data.managers;

import android.content.Context;

import com.mykola.schedule.data.storage.models.EditLecture;
import com.mykola.schedule.data.storage.models.LessonDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mykola on 24.03.17.
 */

public class EditSheduleManager {

    private List<EditLecture> lessons;
    private static EditSheduleManager editManager;
    private DBManager dbManager;
    private Context context;
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
        this.context = context;
        week = 1;
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
       /* for (int i = 0; i < 6; i++) {
            if (manager.getLessons().containsKey(i)) {

                List<LessonDTO> tmpLessons = manager.getLessons().get(i);

                for (int j = 0; j < 5; j++) {
                    lessons.add(new EditLecture(null, 1, i, j));

                    for (LessonDTO tmpLesson : tmpLessons) {
                        if (Integer.parseInt(tmpLesson.getLessonNumber()) == j) {
                            lessons.get(lessons.size() - 1).setLesson(tmpLesson);
                            break;
                        }
                    }
                }
            } else {
                for (int j = 0; j < 5; j++) {
                    lessons.add(new EditLecture(null, 1, i, j));
                }
            }
        }*/

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 6; j++) {
                lessons.add(new EditLecture(null, week, j + 1, i + 1));

                if (realLessons.containsKey(j + 1)) {
                    List<LessonDTO> tmpLessons = realLessons.get(j + 1);
                    for (LessonDTO tmpLesson : tmpLessons) {
                        if (Integer.parseInt(tmpLesson.getLessonNumber()) == i + 1) {
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

    public void setMoveState(boolean moveState) {
        this.moveState = moveState;
    }
    public LessonDTO getMovedLesson() {
        return movedLesson;
    }


    public void runMove(int position) {
        moveState = true;
        movedLesson = lessons.get(position).getLesson();
        lessons.get(position).setLesson(null);

    }


    public void endMove(int position) {

        dbManager.removeLessonFromDB(movedLesson);

        EditLecture newLecture = lessons.get(position);
        movedLesson.setDayNumber(String.valueOf(newLecture.getDayNumber()));
        movedLesson.setLessonWeek(String.valueOf(newLecture.getWeekNumber()));
        movedLesson.setLessonNumber(String.valueOf(newLecture.getLessonNumber()));

        dbManager.putLessonIntoDB(movedLesson);
        lessons.get(position).setLesson(movedLesson);

        movedLesson = null;
        moveState = false;
    }

    public void removeLesson(EditLecture lecture){
        lecture.setLesson(null);
    }
}

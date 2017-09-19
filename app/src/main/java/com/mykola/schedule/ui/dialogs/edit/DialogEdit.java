package com.mykola.schedule.ui.dialogs.edit;

import android.app.Dialog;
import android.os.Bundle;

import com.mykola.schedule.R;
import com.mykola.schedule.data.storage.models.EditLecture;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.ui.views.LessonDialogView;
import com.mykola.schedule.utils.Constants;


public class DialogEdit extends DialogLesson {


    public static DialogLesson newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(LECTURE_POSITION, position);
        DialogLesson fragment = new DialogEdit();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog createDialog(int position) {
        final EditLecture lecture = manager.getLessons().get(position);
        LessonDialogView lessonDialog = new LessonDialogView(getActivity(), lecture,getString(R.string.edit_lesson) , R.drawable.ic_edit_black_24dp){

            @Override
            public void actionPositiveButton(EditLecture lecture) {
                String name = getInputLessonName().getText().toString();
                String type = getInputLessonType().getText().toString();
                String teacher = getInputLessonTeather().getText().toString();
                String room = getInputLessonRoom().getText().toString() + '-' + getInputLessonBuilding().getText().toString();
                int number = lecture.getLessonNumber();
                int day = lecture.getDayNumber();
                int week = lecture.getWeekNumber();

                LessonDTO lesson = new LessonDTO(name, type, teacher, room, number, day, week);
                lecture.setLesson(lesson);
                manager.getDbManager().updateLessonInDB(lesson);
                update.update();
            }
        };
        return lessonDialog.getDialog();
    }


}

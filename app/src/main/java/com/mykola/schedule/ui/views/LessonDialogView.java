package com.mykola.schedule.ui.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.mykola.schedule.R;
import com.mykola.schedule.data.storage.models.EditLecture;
import com.mykola.schedule.utils.Constants;

import static com.mykola.schedule.R.id.lesson_building;

public abstract class LessonDialogView {

    private EditText inputLessonName, inputLessonTeather, inputLessonRoom, inputLessonType, inputLessonBuilding;
    private TextView infoLessonNumber, infoLessonDayWeek, infoLessonTime;
    private Dialog dialog;

    private View v;

    public View getV() {
        return v;
    }

    public EditText getInputLessonName() {
        return inputLessonName;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public EditText getInputLessonTeather() {
        return inputLessonTeather;
    }

    public EditText getInputLessonRoom() {
        return inputLessonRoom;
    }

    public EditText getInputLessonType() {
        return inputLessonType;
    }

    public EditText getInputLessonBuilding() {
        return inputLessonBuilding;
    }

    public LessonDialogView(Context context, final EditLecture lecture,String title, int icon) {
        v = LayoutInflater.from(context).inflate(R.layout.dialog_lesson, null);
        inputLessonName = (EditText) v.findViewById(R.id.lesson_name);
        inputLessonBuilding = (EditText) v.findViewById(lesson_building);
        inputLessonRoom = (EditText) v.findViewById(R.id.lesson_room);
        inputLessonType = (EditText) v.findViewById(R.id.lesson_type);
        inputLessonTeather = (EditText) v.findViewById(R.id.lesson_teather);

        infoLessonDayWeek = (TextView) v.findViewById(R.id.lesson_day_week);
        infoLessonNumber = (TextView) v.findViewById(R.id.lesson_number);
        infoLessonTime = (TextView) v.findViewById(R.id.lesson_time);


        String[] daysName = context.getResources().getStringArray(R.array.days_of_week);
        String day = daysName[lecture.getDayNumber() - 1];
        int week  = lecture.getWeekNumber();
        int lessonNumber = lecture.getLessonNumber();
        String time = Constants.times.get(lecture.getLessonNumber());

        infoLessonDayWeek.setText( context.getString(R.string.day_week,day,week));
        infoLessonNumber.setText(context.getString(R.string.number_lesson,lessonNumber));
        infoLessonTime.setText(context.getString(R.string.time_lesson, time));

        if (lecture.getLesson() != null) {
            inputLessonName.setText(lecture.getLesson().getLessonName());
            inputLessonTeather.setText(lecture.getLesson().getTeacherName());
            inputLessonType.setText(lecture.getLesson().getLessonType());
            try {
                String[] room = lecture.getLesson().getLessonRoom().split("-");
                inputLessonRoom.setText(room[0]);
                inputLessonBuilding.setText(room[1]);
            } catch (Exception e) {

            }

        }
        dialog = new AlertDialog.Builder(context)
                .setView(v)
                .setTitle(title)
                .setIcon(icon)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        actionPositiveButton(lecture);
                    }
                })
                .create();
    }


    public abstract void actionPositiveButton(EditLecture lecture);
}

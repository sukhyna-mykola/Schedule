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

/**
 * Created by mykola on 24.03.17.
 */

public abstract class LessonDialogView {
    private Context context;

    private EditText inputLessonName, inputLessonTeather, inputLessonRoom, inputLessonType, inputLessonBuilding;
    private TextView infoLessonNumber, infoLessonDay, infoLessonWeek, infoLessonTime;
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
        this.context = context;
        v = LayoutInflater.from(context).inflate(R.layout.dialog_lesson, null);
        inputLessonName = (EditText) v.findViewById(R.id.lesson_name);
        inputLessonBuilding = (EditText) v.findViewById(lesson_building);
        inputLessonRoom = (EditText) v.findViewById(R.id.lesson_room);
        inputLessonType = (EditText) v.findViewById(R.id.lesson_type);
        inputLessonTeather = (EditText) v.findViewById(R.id.lesson_teather);

        infoLessonDay = (TextView) v.findViewById(R.id.lesson_day);
        infoLessonNumber = (TextView) v.findViewById(R.id.lesson_number);
        infoLessonTime = (TextView) v.findViewById(R.id.lesson_time);
        infoLessonWeek = (TextView) v.findViewById(R.id.lesson_week);


        String[] daysName = context.getResources().getStringArray(R.array.days_of_week);
        infoLessonDay.setText(daysName[lecture.getDayNumber() - 1]);
        infoLessonWeek.setText(" , " + String.valueOf(lecture.getWeekNumber()) + context.getString(R.string.th_week));
        infoLessonNumber.setText(context.getString(R.string.number_lesson) + String.valueOf(lecture.getLessonNumber()));
        infoLessonTime.setText(context.getString(R.string.time_lesson) + Constants.times.get(lecture.getLessonNumber()));

        if (lecture.getLesson() != null) {
            inputLessonName.setText(lecture.getLesson().getLessonName());
            inputLessonTeather.setText(lecture.getLesson().getTeacherName());
            inputLessonType.setText(lecture.getLesson().getLessonType());
            try {
                String[] romm = lecture.getLesson().getLessonRoom().split("-");
                inputLessonRoom.setText(romm[0]);
                inputLessonBuilding.setText(romm[1]);
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

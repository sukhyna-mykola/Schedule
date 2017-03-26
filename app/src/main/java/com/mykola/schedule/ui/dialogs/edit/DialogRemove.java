package com.mykola.schedule.ui.dialogs.edit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.mykola.schedule.R;
import com.mykola.schedule.data.storage.models.EditLecture;
import com.mykola.schedule.utils.Constants;

/**
 * Created by mykola on 26.03.17.
 */

public class DialogRemove extends DialogLesson {

    public static DialogLesson newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(Constants.LECTURE_POSITION, position);
        DialogLesson fragment = new DialogRemove();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog createDialog(int position) {
        final EditLecture lecture = manager.getLessons().get(position);
        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.remove)
                .setIcon(R.drawable.ic_clear_black_24dp)
                .setMessage(R.string.realy)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.getDbManager().removeLessonFromDB(lecture.getLesson());
                        manager.removeLesson(lecture);
                        update.update();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .create();
        return dialog;
    }


}

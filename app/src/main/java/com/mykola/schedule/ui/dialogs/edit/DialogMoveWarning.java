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

public class DialogMoveWarning extends DialogLesson {


    public static DialogLesson newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(Constants.LECTURE_POSITION, position);
        DialogLesson fragment = new DialogMoveWarning();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Dialog createDialog(final int position) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.warning)
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setMessage(R.string.alredy_exist_lesson)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        manager.endMove(position);
                        update.update();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .create();

    }


}

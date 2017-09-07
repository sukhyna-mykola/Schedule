package com.mykola.schedule.ui.dialogs.edit;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mykola.schedule.data.managers.EditSheduleManager;
import com.mykola.schedule.data.storage.models.EditLecture;
import com.mykola.schedule.ui.dialogs.edit.CallbackDialog;
import com.mykola.schedule.utils.Constants;


public abstract class DialogLesson extends DialogFragment {
    protected EditSheduleManager manager;
    protected CallbackDialog update;

    protected static final String LECTURE_POSITION = "LECTURE_POSITION";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        manager = EditSheduleManager.getManager(getActivity());
        final int position = getArguments().getInt(LECTURE_POSITION);

        return createDialog(position);
    }

    public void registerCallback(CallbackDialog callbackDialog) {
        this.update = callbackDialog;
    }

    public abstract Dialog createDialog(int position);
}

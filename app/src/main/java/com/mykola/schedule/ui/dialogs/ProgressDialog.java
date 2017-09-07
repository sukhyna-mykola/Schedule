package com.mykola.schedule.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.mykola.schedule.R;


public class ProgressDialog extends DialogFragment {

    public static final String DIALOG_PROGRESS = "DOALOG_PROGRESS";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        android.app.ProgressDialog dialog = new android.app.ProgressDialog(getActivity());

        dialog.setCancelable(true);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}

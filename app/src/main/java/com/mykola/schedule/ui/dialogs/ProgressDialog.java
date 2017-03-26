package com.mykola.schedule.ui.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.mykola.schedule.R;

/**
 * Created by mykola on 23.03.17.
 */

public class ProgressDialog extends DialogFragment {

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

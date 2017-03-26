package com.mykola.schedule.ui.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mykola.schedule.R;
import com.mykola.schedule.ui.fragments.EditScheduleFragment;

public class EditScheduleActivity extends FragmentsContainerActivity{


    @Override
    public Fragment getFragment() {
        return EditScheduleFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}

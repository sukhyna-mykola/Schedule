package com.mykola.schedule.ui.activitys;

import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;

import com.mykola.schedule.ui.fragments.EditScheduleFragment;

public class EditScheduleActivity extends FragmentsContainerActivity {

    @Override
    public Fragment getFragment() {
        int weekNumber = getIntent().getIntExtra(EditScheduleFragment.WEEK_NUMBER, 1);
        return EditScheduleFragment.newInstance(weekNumber);
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}

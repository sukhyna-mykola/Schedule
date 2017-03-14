package com.mykola.schedule.ui.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mykola.schedule.ui.fragments.ScheduleFragment;


public class ScheduleActivity extends FragmentsContainerActivity {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Fragment getFragment() {
        return ScheduleFragment.newInstance();
    }
}

package com.mykola.schedule.ui.activitys;

import android.content.Intent;
import android.support.v4.app.Fragment;

import com.mykola.schedule.ui.fragments.EditScheduleFragment;
import com.mykola.schedule.ui.fragments.ScheduleFragment;


public class ScheduleActivity extends FragmentsContainerActivity implements ScheduleFragment.EditSheduleCallback {
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Fragment getFragment() {
        return ScheduleFragment.newInstance();
    }

    @Override
    public void startEditSheduleActivity(int week) {
        Intent intent = new Intent(this, EditScheduleActivity.class);
        intent.putExtra(EditScheduleFragment.WEEK_NUMBER, week);
        startActivity(intent);
    }
}

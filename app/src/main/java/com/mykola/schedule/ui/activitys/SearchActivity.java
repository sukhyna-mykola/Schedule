package com.mykola.schedule.ui.activitys;

import android.support.v4.app.Fragment;

import com.mykola.schedule.ui.fragments.SearchFragment;


public class SearchActivity extends FragmentsContainerActivity {

    @Override
    public Fragment getFragment() {
        return SearchFragment.newInstance();
    }
}

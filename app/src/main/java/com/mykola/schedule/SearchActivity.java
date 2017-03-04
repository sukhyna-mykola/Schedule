package com.mykola.schedule;

import android.support.v4.app.Fragment;


public class SearchActivity extends FragmentsContainerActivity {


    @Override
    public Fragment getFragment() {
        return SearchFragment.newInstance();
    }
}

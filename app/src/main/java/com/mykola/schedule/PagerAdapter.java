package com.mykola.schedule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by mykola on 16.01.17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumOfTabs;


    public PagerAdapter(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        return DayFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
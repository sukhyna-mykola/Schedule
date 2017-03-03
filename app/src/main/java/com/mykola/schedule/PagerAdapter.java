package com.mykola.schedule;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykola on 16.01.17.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<Integer> days;

    public PagerAdapter(FragmentManager fm, List<Integer> days) {
        super(fm);
        this.days = days;
    }

    @Override
    public Fragment getItem(int position) {
        return DayFragment.newInstance(days.get(position));
    }

    @Override
    public int getCount() {
        return days.size();
    }
}
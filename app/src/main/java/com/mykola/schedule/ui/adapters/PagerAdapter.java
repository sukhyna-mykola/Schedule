package com.mykola.schedule.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mykola.schedule.ui.fragments.DayFragment;

import java.util.List;

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
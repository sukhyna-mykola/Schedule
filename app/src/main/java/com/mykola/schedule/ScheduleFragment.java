package com.mykola.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mykola on 03.03.17.
 */

public class ScheduleFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 14;

    private PagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;

    private ScheduleManager manager;

    private Menu menu;


    public static ScheduleFragment newInstance() {

        Bundle args = new Bundle();

        ScheduleFragment fragment = new ScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        fab.setOnClickListener(this);

        manager = ScheduleManager.get(getActivity());
        if (manager.readStatusLogin() == true) {
            manager.loadSchedule();
            setSheduleView();
            setTitleFragment(manager.getGroupName());
        } else {
            showSearchScreen();
        }

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_main, menu);
        setMenuView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.first_week:
                manager.setWeekNumber(Constants.FIRST_WEEK);
                manager.loadScheduleOfWeek();

                break;
            case R.id.second_week:
                manager.setWeekNumber(Constants.SECOND_WEEK);
                manager.loadScheduleOfWeek();
                break;
        }

        setSheduleView();
        setMenuView();
        setTitleFragment(manager.getGroupName());

        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK & requestCode == REQUEST_CODE) {
            manager.loadSchedule();//завантажити розклад з бази
            setSheduleView();//відобразити розклад
            setMenuView();//відобразити меню
            setTitleFragment(manager.getGroupName());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                manager.logOut();
                showSearchScreen();
                break;
        }
    }

    private void setTitleFragment(String groupName) {
        String newTitle = getResources().getString(R.string.title, groupName);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(newTitle);
    }

    private void showSearchScreen() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void setMenuView() {
        if (manager.getWeekNumber() == Constants.FIRST_WEEK) {
            menu.getItem(0).setIcon(R.drawable.ic_looks_one_red_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_looks_two_white_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_looks_one_white_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_looks_two_red_24dp);
        }
    }

    private void setSheduleView() {
        //Тут я віднімаю 1 через те, що відлік днів відбувається з неділлі
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;

        List<Integer> days = new ArrayList<>(manager.getLessons().keySet());
        Collections.sort(days);

        String[] daysName = getResources().getStringArray(R.array.days_of_week);

        tabLayout.removeAllTabs();
        for (Integer i : days) {
            int position = i-1;
            if (position >= 0 && position < daysName.length)
                tabLayout.addTab(tabLayout.newTab().setText(daysName[position]));
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), days);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(days.indexOf(day));


    }
}

package com.mykola.schedule.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.mykola.schedule.R;
import com.mykola.schedule.data.managers.ScheduleManager;
import com.mykola.schedule.data.network.pojo.ResponceLessons;
import com.mykola.schedule.data.network.pojo.ResponceWeek;
import com.mykola.schedule.ui.activitys.SearchActivity;
import com.mykola.schedule.ui.adapters.PagerAdapter;
import com.mykola.schedule.utils.Constants;
import com.mykola.schedule.utils.Loger;
import com.mykola.schedule.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * Created by mykola on 03.03.17.
 */

public class ScheduleFragment extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 14;

    private PagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

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
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);

        manager = ScheduleManager.get(getActivity());

        if (manager.getManagerPreferences().readStatusLogin() == true) {
            manager.loadSchedule();
            setSheduleView();
            setTitleFragment(manager.getManagerPreferences().readGroupName());
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
            case R.id.week:

                if (manager.getWeekNumber() == Constants.FIRST_WEEK)
                    manager.setWeekNumber(Constants.SECOND_WEEK);
                else
                    manager.setWeekNumber(Constants.FIRST_WEEK);
                manager.loadScheduleOfWeek();
                setSheduleView();
                setMenuView();
                break;
            case R.id.search:
                manager.logOut();
                showSearchScreen();
                break;
            case R.id.update:
                getSchedule(manager.getManagerPreferences().readGroupName());
                break;

        }


        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK & requestCode == REQUEST_CODE) {
            loadSheduleFromDB();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        MenuItem weekIcon = menu.findItem(R.id.week);
        if (manager.getWeekNumber() == Constants.FIRST_WEEK) {
            if (manager.getCurrentWeek() == Constants.FIRST_WEEK)
                weekIcon.setIcon(R.drawable.ic_looks_one_red_24dp);
            else weekIcon.setIcon(R.drawable.ic_looks_one_white_24dp);
        } else {
            if (manager.getCurrentWeek() == Constants.SECOND_WEEK)
                weekIcon.setIcon(R.drawable.ic_looks_two_red_24dp);
            else weekIcon.setIcon(R.drawable.ic_looks_two_white_24dp);
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
            int position = i - 1;
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


    private void getWeek() {

        if (NetworkStatusChecker.isNetworkAvailable(getContext())) {
            // get WeekNumber
            Loger.LOG("get week");
            manager.getWeekFromServer().enqueue(new Callback<ResponceWeek>() {
                @Override
                public void onResponse(Call<ResponceWeek> call, Response<ResponceWeek> response) {
                    manager.saveWeek(response.body().getData());
                    loadSheduleFromDB();
                }

                @Override
                public void onFailure(Call<ResponceWeek> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.not_found, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    private void getSchedule(final String query) {
        if (NetworkStatusChecker.isNetworkAvailable(getContext())) {
            Loger.LOG(query);
            manager.getSheduleFromServer(query).enqueue(new Callback<ResponceLessons>() {
                @Override
                public void onResponse(Call<ResponceLessons> call, Response<ResponceLessons> response) {
                    if (response.body() != null) {
                        manager.clearDB();
                        manager.logIn(query, response.body().getData());
                        getWeek();
                    } else {
                        Toast.makeText(getActivity(), R.string.not_found, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponceLessons> call, Throwable t) {
                    Toast.makeText(getActivity(),  R.string.not_found, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }


    private void loadSheduleFromDB() {
        manager.loadSchedule();//завантажити розклад з бази
        setSheduleView();//відобразити розклад
        setMenuView();//відобразити меню
        setTitleFragment(manager.getManagerPreferences().readGroupName());
    }

}

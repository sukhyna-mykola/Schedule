package com.mykola.schedule.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import com.mykola.schedule.data.network.APICallbacks;
import com.mykola.schedule.data.network.pojo.ResponceLessons;
import com.mykola.schedule.data.network.pojo.ResponceSearchGroups;
import com.mykola.schedule.data.network.pojo.ResponceWeek;
import com.mykola.schedule.ui.activitys.SearchActivity;
import com.mykola.schedule.ui.adapters.PagerAdapter;
import com.mykola.schedule.ui.dialogs.ProgressDialog;
import com.mykola.schedule.utils.Constants;
import com.mykola.schedule.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.mykola.schedule.ui.dialogs.ProgressDialog.DIALOG_PROGRESS;


public class ScheduleFragment extends Fragment implements APICallbacks {

    public static final int REQUEST_CODE = 14;

    private PagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ScheduleManager manager;

    private EditSheduleCallback editSheduleCallback;

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

        manager = ScheduleManager.get(getActivity());

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        viewPager = (ViewPager) view.findViewById(R.id.pager);


        if (manager.readStatusLogin()) {
            manager.loadScheduleFromDB();
            setSheduleView();
            setTitleFragment(manager.readGroupName());
        } else {
            showSearchScreen();
        }

        return view;
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

    private void setTitleFragment(String groupName) {
        String newTitle = getResources().getString(R.string.title, groupName);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(newTitle);
    }

    private void showSearchScreen() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null && manager.getLessons() != null) {
            manager.configureLessons();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_schedule, menu);
        setMenuView();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.week:

                if (manager.getWeekNumber() == Constants.FIRST_WEEK)
                    manager.setWeekNumber(Constants.SECOND_WEEK);
                else
                    manager.setWeekNumber(Constants.FIRST_WEEK);

                manager.loadScheduleOfWeekFromDB();
                setSheduleView();
                setMenuView();
                break;
            case R.id.search:
                manager.logOut();
                showSearchScreen();
                break;
            case R.id.update:
                getSchedule(manager.readGroupName());
                break;

            case R.id.edit:
                editSheduleCallback.startEditSheduleActivity(manager.getWeekNumber());
                break;
        }


        return true;
    }

    private void getSchedule(final String query) {
        if (NetworkStatusChecker.isNetworkAvailable(getContext())) {
            showDialog();
            manager.getSheduleFromServer(query, this);
        } else {
            showToast(getString(R.string.no_internet));
        }
    }

    private void showDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DialogFragment dialog = new ProgressDialog();

        dialog.show(fm, DIALOG_PROGRESS);
    }


    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK & requestCode == REQUEST_CODE) {
            loadSheduleFromDB();
        }
    }

    private void loadSheduleFromDB() {
        manager.loadScheduleFromDB();//завантажити розклад з бази
        setSheduleView();//відобразити розклад
        setMenuView();//відобразити меню
        setTitleFragment(manager.readGroupName());
    }


    @Override
    public void onResponseSheduleFromServer(String query, Call<ResponceLessons> call, Response<ResponceLessons> response) {
        if (response.body() != null) {

            manager.clearDB();
            manager.putLessonsIntoDB(response.body().getData());
            manager.saveGroupName(query);
            getWeek();
        } else {
            showToast(getString(R.string.not_found));
            hideDialog();
        }
    }

    private void getWeek() {

        if (NetworkStatusChecker.isNetworkAvailable(getContext())) {
            manager.getWeekFromServer(this);
        } else {
            showToast(getResources().getString(R.string.no_internet));
            hideDialog();
        }
    }

    private void hideDialog() {

        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment dialog = fm.findFragmentByTag(DIALOG_PROGRESS);

        if (dialog != null) {
            fm.beginTransaction()
                    .remove(dialog)
                    .commit();
        }
    }

    @Override
    public void onFailureSheduleFromServer(Call<ResponceLessons> call, Throwable t) {
        showToast(getString(R.string.not_found));
        hideDialog();
    }

    @Override
    public void onResponseWeekFromServer(Call<ResponceWeek> call, Response<ResponceWeek> response) {
        manager.saveWeek(response.body().getData());

        loadSheduleFromDB();
        showToast(getString(R.string.schedule_updated));
        hideDialog();
    }

    @Override
    public void onFailureWeekFromServer(Call<ResponceWeek> call, Throwable t) {
        showToast(getString(R.string.not_found));
        hideDialog();
    }

    @Override
    public void onResponseSearchGroups(Call<ResponceSearchGroups> call, Response<ResponceSearchGroups> response) {

    }

    @Override
    public void onFailureSearchGroups(Call<ResponceSearchGroups> call, Throwable t) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditSheduleCallback) {
            editSheduleCallback = (EditSheduleCallback) context;
        } else {
            throw new RuntimeException("must implement EditSheduleCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        editSheduleCallback = null;
    }

    public interface EditSheduleCallback {
        void startEditSheduleActivity(int week);
    }
}

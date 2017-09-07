package com.mykola.schedule.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.mykola.schedule.R;
import com.mykola.schedule.data.managers.EditSheduleManager;
import com.mykola.schedule.data.storage.models.EditLecture;
import com.mykola.schedule.ui.adapters.LecturesAdapter;
import com.mykola.schedule.ui.dialogs.edit.CallbackDialog;
import com.mykola.schedule.utils.Constants;

import java.util.ArrayList;


public class EditScheduleFragment extends Fragment implements CallbackDialog {
    public static final String WEEK_NUMBER = "WEEK_NUMBER";

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private EditSheduleManager editSheduleManager;
    private RecyclerView mRecyclerView;

    private Menu menu;

    public static EditScheduleFragment newInstance(int weekNumber) {

        Bundle args = new Bundle();
        args.putInt(WEEK_NUMBER, weekNumber);

        EditScheduleFragment fragment = new EditScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        int weekNumber = getArguments().getInt(WEEK_NUMBER, 1);

        editSheduleManager = EditSheduleManager.getManager(getContext());
        editSheduleManager.setWeek(weekNumber);
        editSheduleManager.genareteLessonsByWeek();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_edit, menu);
        setMenuView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.week:

                if (editSheduleManager.getWeek() == Constants.FIRST_WEEK)
                    editSheduleManager.setWeek(Constants.SECOND_WEEK);
                else
                    editSheduleManager.setWeek(Constants.FIRST_WEEK);
                editSheduleManager.genareteLessonsByWeek();
                setSheduleView();
                setMenuView();
                break;


        }


        return super.onOptionsItemSelected(item);
    }

    private void setMenuView() {
        MenuItem weekIcon = menu.findItem(R.id.week);

        if (editSheduleManager.getWeek() == Constants.FIRST_WEEK)
            weekIcon.setIcon(R.drawable.ic_looks_one_white_24dp);
        else weekIcon.setIcon(R.drawable.ic_looks_two_white_24dp);

    }

    private void setSheduleView() {
        mAdapter = new LecturesAdapter((ArrayList<EditLecture>) editSheduleManager.getLessons(), getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_schedule, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.edit_lessons);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), Constants.DAYS_NUMBER);
        mRecyclerView.setLayoutManager(mLayoutManager);

        setSheduleView();

        return view;
    }


    @Override
    public void update() {
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
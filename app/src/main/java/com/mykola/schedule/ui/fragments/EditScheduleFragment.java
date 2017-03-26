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

/**
 * Created by mykola on 03.03.17.
 */

public class EditScheduleFragment extends Fragment implements CallbackDialog {


    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private EditSheduleManager editSheduleManager;
    private RecyclerView mRecyclerView;

    private Menu menu;

    public static EditScheduleFragment newInstance() {

        Bundle args = new Bundle();

        EditScheduleFragment fragment = new EditScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
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
            weekIcon.setIcon(R.drawable.ic_looks_one_red_24dp);
        else weekIcon.setIcon(R.drawable.ic_looks_two_red_24dp);

    }

    private void setSheduleView() {
        mAdapter = new LecturesAdapter((ArrayList<EditLecture>) editSheduleManager.getLessons(), getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_schedule, container, false);
        editSheduleManager = EditSheduleManager.getManager(getActivity());
        editSheduleManager.genareteLessonsByWeek();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.edit_lessons);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(getActivity(), 6);
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
        editSheduleManager.setMoveState(false);
    }
}
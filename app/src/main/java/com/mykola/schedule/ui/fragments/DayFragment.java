package com.mykola.schedule.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mykola.schedule.R;
import com.mykola.schedule.data.managers.ScheduleManager;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.ui.adapters.LessonsAdapter;

import java.util.List;


public class DayFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String DAY_NUMBER_KEY = "DAY_NUMBER_KEY";

    private final String TAG = getClass().getSimpleName();

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    private SwipeRefreshLayout refreshLayout;


    public static DayFragment newInstance(int day) {
        Bundle args = new Bundle();
        args.putInt(DAY_NUMBER_KEY, day);
        DayFragment fragment = new DayFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int day = getArguments().getInt(DAY_NUMBER_KEY, 0);

        List<LessonDTO> lessons = ScheduleManager.get(getContext()).getLessons().get(day);

        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new LessonsAdapter(lessons, getContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();

        Log.d(TAG, "DayFragment  updated");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_fragment, container, false);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.lessons_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_day_fragment);
        refreshLayout.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void onRefresh() {
        mAdapter.notifyDataSetChanged();
        refreshLayout.setRefreshing(false);
        Log.d(TAG, "DayFragment  refreshed");
    }
}

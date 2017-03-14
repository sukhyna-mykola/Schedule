package com.mykola.schedule.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mykola.schedule.utils.Constants;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.ui.adapters.LessonsAdapter;
import com.mykola.schedule.R;
import com.mykola.schedule.data.managers.ScheduleManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mykola on 16.01.17.
 */

public class DayFragment extends Fragment {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;
    private List<LessonDTO> lessons;

    public static DayFragment newInstance(int day) {
        Bundle args = new Bundle();
        args.putInt(Constants.DAY_NUMBER_KEY,day);
        DayFragment fragment = new DayFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.day_fragment, container, false);
        int day = getArguments().getInt(Constants.DAY_NUMBER_KEY,0);
        lessons = ScheduleManager.get(getContext()).getLessons().get(day);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.lessons_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LessonsAdapter((ArrayList<LessonDTO>) lessons,getContext());
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

}

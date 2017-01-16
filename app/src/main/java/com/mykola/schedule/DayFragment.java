package com.mykola.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by mykola on 16.01.17.
 */

public class DayFragment extends Fragment {
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.day_fragment, container, false);
        TextView tv = (TextView) view.findViewById(R.id.tmp_text);
        int day = getArguments().getInt(Constants.DAY_NUMBER_KEY,0);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.lessons_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new LessonsAdapter(MainActivity.lessons.get(day));
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    public static DayFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(Constants.DAY_NUMBER_KEY,position);
        DayFragment fragment = new DayFragment();
        fragment.setArguments(args);
        return fragment;
    }
}

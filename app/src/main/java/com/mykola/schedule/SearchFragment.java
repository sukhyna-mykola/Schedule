package com.mykola.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.mykola.schedule.Constants.CODE_OK;

/**
 * Created by mykola on 03.03.17.
 */

public class SearchFragment extends Fragment implements View.OnClickListener,Request,AdapterView.OnItemClickListener {

    private EditText groupNameInputField;
    private ProgressBar progressBar;
    private ListView itemList;
    private Button buttonRequest;

    private ArrayAdapter<String> listAdapter;

    private ScheduleManager manager;


    public static SearchFragment newInstance() {

        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        manager = ScheduleManager.get(getActivity());
        manager.registerCallback(this);

        groupNameInputField = (EditText) view.findViewById(R.id.group_name_input_field);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        itemList = (ListView) view.findViewById(R.id.listView);
        buttonRequest = (Button) view.findViewById(R.id.button_request);


        buttonRequest.setOnClickListener(this);
        itemList.setOnItemClickListener(this);

        groupNameInputField.addTextChangedListener(new TextWatcher() {

            private int before;
            private int after;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before = s.length();
                Log.d("TAG", "before = " + s.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                after = s.length();
                Log.d("TAG", "after = " + s.length());
                if (s.length() > 0)
                    manager.searchGroups(String.valueOf(s));

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (before < after)
                    if (String.valueOf(s).length() == 2 && !String.valueOf(s).contains("-")) {
                        groupNameInputField.setText(groupNameInputField.getText().toString().toUpperCase() + "-");
                    }
                groupNameInputField.setSelection(groupNameInputField.getText().toString().length());
            }
        });

        return view;
    }


    private void makeRequest(String query) {
        buttonRequest.setVisibility(View.GONE);
        itemList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        // get WeekNumber
        manager.getWeekFromServer();
        //get Schedule
        manager.getSheduleFromServer(query);


    }


    @Override
    public void responceWeek(int responceCode) {
        if (responceCode == CODE_OK) {

        } else {
            progressBar.setVisibility(View.GONE);
            buttonRequest.setVisibility(View.VISIBLE);
            itemList.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void responceGroupsHint(int responceCode, String[] names) {
        if (responceCode == CODE_OK) {
            listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, names);
            itemList.setAdapter(listAdapter);
        } else {
            listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, names);
            itemList.setAdapter(listAdapter);
        }

    }

    @Override
    public void responceSchedule(int responceCode) {

        if (responceCode == CODE_OK) {
            Intent intent = new Intent();
            getActivity().setResult(RESULT_OK, intent);
            getActivity().finish();
        } else {
            Toast.makeText(getActivity(), "Не знайдено", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            buttonRequest.setVisibility(View.VISIBLE);
            itemList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_request: {
                makeRequest(String.valueOf(groupNameInputField.getText()).toLowerCase());
                break;
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        groupNameInputField.setText(listAdapter.getItem(position));
        makeRequest(listAdapter.getItem(position));
    }
}
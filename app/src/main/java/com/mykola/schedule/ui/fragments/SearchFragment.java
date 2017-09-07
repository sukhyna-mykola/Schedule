package com.mykola.schedule.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mykola.schedule.R;
import com.mykola.schedule.data.managers.ScheduleManager;
import com.mykola.schedule.data.network.APICallbacks;
import com.mykola.schedule.data.network.pojo.ResponceLessons;
import com.mykola.schedule.data.network.pojo.ResponceSearchGroups;
import com.mykola.schedule.data.network.pojo.ResponceWeek;
import com.mykola.schedule.data.storage.models.GroupDTO;
import com.mykola.schedule.utils.NetworkStatusChecker;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class SearchFragment extends Fragment implements View.OnClickListener,
        AdapterView.OnItemClickListener, APICallbacks {

    private EditText groupNameInputField;
    private ProgressBar progressBar;
    private ListView itemList;
    private Button buttonRequest, buttonGroup;


    private ArrayAdapter<String> listAdapter;

    private ScheduleManager manager;

    private TextWatcher textWatcher = new TextWatcher() {
        private int before;
        private int after;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            before = s.length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            after = s.length();
            if (s.length() > 0)
                manager.searchGroups(String.valueOf(s), SearchFragment.this);
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (before < after)
                if (String.valueOf(s).length() == 2 && !String.valueOf(s).contains("-")) {
                    groupNameInputField.setText(groupNameInputField.getText().toString().toUpperCase() + "-");
                }
            groupNameInputField.setSelection(groupNameInputField.getText().toString().length());
        }
    };


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

        manager = ScheduleManager.get(getActivity());

        listAdapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, new ArrayList<String>());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        groupNameInputField = (EditText) view.findViewById(R.id.group_name_input_field);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        itemList = (ListView) view.findViewById(R.id.listView);

        buttonRequest = (Button) view.findViewById(R.id.button_request);
        buttonGroup = (Button) view.findViewById(R.id.quick_group);

        buttonRequest.setOnClickListener(this);
        buttonGroup.setOnClickListener(this);
        itemList.setOnItemClickListener(this);

        groupNameInputField.addTextChangedListener(textWatcher);

        configureQuickGroupButton();

        itemList.setAdapter(listAdapter);

        return view;
    }

    private void configureQuickGroupButton() {
        String groupName = manager.readGroupName();

        if (groupName != null) {
            buttonGroup.setVisibility(View.VISIBLE);
            buttonGroup.setText(groupName);
        } else {
            buttonGroup.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_request:
                getSchedule(String.valueOf(groupNameInputField.getText()).toLowerCase());
                break;

            case R.id.quick_group:
                manager.logIn();
                setResultAndFinish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        groupNameInputField.setText(listAdapter.getItem(position));
        getSchedule(listAdapter.getItem(position));
    }


    private void getSchedule(final String query) {
        if (NetworkStatusChecker.isNetworkAvailable(getContext())) {
            buttonRequest.setVisibility(View.GONE);
            itemList.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            // get Schedule
            manager.getSheduleFromServer(query, this);
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResponseSheduleFromServer(String query, Call<ResponceLessons> call, Response<ResponceLessons> response) {
        if (response.body() != null) {
            manager.clearDB();
            manager.putLessonsIntoDB(response.body().getData());
            manager.saveGroupName(query);
            getWeek();
        } else {
            notFound();
        }
    }

    private void getWeek() {

        if (NetworkStatusChecker.isNetworkAvailable(getContext())) {
            // get WeekNumber
            manager.getWeekFromServer(this);
        } else {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }




    private void notFound() {
        Toast.makeText(getActivity(), R.string.not_found, Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        buttonRequest.setVisibility(View.VISIBLE);
        itemList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFailureSheduleFromServer(Call<ResponceLessons> call, Throwable t) {
        notFound();
    }

    @Override
    public void onResponseWeekFromServer(Call<ResponceWeek> call, Response<ResponceWeek> response) {
        if (response.body() != null) {
            manager.saveWeek(response.body().getData());
            manager.logIn();

            setResultAndFinish();
        } else {
            notFound();
        }
    }

    private void setResultAndFinish() {

        Intent intent = new Intent();
        getActivity().setResult(RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onFailureWeekFromServer(Call<ResponceWeek> call, Throwable t) {
        notFound();
    }

    @Override
    public void onResponseSearchGroups(Call<ResponceSearchGroups> call, Response<ResponceSearchGroups> response) {

        List<String> result = new ArrayList<String>();

        if (response.body() != null)
            for (GroupDTO group : response.body().getData()) {
                result.add(group.getGroupFullName());
            }

        listAdapter.clear();
        listAdapter.addAll(result);
    }

    @Override
    public void onFailureSearchGroups(Call<ResponceSearchGroups> call, Throwable t) {
        listAdapter.clear();
    }
}
package com.mykola.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import static com.mykola.schedule.Constants.CODE_OK;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener, Request {

    private EditText groupNameInputField;
    private ProgressBar progressBar;
    private ListView itemList;
    private Button buttonRequest;

    private ArrayAdapter<String> listAdapter;

    private ScheduleManager manager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        manager = ScheduleManager.get(this);
        manager.registerCallback(this);

        groupNameInputField = (EditText) findViewById(R.id.group_name_input_field);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        itemList = (ListView) findViewById(R.id.listView);
        buttonRequest = (Button) findViewById(R.id.button_request);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groupNameInputField.setText(listAdapter.getItem(position));
            }
        });
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

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.button_request: {
                makeRequest();
                break;
            }
        }
    }

    private void makeRequest() {
        buttonRequest.setVisibility(View.GONE);
        itemList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        // get WeekNumber
        manager.getWeekFromServer();
        //get Schedule
        manager.getSheduleFromServer(String.valueOf(groupNameInputField.getText()).toLowerCase());


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
            listAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
            itemList.setAdapter(listAdapter);
        } else {
            listAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
            itemList.setAdapter(listAdapter);
        }

    }

    @Override
    public void responceSchedule(int responceCode) {

        if (responceCode == CODE_OK) {
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(SearchActivity.this, "Не знайдено", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            buttonRequest.setVisibility(View.VISIBLE);
            itemList.setVisibility(View.VISIBLE);
        }
    }
}

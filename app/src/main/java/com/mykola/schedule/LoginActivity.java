package com.mykola.schedule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText groupNameInputField;

    private DBHelper dbHelper;
    private ProgressBar progressBar;
    private ArrayAdapter<String> listAdapter;
    private ListView itemList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);
        groupNameInputField = (EditText) findViewById(R.id.group_name_input_field);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        itemList = (ListView) findViewById(R.id.listView);


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
                Log.d("TAG","before = "+s.length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                after = s.length();
                Log.d("TAG","after = "+s.length());
                if (s.length() > 0)
                    searchGroups(String.valueOf(s));

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
                makeRequest(v);
                break;
            }
        }
    }

    private void makeRequest(final View v) {
        v.setVisibility(View.GONE);
        itemList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Call<ResponceWeek> callWeek = App.getApi().getWeek();
        callWeek.enqueue(new Callback<ResponceWeek>() {
            @Override
            public void onResponse(Call<ResponceWeek> call, Response<ResponceWeek> response) {
                MainActivity.weekNumber = response.body().getData();
                Log.d("TAG", String.valueOf(MainActivity.weekNumber));
            }

            @Override
            public void onFailure(Call<ResponceWeek> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                v.setVisibility(View.VISIBLE);
                itemList.setVisibility(View.VISIBLE);
            }
        });


        final String groupName = String.valueOf(groupNameInputField.getText()).toLowerCase();

        Call<ResponceLessons> callLessons = App.getApi().getLessons(groupName);
        callLessons.enqueue(new Callback<ResponceLessons>() {
            @Override
            public void onResponse(Call<ResponceLessons> call, Response<ResponceLessons> response) {
                if (response.body() != null) {
                    for (Lesson lessson : response.body().getData()) {
                        Log.d("TAG", lessson.getLessonName());
                        putDataToDB(lessson);
                    }

                    Intent intent = new Intent();
                    intent.putExtra(Constants.GROUP_KEY, groupName);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Не знайдено", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    v.setVisibility(View.VISIBLE);
                    itemList.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponceLessons> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Помилка", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                v.setVisibility(View.VISIBLE);
                itemList.setVisibility(View.VISIBLE);
            }
        });
    }


    private void searchGroups(String groupName) {
        final ArrayList<String> result = new ArrayList<>();
        String filter = "{'query':'" + groupName + "'}";
        Call<ResponceSearchGroups> callGroups = App.getApi().searchGroupsByName(filter);
        callGroups.enqueue(new Callback<ResponceSearchGroups>() {
            String[] names;

            @Override
            public void onResponse(Call<ResponceSearchGroups> call, Response<ResponceSearchGroups> response) {
                if (response.body() != null)
                    for (Group group : response.body().getData()) {
                        result.add(group.getGroupFullName());
                    }
                names = new String[result.size()];
                result.toArray(names);
                listAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
                itemList.setAdapter(listAdapter);
            }

            @Override
            public void onFailure(Call<ResponceSearchGroups> call, Throwable t) {
                names = new String[result.size()];
                result.toArray(names);
                listAdapter = new ArrayAdapter<String>(LoginActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, names);
                itemList.setAdapter(listAdapter);
            }
        });


    }

    private void putDataToDB(Lesson lesson) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        cv.put(Constants.TEACHER_NAME, lesson.getTeacherName());
        cv.put(Constants.LESSON_NAME, lesson.getLessonName());
        cv.put(Constants.LESSON_NUMBER, lesson.getLessonNumber());
        cv.put(Constants.LESSON_ROOM, lesson.getLessonRoom());
        cv.put(Constants.LESSON_TYPE, lesson.getLessonType());
        cv.put(Constants.DAY_NUMBER, lesson.getDayNumber());
        cv.put(Constants.LESSON_WEEK, lesson.getLessonWeek());
        cv.put(Constants.TIME_END, lesson.getTimeEnd());
        cv.put(Constants.TIME_START, lesson.getTimeStart());

        db.insert(Constants.TABLE_NAME, null, cv);
    }


}

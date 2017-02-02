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

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText groupNameInputField;

    private DBHelper dbHelper;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);
        groupNameInputField = (EditText) findViewById(R.id.group_name_input_field);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.button_request: {
                v.setVisibility(View.GONE);
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
                    }
                });


                final String groupName = String.valueOf(groupNameInputField.getText()).toLowerCase();

                Call<ResponceLessons> callLessons = App.getApi().getLessons(groupName);
                callLessons.enqueue(new Callback<ResponceLessons>() {
                    @Override
                    public void onResponse(Call<ResponceLessons> call, Response<ResponceLessons> response) {
                        for (Lesson lessson : response.body().getData()) {
                            Log.d("TAG", lessson.getLessonName());
                            putDataToDB(lessson);
                        }

                        Intent intent = new Intent();
                        intent.putExtra(Constants.GROUP_KEY, groupName);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<ResponceLessons> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Помилка", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        v.setVisibility(View.VISIBLE);
                    }
                });
                break;
            }
        }
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

package com.mykola.schedule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText groupNameInputField;

    private DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);
        groupNameInputField = (EditText) findViewById(R.id.group_name_input_field);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_requset: {

                Call<ResponceWeek> callWeek =App.getApi().getWeek();
                callWeek.enqueue(new Callback<ResponceWeek>() {
                    @Override
                    public void onResponse(Call<ResponceWeek> call, Response<ResponceWeek> response) {
                        MainActivity.weekNumber = response.body().getData();
                        Log.d("TAG",String.valueOf(MainActivity.weekNumber));
                    }

                    @Override
                    public void onFailure(Call<ResponceWeek> call, Throwable t) {

                    }
                });


                final String groupName = String.valueOf(groupNameInputField.getText());

                Call<ResponceLessons> callLessons = App.getApi().getLessons(groupName);
                callLessons.enqueue(new Callback<ResponceLessons>() {
                    @Override
                    public void onResponse(Call<ResponceLessons> call, Response<ResponceLessons>response) {
                            for (Lesson lessson : response.body().getData()) {
                                Log.d("TAG",lessson.getLessonFullName());
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
                    }


                });
                break;
            }
        }
    }


    private void putDataToDB(Lesson lesson) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        cv.put(Constants.DAY_NUMBER, lesson.getDayNumber());
        cv.put(Constants.TEACHER_NAME, lesson.getTeacherName());
        cv.put(Constants.LESSON_NAME, lesson.getLessonName());
        cv.put(Constants.LESSON_NUMBER, lesson.getLessonNumber());
        cv.put(Constants.LESSON_ROOM, lesson.getLessonRoom());
        cv.put(Constants.LESSON_TYPE, lesson.getLessonType());
        cv.put(Constants.LESSON_WEEK, lesson.getLessonWeek());

        db.insert(Constants.TABLE_NAME, null, cv);
    }


}

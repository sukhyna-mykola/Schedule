package com.mykola.schedule;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText groupNameInputField;
    private ProgressBar mProgressBar;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DBHelper(this);
        groupNameInputField = (EditText) findViewById(R.id.group_name_input_field);

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_requset: {
                String groupName = String.valueOf(groupNameInputField.getText());
                new ScheduleRequset().execute(groupName);
                break;
            }
        }
    }


    private class ScheduleRequset extends AsyncTask<String, Integer, String> {
        private String groupName;

        @Override
        protected String doInBackground(String... params) {
            groupName = params[0];
            String dataUrl = Constants.GROUPS_URL + groupName + "/lessons";
            Log.d(Constants.TAG, dataUrl);
            URL url;
            HttpURLConnection connection = null;
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                //отримати номер тижня
                url = new URL(Constants.WEEK_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                publishProgress(15);
                InputStream in = connection.getInputStream();
                publishProgress(20);
                br = new BufferedReader(new InputStreamReader(in));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                publishProgress(35);
                setWeekNumber(sb);

                //отримати предмети

                sb = new StringBuilder();
                url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                publishProgress(40);

                in = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(in));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                publishProgress(50);
                parseJSON(String.valueOf(sb));
                publishProgress(100);
                return String.valueOf(sb);

            } catch (Exception e) {
                publishProgress(0);
                return null;
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (connection != null)
                    connection.disconnect();
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressBar.setProgress(values[0]);
        }


        @Override
        protected void onPostExecute(String responce) {
            if (responce != null) {
                Log.d(Constants.TAG, responce);
                Intent intent = new Intent();
                intent.putExtra(Constants.GROUP_KEY, groupName);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Помилка", Toast.LENGTH_SHORT).show();
            }

        }

        private void setWeekNumber(StringBuilder sb) {
            try {
                JSONObject jsonObj = new JSONObject(String.valueOf(sb));
                MainActivity.weekNumber = jsonObj.getInt(Constants.JSON_DATA);
                Log.d(Constants.TAG, MainActivity.weekNumber + "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private boolean parseJSON(String jsonStr) {

            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray data = jsonObj.getJSONArray(Constants.JSON_DATA);
                for (int i = 0; i < data.length(); i++) {
                    publishProgress(50+i*30/data.length());

                    JSONObject lesson = data.getJSONObject(i);

                    String dayNumber = lesson.getString(Constants.DAY_NUMBER);
                    String lessonNumber = lesson.getString(Constants.LESSON_NUMBER);
                    String lessonWeek = lesson.getString(Constants.LESSON_WEEK);
                    String lessonName = lesson.getString(Constants.LESSON_NAME);
                    String lessonType = lesson.getString(Constants.LESSON_TYPE);
                    String lessonRoom = lesson.getString(Constants.LESSON_ROOM);
                    String teacherName = lesson.getString(Constants.TEACHER_NAME);
                    Lesson les = new Lesson(lessonName, lessonType, teacherName, lessonRoom,
                            Integer.parseInt(lessonNumber), Integer.parseInt(dayNumber), Integer.parseInt(lessonWeek));
                    putDataToBD(les);

                }

                return true;
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }

        }

        private void putDataToBD(Lesson lesson) {
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            cv.put(Constants.DAY_NUMBER, lesson.getDay());
            cv.put(Constants.TEACHER_NAME, lesson.getTeatherName());
            cv.put(Constants.LESSON_NAME, lesson.getName());
            cv.put(Constants.LESSON_NUMBER, lesson.getNumber());
            cv.put(Constants.LESSON_ROOM, lesson.getRoom());
            cv.put(Constants.LESSON_TYPE, lesson.getType());
            cv.put(Constants.LESSON_WEEK, lesson.getWeek());

            db.insert(Constants.TABLE_NAME, null, cv);
        }
    }

}

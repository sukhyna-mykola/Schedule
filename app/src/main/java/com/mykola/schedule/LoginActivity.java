package com.mykola.schedule;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText groupNameInputField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        groupNameInputField = (EditText) findViewById(R.id.group_name_input_field);

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


    private class ScheduleRequset extends AsyncTask<String, Void, String> {
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

                InputStream in = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(in));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                setWeekNumber(sb);

                //отримати предмети
                sb = new StringBuilder();
                url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                in = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(in));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                return String.valueOf(sb);

            } catch (Exception e) {
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

        private void setWeekNumber(StringBuilder sb) {
            try {
                JSONObject jsonObj = new JSONObject(String.valueOf(sb));
                MainActivity.weekNumber = jsonObj.getInt(Constants.JSON_DATA);
                Log.d(Constants.TAG,MainActivity.weekNumber+"");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(String responce) {
            if (responce != null) {
                Log.d(Constants.TAG, responce);
                Intent intent = new Intent();
                intent.putExtra(Constants.JSON_RESPONCE_KEY, responce);
                intent.putExtra(Constants.GROUP_KEY,groupName);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Log.d(Constants.TAG, "ERROR");
            }

        }
    }

}

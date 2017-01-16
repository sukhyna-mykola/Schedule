package com.mykola.schedule;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(Constants.TAG, "START");
        new ScheduleRequset().execute("іп-62м");
    }


    private class ScheduleRequset extends AsyncTask<String, Void, Void> {
        private String groupName;

        @Override
        protected Void doInBackground(String... params) {
            groupName = params[0];
            String dataUrl = Constants.GROUPS_URL + groupName + "/lessons";
            Log.d(Constants.TAG, dataUrl);
            URL url;
            HttpURLConnection connection = null;
            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();
            String line;

            try {
//               Create connection
                url = new URL(dataUrl);
                connection = (HttpURLConnection) url.openConnection();

                InputStream in = connection.getInputStream();

                br = new BufferedReader(new InputStreamReader(in));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                Log.d(Constants.TAG, String.valueOf(sb));
            } catch (Exception e) {
                Log.d(Constants.TAG, "ERROR");
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
            return null;
        }
    }
}

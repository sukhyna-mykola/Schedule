package com.mykola.schedule;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sPref;
    public static ArrayList<ArrayList<Lesson>> lessons;
    public static int weekNumber;

    private PagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        initLessons();
        boolean logined = checkStatusLogin();
        if (!logined) {

        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 1);
        }
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            String responce = data.getStringExtra(Constants.JSON_RESPONCE_KEY);
            setStatusLogin(parseJSON(responce));
            setViewScheme();
        }
    }


    private void initLessons() {
        lessons = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lessons.add(new ArrayList<Lesson>());

        }
    }

    private void setViewScheme() {

        for (int i = 0; i < 6; i++) {

            if (lessons.get(i).size() != 0) {
                switch (i) {
                    case 0:
                        tabLayout.addTab(tabLayout.newTab().setText("Понеділок"));
                        break;
                    case 1:
                        tabLayout.addTab(tabLayout.newTab().setText("Вівторок"));
                        break;
                    case 2:
                        tabLayout.addTab(tabLayout.newTab().setText("Середа"));
                        break;
                    case 3:
                        tabLayout.addTab(tabLayout.newTab().setText("Червер"));
                        break;
                    case 4:
                        tabLayout.addTab(tabLayout.newTab().setText("Пятниця"));
                        break;
                    case 5:
                        tabLayout.addTab(tabLayout.newTab().setText("Субота"));
                        break;
                }
            }
        }

        Iterator<ArrayList<Lesson>> iter = lessons.iterator();
        while (iter.hasNext()) {
            ArrayList<Lesson> element = iter.next();
            if (element.size() == 0) {
                iter.remove();
            }
        }
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void setStatusLogin(boolean statusLogin) {
        sPref = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.LOGIN_KEY, statusLogin);
        ed.commit();

    }

    private boolean checkStatusLogin() {
        sPref = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sPref.getBoolean(Constants.LOGIN_KEY, false);
    }

    private boolean parseJSON(String jsonStr) {
        initLessons();

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray data = jsonObj.getJSONArray(Constants.JSON_DATA);
            for (int i = 0; i < data.length(); i++) {

                JSONObject lesson = data.getJSONObject(i);

                String dayNumber = lesson.getString(Constants.DAY_NUMBER);
                String lessonNumber = lesson.getString(Constants.LESSON_NUMBER);
                String lessonWeek = lesson.getString(Constants.LESSON_WEEK);
                String lessonName = lesson.getString(Constants.LESSON_NAME);
                String lessonType = lesson.getString(Constants.LESSON_TYPE);
                String lessonRoom = lesson.getString(Constants.LESSON_ROOM);
                String teacherName = lesson.getString(Constants.TEACHER_NAME);
                if (weekNumber == Integer.parseInt(lessonWeek))
                    lessons.get(Integer.parseInt(dayNumber) - 1).add(new Lesson(lessonName, lessonType, teacherName, lessonRoom,
                            Integer.parseInt(lessonNumber), Integer.parseInt(dayNumber), Integer.parseInt(lessonWeek)));

            }


            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }
}

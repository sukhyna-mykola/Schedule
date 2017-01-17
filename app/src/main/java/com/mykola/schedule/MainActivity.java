package com.mykola.schedule;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {


    public static ArrayList<ArrayList<Lesson>> lessons;
    public static int weekNumber;

    private PagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private DBHelper dbHelper;
    private SharedPreferences sPref;

    private Menu menu;

    private String groupName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveStatusLogin(false);
                clearDB();
                initLessons();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);

            }
        });

        dbHelper = new DBHelper(this);

        initLessons();

        boolean logined = checkStatusLogin();
        if (logined) {
            groupName = getGroupName();
            loadNumberWeek();
            readDataFromDB();
            setViewScheme();
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 1);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        setMenuView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.one_week) {
            item.setIcon(R.drawable.ic_looks_one_red_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_looks_two_white_24dp);
            weekNumber = Constants.FIRST_WEEK;
        }
        if (id == R.id.two_week) {
            item.setIcon(R.drawable.ic_looks_two_red_24dp);
            menu.getItem(0).setIcon(R.drawable.ic_looks_one_white_24dp);
            weekNumber = Constants.SECOND_WEEK;
        }

        initLessons();
        readDataFromDB();
        setViewScheme();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            groupName = data.getStringExtra(Constants.GROUP_KEY);
            setGroupName(groupName);

            if (weekNumber == Constants.FIRST_WEEK)
                saveParityWeek(false);
            else saveParityWeek(true);

            setMenuView();
            saveStatusLogin(true);
            readDataFromDB();
            setViewScheme();

        }
    }


    private void initLessons() {
        lessons = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            lessons.add(new ArrayList<Lesson>());

        }
    }

    private void clearDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, null, null);
    }

    private void setMenuView() {
        if (weekNumber == Constants.FIRST_WEEK) {
            menu.getItem(0).setIcon(R.drawable.ic_looks_one_red_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_looks_two_white_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_looks_one_white_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_looks_two_red_24dp);
        }
    }



    private void readDataFromDB() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query(Constants.TABLE_NAME, null, Constants.LESSON_WEEK + "=?", new String[]{String.valueOf(MainActivity.weekNumber)}, null, null, null);

        if (c.moveToFirst()) {

            int dayColIndex = c.getColumnIndex(Constants.DAY_NUMBER);
            int nameColIndex = c.getColumnIndex(Constants.LESSON_NAME);
            int teacherlColIndex = c.getColumnIndex(Constants.TEACHER_NAME);
            int typeColIndex = c.getColumnIndex(Constants.LESSON_TYPE);
            int roomColIndex = c.getColumnIndex(Constants.LESSON_ROOM);
            int numberColIndex = c.getColumnIndex(Constants.LESSON_NUMBER);
            int weekColIndex = c.getColumnIndex(Constants.LESSON_WEEK);


            do {

                Lesson lesson = new Lesson(c.getString(nameColIndex), c.getString(typeColIndex),
                        c.getString(teacherlColIndex), c.getString(roomColIndex), c.getInt(numberColIndex),
                        c.getInt(dayColIndex), c.getInt(weekColIndex));
                lessons.get(c.getInt(dayColIndex) - 1).add(lesson);

            } while (c.moveToNext());
        }

        c.close();
    }


    private void setViewScheme() {
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + "(група " + groupName + ")");
        tabLayout.removeAllTabs();
        for (int i = 0; i < 6; i++) {
            if (lessons.get(i).size() != 0) {
                switch (i) {
                    case 0:
                        tabLayout.addTab(tabLayout.newTab().setText(R.string.monday));
                        break;
                    case 1:
                        tabLayout.addTab(tabLayout.newTab().setText(R.string.tuesday));
                        break;
                    case 2:
                        tabLayout.addTab(tabLayout.newTab().setText(R.string.wednesday));
                        break;
                    case 3:
                        tabLayout.addTab(tabLayout.newTab().setText(R.string.thursday));
                        break;
                    case 4:
                        tabLayout.addTab(tabLayout.newTab().setText(R.string.friday));
                        break;
                    case 5:
                        tabLayout.addTab(tabLayout.newTab().setText(R.string.saturday));
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
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
        adapter.notifyDataSetChanged();
    }

    private void saveStatusLogin(boolean statusLogin) {
        sPref = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.LOGIN_KEY, statusLogin);
        ed.commit();

    }

    private void setGroupName(String name) {
        sPref = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.GROUP_KEY, name);
        ed.commit();

    }

    private boolean checkStatusLogin() {
        sPref = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sPref.getBoolean(Constants.LOGIN_KEY, false);
    }

    private String getGroupName() {
        sPref = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sPref.getString(Constants.GROUP_KEY, "");
    }

    private void saveParityWeek(boolean parity) {
        sPref = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.PARITY_WEEK_KEY, parity);
        ed.commit();

    }

    private boolean checkParityWeek() {
        sPref = getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        return sPref.getBoolean(Constants.PARITY_WEEK_KEY, false);
    }

    private void loadNumberWeek() {
        boolean parity = checkParityWeek();
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_YEAR);
        if (parity) {
            if (week % 2 == 0) {
                weekNumber = Constants.SECOND_WEEK;
            } else weekNumber = Constants.FIRST_WEEK;
        } else {
            if (week % 2 == 0) {
                weekNumber = Constants.FIRST_WEEK;
            } else weekNumber = Constants.SECOND_WEEK;
        }
    }


}

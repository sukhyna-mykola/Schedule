package com.mykola.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private PagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ScheduleManager manager;

    private Menu menu;

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
                manager.logOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent, 1);

            }
        });
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        manager = ScheduleManager.get(this);
        if (manager.checkStatusLogin()==true) {
            manager.loadSchedule();
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
            manager.setWeekNumber(Constants.FIRST_WEEK) ;
        }
        if (id == R.id.two_week) {
            item.setIcon(R.drawable.ic_looks_two_red_24dp);
            menu.getItem(0).setIcon(R.drawable.ic_looks_one_white_24dp);
            manager.setWeekNumber(Constants.SECOND_WEEK);
        }

        manager.showScheduleOfWeek();
        setViewScheme();

        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            manager.loadSchedule();
            setViewScheme();
            setMenuView();

        }
    }


    private void setMenuView() {
        if (manager.getWeekNumber() == Constants.FIRST_WEEK) {
            menu.getItem(0).setIcon(R.drawable.ic_looks_one_red_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_looks_two_white_24dp);
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_looks_one_white_24dp);
            menu.getItem(1).setIcon(R.drawable.ic_looks_two_red_24dp);
        }
    }


    private void setViewScheme() {
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name) + "(група " + manager.getGroupName() + ")");
        tabLayout.removeAllTabs();
        //Тут я віднімаю 1 через те, що відлік днів відбувається з неділлі
        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
        Log.d("TAG", " day = " + day);
        int position = -1;
        int count = 0;

        for (int i = 0; i < 6; i++) {
            if (manager.getLessons().get(i).size() != 0) {
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

                if (day == i + 1) {
                    position = count;
                }
                count++;
            }

        }

        Iterator<ArrayList<Lesson>> iter = manager.getLessons().iterator();
        while (iter.hasNext()) {
            ArrayList<Lesson> element = iter.next();
            if (element.size() == 0) {
                iter.remove();
            }
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        if (position != -1)
            viewPager.setCurrentItem(position);
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


}

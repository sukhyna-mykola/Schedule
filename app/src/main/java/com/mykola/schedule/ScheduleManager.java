package com.mykola.schedule;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.mykola.schedule.Constants.CODE_ERROR;
import static com.mykola.schedule.Constants.CODE_OK;

/**
 * Created by mykola on 17.02.17.
 */

public class ScheduleManager {

    private HashMap<Integer, List<Lesson>> lessons;
    private int weekNumber;
    private int currentWeek;

    private DBHelper dbHelper;
    private SharedPreferences sPref;
    private Context context;

    private String groupName;

    private static ScheduleManager manager;

    private Request request;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public HashMap<Integer, List<Lesson>> getLessons() {
        return lessons;
    }

    public void setLessons(HashMap<Integer, List<Lesson>> lessons) {
        this.lessons = lessons;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }


    private ScheduleManager(Context context) {
        this.context = context;
        dbHelper = new DBHelper(context);
        sPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
        setGroupName(readGroupName());
    }

    public static ScheduleManager get(Context context) {
        if (manager == null)
            manager = new ScheduleManager(context);
        return manager;
    }

    public void logOut() {
        saveStatusLogin(false);
    }

    public void registerCallback(Request request) {
        this.request = request;
    }

    public synchronized void getSheduleFromServer(final String name) {

        Call<ResponceLessons> callLessons = App.getApi().getLessons(name);
        callLessons.enqueue(new Callback<ResponceLessons>() {
            @Override
            public void onResponse(Call<ResponceLessons> call, Response<ResponceLessons> response) {
                if (response.body() != null) {
                    clearDB();
                    logIn(name, response.body().getData());
                    request.responceSchedule(CODE_OK);

                } else {
                    request.responceSchedule(CODE_ERROR);
                }
            }

            @Override
            public void onFailure(Call<ResponceLessons> call, Throwable t) {
                request.responceSchedule(CODE_ERROR);
            }
        });
    }


    public void clearDB(){
        dbHelper.clearDB();
    }

    public synchronized void getWeekFromServer() {

        Call<ResponceWeek> callWeek = App.getApi().getWeek();
        callWeek.enqueue(new Callback<ResponceWeek>() {
            @Override
            public void onResponse(Call<ResponceWeek> call, Response<ResponceWeek> response) {
                setWeek(response.body().getData());
                request.responceWeek(CODE_OK);
            }

            @Override
            public void onFailure(Call<ResponceWeek> call, Throwable t) {
                request.responceWeek(CODE_ERROR);
            }
        });
    }


    /**
     * Підказки при введенні назви групи
     * @param groupName
     */
    public void searchGroups(String groupName) {
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
                request.responceGroupsHint(CODE_OK, names);

            }

            @Override
            public void onFailure(Call<ResponceSearchGroups> call, Throwable t) {
                names = new String[result.size()];
                result.toArray(names);
                request.responceGroupsHint(CODE_ERROR, names);

            }
        });


    }

    public void setWeek(int week) {
        setWeekNumber(week);
        if (week == Constants.FIRST_WEEK)
            saveParityWeek(false);
        else saveParityWeek(true);
    }

    public void logIn(String name, List<Lesson> lessons) {

        for (Lesson lessson : lessons) {
            dbHelper.putLessonIntoDB(lessson);
        }
        saveGroupName(name);
        saveStatusLogin(true);

        setGroupName(name);
    }

    public void loadSchedule() {
        loadNumberWeek();
        setLessons(dbHelper.readLessonsFromDB(weekNumber));
    }

    public void loadScheduleOfWeek() {
        setLessons(dbHelper.readLessonsFromDB(weekNumber));
    }


    private void saveStatusLogin(boolean statusLogin) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.LOGIN_KEY, statusLogin);
        ed.commit();
    }

    public boolean readStatusLogin() {
        return sPref.getBoolean(Constants.LOGIN_KEY, false);
    }


    private void loadNumberWeek() {
        boolean parity = readParityWeek();
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
        currentWeek = weekNumber;
    }

    private boolean readParityWeek() {
        return sPref.getBoolean(Constants.PARITY_WEEK_KEY, false);
    }

    private void saveParityWeek(boolean parity) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.PARITY_WEEK_KEY, parity);
        ed.commit();

    }

    private String readGroupName() {
        return sPref.getString(Constants.GROUP_KEY, "GROUPE");
    }

    private void saveGroupName(String name) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.GROUP_KEY, name);
        ed.commit();
    }
}

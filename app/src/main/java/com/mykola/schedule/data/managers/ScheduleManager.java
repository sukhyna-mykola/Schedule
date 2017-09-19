package com.mykola.schedule.data.managers;

import android.content.Context;

import com.mykola.schedule.data.network.APICallbacks;
import com.mykola.schedule.data.network.ServiceBuilder;
import com.mykola.schedule.data.network.pojo.ResponceLessons;
import com.mykola.schedule.data.network.pojo.ResponceSearchGroups;
import com.mykola.schedule.data.network.pojo.ResponceWeek;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.utils.Constants;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public final class ScheduleManager {

    private HashMap<Integer, List<LessonDTO>> lessons;

    private int weekNumber;
    private int currentWeek;

    private DBManager managerDB;
    private PreferencesManager managerPreferences;

    private static ScheduleManager manager;

    public static ScheduleManager get(Context context) {
        if (manager == null)
            manager = new ScheduleManager(context);
        return manager;
    }

    private ScheduleManager(Context context) {
        managerDB = new DBManager(context);
        managerPreferences = new PreferencesManager(context);
    }


    public void getSheduleFromServer(final String name, final APICallbacks callbacks) {
        ServiceBuilder.getApi().getLessons(name).enqueue(new Callback<ResponceLessons>() {
            @Override
            public void onResponse(Call<ResponceLessons> call, Response<ResponceLessons> response) {
                callbacks.onResponse( call, response);
            }

            @Override
            public void onFailure(Call<ResponceLessons> call, Throwable t) {
                callbacks.onFailure(call, t);
            }
        });

    }

    public void getWeekFromServer(final APICallbacks callbacks) {
        ServiceBuilder.getApi().getWeek().enqueue(new Callback<ResponceWeek>() {
            @Override
            public void onResponse(Call<ResponceWeek> call, Response<ResponceWeek> response) {
                callbacks.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponceWeek> call, Throwable t) {
                callbacks.onFailure(call, t);
            }
        });
    }


    /**
     * Підказки при введенні назви групи
     *
     * @param groupName
     */
    public void searchGroups(String groupName, final APICallbacks callbacks) {
        String filter = "{'query':'" + groupName + "'}";
        ServiceBuilder.getApi().searchGroupsByName(filter).enqueue(new Callback<ResponceSearchGroups>() {

            @Override
            public void onResponse(Call<ResponceSearchGroups> call, Response<ResponceSearchGroups> response) {
                callbacks.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponceSearchGroups> call, Throwable t) {
                callbacks.onFailure(call, t);
            }
        });

    }

    public void clearDB() {
        managerDB.clearDB();
    }

    public void saveWeek(int week) {
        Calendar c = Calendar.getInstance(Locale.UK);
        int weekReal = c.get(Calendar.WEEK_OF_YEAR);

        /**
         * Якщо зараз парний тиждень в дійсності
         *      якщо парний по розкраду -
         *           відповідність  = true;
         *      інакще -
         *          відповідність  = false;
         * інакше
         *      якщо парний по розкраду -
         *           відповідність  = false;
         *      інакще -
         *          відповідність  = true;
         */
        if (weekReal % 2 == 0) {
            if (week % 2 == 0) {
                managerPreferences.saveConformityWeek(true);
            } else managerPreferences.saveConformityWeek(false);
        } else {
            if (week % 2 == 0) {
                managerPreferences.saveConformityWeek(false);
            } else managerPreferences.saveConformityWeek(true);
        }

    }

    public void logOut() {
        managerPreferences.saveStatusLogin(false);
    }

    public void logIn() {
        managerPreferences.saveStatusLogin(true);
    }

    public boolean readStatusLogin() {
        return managerPreferences.readStatusLogin();
    }

    public void putLessonsIntoDB(List<LessonDTO> lessons) {
        for (LessonDTO lessson : lessons) {
            managerDB.putLessonIntoDB(lessson);
        }
    }

    public void saveGroupName(String name) {
        managerPreferences.saveGroupName(name);
    }

    public String readGroupName() {
        return managerPreferences.readGroupName();
    }

    public void loadScheduleFromDB() {
        determineNumberWeek();
        loadScheduleOfWeekFromDB();
    }

    /**
     * Визначає номер тижня
     */
    private void determineNumberWeek() {
        Calendar c = Calendar.getInstance(Locale.UK);
        int week = c.get(Calendar.WEEK_OF_YEAR);

        boolean conformity = managerPreferences.readConformityWeek();
        if (conformity) {
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

    public void loadScheduleOfWeekFromDB() {
        this.lessons = managerDB.readLessonsFromDB(weekNumber);
    }


    public HashMap<Integer, List<LessonDTO>> getLessons() {
        return lessons;
    }

    public int getCurrentWeek() {
        return currentWeek;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }


}

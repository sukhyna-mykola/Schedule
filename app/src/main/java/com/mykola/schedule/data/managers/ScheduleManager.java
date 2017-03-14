package com.mykola.schedule.data.managers;

import android.content.Context;

import com.mykola.schedule.data.network.ServiceBuilder;
import com.mykola.schedule.data.network.pojo.ResponceLessons;
import com.mykola.schedule.data.network.pojo.ResponceSearchGroups;
import com.mykola.schedule.data.network.pojo.ResponceWeek;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.utils.Constants;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;

/**
 * Created by mykola on 17.02.17.
 */

public class ScheduleManager {

    private HashMap<Integer, List<LessonDTO>> lessons;
    private int weekNumber;
    private int currentWeek;

    private DBManager managerDB;
    private PreferencesManager managerPreferences;

    private static ScheduleManager manager;

    public DBManager getManagerDB() {
        return managerDB;
    }

    public PreferencesManager getManagerPreferences() {
        return managerPreferences;
    }

    public HashMap<Integer, List<LessonDTO>> getLessons() {
        return lessons;
    }

    public void setLessons(HashMap<Integer, List<LessonDTO>> lessons) {
        this.lessons = lessons;
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


    private ScheduleManager(Context context) {
        managerDB = new DBManager(context);
        managerPreferences = new PreferencesManager(context);
    }

    public static ScheduleManager get(Context context) {
        if (manager == null)
            manager = new ScheduleManager(context);
        return manager;
    }


    public Call<ResponceLessons> getSheduleFromServer(final String name) {
        return ServiceBuilder.getApi().getLessons(name);
    }

    public Call<ResponceWeek> getWeekFromServer() {
        return ServiceBuilder.getApi().getWeek();
    }


    /**
     * Підказки при введенні назви групи
     *
     * @param groupName
     */
    public Call<ResponceSearchGroups> searchGroups(String groupName) {
        String filter = "{'query':'" + groupName + "'}";
        return ServiceBuilder.getApi().searchGroupsByName(filter);
    }


    public void clearDB() {
        managerDB.clearDB();
    }

    public void saveWeek(int week) {
        if (week == Constants.FIRST_WEEK)
            managerPreferences.saveParityWeek(false);
        else managerPreferences.saveParityWeek(true);
    }

    public void logOut() {
        managerPreferences.saveStatusLogin(false);
    }

    public void logIn(String name, List<LessonDTO> lessons) {
        managerPreferences.saveStatusLogin(true);
        for (LessonDTO lessson : lessons) {
            managerDB.putLessonIntoDB(lessson);
        }
        managerPreferences.saveGroupName(name);
    }

    public void loadSchedule() {
        determineNumberWeek();
        setLessons(managerDB.readLessonsFromDB(weekNumber));
    }

    public void loadScheduleOfWeek() {
        setLessons(managerDB.readLessonsFromDB(weekNumber));
    }


    private void determineNumberWeek() {
        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.WEEK_OF_YEAR);
        
        boolean parity = managerPreferences.readParityWeek();
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


}

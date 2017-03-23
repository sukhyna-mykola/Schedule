package com.mykola.schedule.data.managers;

import android.content.Context;

import com.mykola.schedule.data.network.ServiceBuilder;
import com.mykola.schedule.data.network.pojo.ResponceLessons;
import com.mykola.schedule.data.network.pojo.ResponceSearchGroups;
import com.mykola.schedule.data.network.pojo.ResponceWeek;
import com.mykola.schedule.data.storage.models.LessonDTO;
import com.mykola.schedule.utils.Constants;
import com.mykola.schedule.utils.Loger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
        determinateLessons();
    }


    public void loadScheduleOfWeek() {
        setLessons(managerDB.readLessonsFromDB(weekNumber));
        determinateLessons();
    }

    /**
     * Визначає номер тижня
     */
    private void determineNumberWeek() {
        Calendar c = Calendar.getInstance(Locale.UK);
        int week = c.get(Calendar.WEEK_OF_YEAR);
        Loger.LOG("week = " + week);

        boolean conformity = managerPreferences.readConformityWeek();
        Loger.LOG("Conformity = " + conformity);
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


    /**
     * Визначає поточний день та поточну пару
     */
    private void determinateLessons() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");

        int currentDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;

        for (Collection<LessonDTO> tLessons : lessons.values()) {
            for (LessonDTO lesson : tLessons) {
                try {
                    Date startTime = df.parse(lesson.getTimeStart());
                    Date endTime = df.parse(lesson.getTimeEnd());
                    Date thisTime = df.parse(df.format(calendar.getTime()));

                    if ((currentDay == Integer.parseInt(lesson.getDayNumber())) &&
                            (Integer.parseInt(lesson.getLessonWeek()) == currentWeek)) {
                        if (thisTime.after(startTime) && thisTime.before(endTime)) {
                            lesson.setCurrentLesson(true);
                        }

                        lesson.setCurrentDay(true);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

    }


}

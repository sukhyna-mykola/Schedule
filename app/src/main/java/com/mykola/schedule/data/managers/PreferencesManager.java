package com.mykola.schedule.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.mykola.schedule.utils.Constants;

import static android.content.Context.MODE_PRIVATE;


public class PreferencesManager {

    private static final String SHARED_PREFERENCES = "schedule.preferences";

    private static final String LOGIN_KEY = "LOGIN_KEY";
    private static final String GROUP_KEY = "GROUP_KEY";
    private static final String PARITY_WEEK_KEY = "PARITY_WEEK_KEY";
    
    private SharedPreferences sPref;

    public PreferencesManager(Context context) {
        sPref = context.getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
    }

    public boolean readStatusLogin() {
        return sPref.getBoolean(LOGIN_KEY, false);
    }

    public boolean readConformityWeek() {
        return sPref.getBoolean(PARITY_WEEK_KEY, false);
    }

    public String readGroupName() {
        return sPref.getString(GROUP_KEY, null);
    }


    public void saveConformityWeek(boolean parity) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(PARITY_WEEK_KEY, parity);
        ed.apply();
    }

    public void saveGroupName(String name) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(GROUP_KEY, name);
        ed.apply();
    }

    public void saveStatusLogin(boolean statusLogin) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(LOGIN_KEY, statusLogin);
        ed.apply();
    }
}

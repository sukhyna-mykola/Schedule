package com.mykola.schedule.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

import com.mykola.schedule.utils.Constants;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mykola on 14.03.17.
 */

public class PreferencesManager {
    private SharedPreferences sPref;

    public PreferencesManager(Context context) {
        sPref = context.getSharedPreferences(Constants.SHARED_PREFERENCES, MODE_PRIVATE);
    }

    public boolean readStatusLogin() {
        return sPref.getBoolean(Constants.LOGIN_KEY, false);
    }

    public boolean readConformityWeek() {
        return sPref.getBoolean(Constants.PARITY_WEEK_KEY, false);
    }

    public String readGroupName() {
        return sPref.getString(Constants.GROUP_KEY, "GROUPE");
    }


    public void saveConformityWeek(boolean parity) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.PARITY_WEEK_KEY, parity);
        ed.commit();

    }

    public void saveGroupName(String name) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(Constants.GROUP_KEY, name);
        ed.commit();
    }

    public void saveStatusLogin(boolean statusLogin) {
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(Constants.LOGIN_KEY, statusLogin);
        ed.commit();
    }
}

package com.mykola.schedule.utils;

import android.util.Log;

/**
 * Created by mykola on 04.03.17.
 */

public class Loger {

    public static final String TAG = "shedule";

    public static void LOG(Object text){
        Log.d(TAG,text.toString());
    }

}

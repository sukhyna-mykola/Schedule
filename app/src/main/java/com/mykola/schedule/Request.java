package com.mykola.schedule;

/**
 * Created by mykola on 20.02.17.
 */

public interface Request {
    void responceWeek(int responceCode);
    void responceGroupsHint(int responceCode,String [] names);
    void responceSchedule(int responceCode);
}

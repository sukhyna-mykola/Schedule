package com.mykola.schedule.data.network;


import com.mykola.schedule.data.network.pojo.ResponceLessons;
import com.mykola.schedule.data.network.pojo.ResponceSearchGroups;
import com.mykola.schedule.data.network.pojo.ResponceWeek;

import retrofit2.Call;
import retrofit2.Response;

public interface APICallbacks {

    void onResponseSheduleFromServer(String query,Call<ResponceLessons> call, Response<ResponceLessons> response);
    void onFailureSheduleFromServer(Call<ResponceLessons> call, Throwable t);

    void onResponseWeekFromServer(Call<ResponceWeek> call, Response<ResponceWeek> response);
    void onFailureWeekFromServer(Call<ResponceWeek> call, Throwable t);

    void onResponseSearchGroups(Call<ResponceSearchGroups> call, Response<ResponceSearchGroups> response);
    void onFailureSearchGroups(Call<ResponceSearchGroups> call, Throwable t);
}

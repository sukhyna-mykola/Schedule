package com.mykola.schedule.data.network;

import com.mykola.schedule.data.network.pojo.ResponceLessons;
import com.mykola.schedule.data.network.pojo.ResponceSearchGroups;
import com.mykola.schedule.data.network.pojo.ResponceWeek;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface APIService{
    @GET("groups/{group_name}/lessons")
    Call<ResponceLessons> getLessons(@Path("group_name") String groupName);

    @GET("weeks")
    Call<ResponceWeek> getWeek();

    @GET("groups/?")
    Call<ResponceSearchGroups> searchGroupsByName(@Query("search") String filterJSON);

}

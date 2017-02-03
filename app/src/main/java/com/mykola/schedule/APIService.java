package com.mykola.schedule;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by mykola on 31.01.17.
 */

public interface APIService {
    @GET("groups/{group_name}/lessons")
    Call<ResponceLessons> getLessons(@Path("group_name") String groupName);

    @GET("weeks")
    Call<ResponceWeek> getWeek();

    @GET("groups/?")
    Call<ResponceSearchGroups> searchGroupsByName(@Query("search") String filterJSON);

}

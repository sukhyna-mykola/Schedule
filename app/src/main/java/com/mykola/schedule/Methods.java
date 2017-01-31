package com.mykola.schedule;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by mykola on 31.01.17.
 */

public interface Methods {
    @GET("groups/{group_name}/lessons")
    Call<ResponceLessons> getLessons(@Path("group_name") String groupName);

    @GET("weeks")
    Call<ResponceWeek> getWeek();

}

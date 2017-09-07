package com.mykola.schedule.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceBuilder {


    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://api.rozklad.org.ua/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    private static APIService myApi = retrofit.create(APIService.class);


    public static APIService getApi() {
        return myApi;
    }
}

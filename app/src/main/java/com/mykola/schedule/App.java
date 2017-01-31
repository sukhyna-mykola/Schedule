package com.mykola.schedule;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mykola on 01.02.17.
 */

public class App extends Application {

    private static Methods myApi;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://api.rozklad.org.ua/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        myApi = retrofit.create(Methods.class);
    }

    public static Methods getApi() {
        return myApi;
    }
}

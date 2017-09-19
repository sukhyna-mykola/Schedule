package com.mykola.schedule.data.network;


import retrofit2.Call;
import retrofit2.Response;

public interface APICallbacks <T>{
    void onResponse(Call<T> call, Response<T> response);
    void onFailure(Call<T> call, Throwable t);
}

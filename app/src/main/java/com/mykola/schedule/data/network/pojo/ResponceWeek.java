package com.mykola.schedule.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponceWeek {

    @SerializedName("data")
    @Expose
    private Integer data;


    public Integer getData() {
        return data;
    }

    public void setData(Integer data) {
        this.data = data;
    }
}

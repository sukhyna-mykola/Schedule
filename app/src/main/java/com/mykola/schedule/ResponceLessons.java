package com.mykola.schedule;

/**
 * Created by mykola on 16.01.17.
 */


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponceLessons extends ResponceHttp {

    @SerializedName("data")
    @Expose
    private List<Lesson> data = null;

    public List<Lesson> getData() {
        return data;
    }

    public void setData(List<Lesson> data) {
        this.data = data;
    }

}




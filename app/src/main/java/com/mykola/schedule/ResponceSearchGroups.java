package com.mykola.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mykola on 31.01.17.
 */

public class ResponceSearchGroups {

    @SerializedName("data")
    @Expose
    private List<Group> data = null;


    public List<Group> getData() {
        return data;
    }

    public void setData(List<Group> data) {
        this.data = data;
    }
}

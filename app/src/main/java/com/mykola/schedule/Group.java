package com.mykola.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mykola on 02.02.17.
 */

public class Group {

    @SerializedName("group_full_name")
    @Expose
    private String groupFullName;


    public String getGroupFullName() {
        return groupFullName;
    }

    public void setGroupFullName(String groupFullName) {
        this.groupFullName = groupFullName;
    }



}


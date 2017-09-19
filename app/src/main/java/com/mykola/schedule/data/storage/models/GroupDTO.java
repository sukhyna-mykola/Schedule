package com.mykola.schedule.data.storage.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GroupDTO {

    @SerializedName("group_full_name")
    @Expose
    private String groupFullName;


    public String getGroupFullName() {
        return groupFullName;
    }


}


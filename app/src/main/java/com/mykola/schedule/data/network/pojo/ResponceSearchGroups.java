package com.mykola.schedule.data.network.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mykola.schedule.data.storage.models.GroupDTO;

import java.util.List;


public class ResponceSearchGroups {

    @SerializedName("data")
    @Expose
    private List<GroupDTO> data = null;


    public List<GroupDTO> getData() {
        return data;
    }

    public void setData(List<GroupDTO> data) {
        this.data = data;
    }
}

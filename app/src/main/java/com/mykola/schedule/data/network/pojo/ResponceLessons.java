package com.mykola.schedule.data.network.pojo;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mykola.schedule.data.storage.models.LessonDTO;

public class ResponceLessons  {

    @SerializedName("data")
    @Expose
    private List<LessonDTO> data = null;

    public List<LessonDTO> getData() {
        return data;
    }

    public void setData(List<LessonDTO> data) {
        this.data = data;
    }

}




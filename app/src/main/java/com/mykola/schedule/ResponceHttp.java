package com.mykola.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mykola on 01.02.17.
 */

public abstract class ResponceHttp {
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("timeStamp")
    @Expose
    private Integer timeStamp;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("debugInfo")
    @Expose
    private String debugInfo;
    @SerializedName("meta")
    @Expose
    private Object meta;


    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Integer timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugInfo() {
        return debugInfo;
    }

    public void setDebugInfo(String debugInfo) {
        this.debugInfo = debugInfo;
    }

    public Object getMeta() {
        return meta;
    }

    public void setMeta(Object meta) {
        this.meta = meta;
    }


}

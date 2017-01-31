package com.mykola.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by mykola on 01.02.17.
 */

public class Room {

    @SerializedName("room_id")
    @Expose
    private String roomId;
    @SerializedName("room_name")
    @Expose
    private String roomName;
    @SerializedName("room_latitude")
    @Expose
    private String roomLatitude;
    @SerializedName("room_longitude")
    @Expose
    private String roomLongitude;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomLatitude() {
        return roomLatitude;
    }

    public void setRoomLatitude(String roomLatitude) {
        this.roomLatitude = roomLatitude;
    }

    public String getRoomLongitude() {
        return roomLongitude;
    }

    public void setRoomLongitude(String roomLongitude) {
        this.roomLongitude = roomLongitude;
    }


}


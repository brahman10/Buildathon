package com.example.buildathon.Model;

import java.io.Serializable;

public class RoomModel implements Serializable {
    String name , description , createdby , roomid;

    public RoomModel(String name, String description, String createdby, String roomid) {
        this.name = name;
        this.description = description;
        this.createdby = createdby;
        this.roomid = roomid;
    }

    public RoomModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getRoomid() {
        return roomid;
    }

    public void setRoomid(String roomid) {
        this.roomid = roomid;
    }
}

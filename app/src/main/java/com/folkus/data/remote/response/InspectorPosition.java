package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class InspectorPosition {
    @SerializedName("position_id")
    private int position_id;

    @SerializedName("position")
    private String position;

    @SerializedName("active")
    private int active;

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}

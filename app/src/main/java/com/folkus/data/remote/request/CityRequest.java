package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class CityRequest {
    @SerializedName("state_id")
    private String state_id;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }
}

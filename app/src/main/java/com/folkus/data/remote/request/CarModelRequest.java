package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class CarModelRequest {
    @SerializedName("makers_id")
    private String  makers_id;

    public String getMakers_id() {
        return makers_id;
    }

    public void setMakers_id(String makers_id) {
        this.makers_id = makers_id;
    }
}

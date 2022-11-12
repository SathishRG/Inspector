package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class ZipRequest {
    @SerializedName("city_id")
    private String city_id;

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }
}

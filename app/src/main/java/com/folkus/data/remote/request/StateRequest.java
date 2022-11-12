package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class StateRequest {
    @SerializedName("country_id")
    private String country_id;

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String city_id) {
        this.country_id = city_id;
    }
}

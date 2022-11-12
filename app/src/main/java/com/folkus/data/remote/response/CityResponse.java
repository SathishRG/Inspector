package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CityResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private ArrayList<City> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<City> getData() {
        return data;
    }

    public void setData(ArrayList<City> data) {
        this.data = data;
    }
}

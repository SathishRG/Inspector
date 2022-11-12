package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CountResponse {
    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<CountData> getData() {
        return data;
    }

    public void setData(ArrayList<CountData> data) {
        this.data = data;
    }

    @SerializedName("data")
    private ArrayList<CountData> data;
}

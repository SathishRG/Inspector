package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ZipCodeResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private ArrayList<ZipCode> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<ZipCode> getData() {
        return data;
    }

    public void setData(ArrayList<ZipCode> data) {
        this.data = data;
    }
}

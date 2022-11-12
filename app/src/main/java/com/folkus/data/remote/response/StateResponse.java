package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class StateResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private ArrayList<StateData> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<StateData> getData() {
        return data;
    }

    public void setData(ArrayList<StateData> data) {
        this.data = data;
    }
}

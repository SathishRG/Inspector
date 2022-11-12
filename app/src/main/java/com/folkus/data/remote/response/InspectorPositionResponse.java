package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InspectorPositionResponse {
    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<InspectorPosition> getData() {
        return data;
    }

    public void setData(ArrayList<InspectorPosition> data) {
        this.data = data;
    }

    @SerializedName("data")
    private ArrayList<InspectorPosition> data;

}

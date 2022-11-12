package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PendingInspectionResponse {
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<PendingInspectionData> getData() {
        return data;
    }

    public void setData(ArrayList<PendingInspectionData> data) {
        this.data = data;
    }

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    ArrayList<PendingInspectionData> data;
}

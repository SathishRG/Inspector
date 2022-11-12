package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InspectionTiresWheelResponse {
    @SerializedName("success")
    boolean success;

    @SerializedName("data")
    InspectionTiresWheelData data;

    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(InspectionTiresWheelData  data) {
        this.data = data;
    }
    public InspectionTiresWheelData  getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

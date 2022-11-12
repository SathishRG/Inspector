package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InspectionMechanicalResponse {
    @SerializedName("success")
    boolean success;

    @SerializedName("data")
    InspectionMechanicalData data;

    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(InspectionMechanicalData data) {
        this.data = data;
    }

    public InspectionMechanicalData getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

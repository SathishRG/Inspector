package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InspectionMechanicalVideoResponse {
    @SerializedName("success")
    boolean success;

    @SerializedName("data")
    ArrayList<MechanicalVideoImagesData> data;

    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<MechanicalVideoImagesData> getData() {
        return data;
    }

    public void setData(ArrayList<MechanicalVideoImagesData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PowerTrainAddImageResponse {
    @SerializedName("success")
    boolean success;

    @SerializedName("data")
    ArrayList<PowerTrainImagesData> data;

    @SerializedName("message")
    String message;

    public ArrayList<PowerTrainImagesData> getData() {
        return data;
    }

    public void setData(ArrayList<PowerTrainImagesData> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

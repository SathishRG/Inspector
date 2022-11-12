package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TestDriveAddImageResponse {
    @SerializedName("success")
    boolean success;

    @SerializedName("data")
    ArrayList<TestDriveImagesData> data;

    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<TestDriveImagesData> getData() {
        return data;
    }

    public void setData(ArrayList<TestDriveImagesData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

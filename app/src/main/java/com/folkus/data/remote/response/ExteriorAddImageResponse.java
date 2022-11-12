package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExteriorAddImageResponse {
    @SerializedName("success")
    boolean success;

    @SerializedName("data")
    ArrayList<ExteriorImagesData> data;

    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<ExteriorImagesData> getData() {
        return data;
    }

    public void setData(ArrayList<ExteriorImagesData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

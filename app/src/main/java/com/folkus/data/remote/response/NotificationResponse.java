package com.folkus.data.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationResponse {
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<NotificationData> getData() {
        return data;
    }

    public void setData(ArrayList<NotificationData> data) {
        this.data = data;
    }

    public LoginError getError() {
        return error;
    }

    public void setError(LoginError error) {
        this.error = error;
    }

    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("data")
    private ArrayList<NotificationData> data;

    @Expose
    @SerializedName("error")
    private LoginError error;
}

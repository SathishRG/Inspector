package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CancelReasonDropDownResponse {

    @SerializedName("success")
    boolean success;

    @SerializedName("data")
    ArrayList<CancelReasonDropDownData> data;

    @SerializedName("error")
    LoginError error;

    public LoginError getError() {
        return error;
    }

    public void setError(LoginError error) {
        this.error = error;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setData(ArrayList<CancelReasonDropDownData> data) {
        this.data = data;
    }

    public ArrayList<CancelReasonDropDownData> getData() {
        return data;
    }
}

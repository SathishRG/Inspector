package com.folkus.data.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RejectReasonDropDownResponse {


    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("data")
    private ArrayList<RejectReasonDropDownData> data;

    public void setError(LoginError error) {
        this.error = error;
    }

    @Expose
    @SerializedName("error")
    private LoginError error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<RejectReasonDropDownData> getData() {
        return data;
    }

    public void setData(ArrayList<RejectReasonDropDownData> data) {
        this.data = data;
    }

    public LoginError getError() {
        return error;
    }
}

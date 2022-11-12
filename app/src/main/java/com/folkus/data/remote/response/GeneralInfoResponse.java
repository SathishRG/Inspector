package com.folkus.data.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GeneralInfoResponse {

    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("data")
    private GeneralInfoData data;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("error")
    private LoginError error;

    public LoginError getError() {
        return error;
    }

    public void setError(LoginError error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public GeneralInfoData getData() {
        return data;
    }

    public void setData(GeneralInfoData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

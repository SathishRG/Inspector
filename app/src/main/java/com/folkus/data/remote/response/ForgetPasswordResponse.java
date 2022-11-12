package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class ForgetPasswordResponse {
    @SerializedName("success")
    public boolean success;

    @SerializedName("error")
    public ForgetPasswordError forgetPasswordError;

    @SerializedName("data")
    public int data;

    @SerializedName("message")
    public String message;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ForgetPasswordError getForgetPasswordError() {
        return forgetPasswordError;
    }

    public void setForgetPasswordError(ForgetPasswordError forgetPasswordError) {
        this.forgetPasswordError = forgetPasswordError;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}

package com.folkus.data.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SellerDetailsResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private ArrayList<SellerDetails> data;

    @Expose
    @SerializedName("error")
    private LoginError error;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<SellerDetails> getData() {
        return data;
    }

    public void setData(ArrayList<SellerDetails> data) {
        this.data = data;
    }

    public LoginError getError() {
        return error;
    }

    public void setError(LoginError error) {
        this.error = error;
    }
}

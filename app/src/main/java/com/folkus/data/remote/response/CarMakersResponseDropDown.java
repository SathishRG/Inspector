package com.folkus.data.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CarMakersResponseDropDown {

    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("data")
    private ArrayList<CatMakersData> data;

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

    public ArrayList<CatMakersData> getData() {
        return data;
    }

    public void setData(ArrayList<CatMakersData> data) {
        this.data = data;
    }

    public LoginError getError() {
        return error;
    }
}

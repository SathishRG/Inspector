package com.folkus.data.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddVehicleResponse {
    @Expose
    @SerializedName("success")
    private boolean success;

    @Expose
    @SerializedName("data")
    private VehicleData data;

    @Expose
    @SerializedName("message")
    private String message;

    @Expose
    @SerializedName("error")
    private LoginError error;

    public boolean isSuccess() {
        return success;
    }

    public VehicleData getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public LoginError getError() {
        return error;
    }

    public void setError(LoginError loginError) {
        error = loginError;
    }
}

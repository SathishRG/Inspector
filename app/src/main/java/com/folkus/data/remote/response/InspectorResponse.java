package com.folkus.data.remote.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InspectorResponse {
    public boolean getSuccess() {
        return success;
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
    @SerializedName("error")
    private LoginError error;
}

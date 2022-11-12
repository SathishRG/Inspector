package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoginResponse {

    public boolean getSuccess() {
        return success;
    }

    public LoginData getData() {
        return data;
    }

    public LoginError getError() {
        return error;
    }

    @SerializedName("success")
    public boolean success;

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @SerializedName("data")
    public LoginData data;

    public void setError(LoginError error) {
        this.error = error;
    }

    @SerializedName("error")
    public LoginError error;
}
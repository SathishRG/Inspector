package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordError {
    @SerializedName("PasswordError" )
    private String passwordError;

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }
}

package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class ChangePasswordResponse {
    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ChangePasswordError getChangePasswordError() {
        return changePasswordError;
    }

    public void setChangePasswordError(ChangePasswordError changePasswordError) {
        this.changePasswordError = changePasswordError;
    }

    @SerializedName("error")
    private ChangePasswordError changePasswordError;
}

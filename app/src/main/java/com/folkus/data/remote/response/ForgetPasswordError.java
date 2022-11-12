package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class ForgetPasswordError {
    @SerializedName("mailError" )
    private String mailError;

    public String getMailError() {
        return mailError;
    }

    public void setMailError(String mailError) {
        this.mailError = mailError;
    }
}

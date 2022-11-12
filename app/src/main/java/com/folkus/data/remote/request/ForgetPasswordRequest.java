package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class ForgetPasswordRequest {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("email" )
    private String email;

}

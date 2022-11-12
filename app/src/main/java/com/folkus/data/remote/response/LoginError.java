package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class LoginError {
    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @SerializedName("err")
    public String err;
}

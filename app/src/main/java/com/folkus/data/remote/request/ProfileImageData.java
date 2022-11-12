package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class ProfileImageData {
    @SerializedName("type")
    String type;

    @SerializedName("base64")
    String base64;


    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
    public String getBase64() {
        return base64;
    }
}

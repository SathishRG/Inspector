package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class TestDriveAddImageData {
    @SerializedName("name")
    String name;

    @SerializedName("type")
    String type;

    @SerializedName("size")
    String size;

    @SerializedName("base64")
    String base64;


    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public String getSize() {
        return size;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }
    public String getBase64() {
        return base64;
    }
}

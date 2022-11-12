package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class CarModelData {

    @SerializedName("model_id")
    private int model_id;

    @SerializedName("model_name")
    private String model_name;

    @SerializedName("makers_id")
    private int makers_id;

    public int getModel_id() {
        return model_id;
    }

    public void setModel_id(int model_id) {
        this.model_id = model_id;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

    public int getMakers_id() {
        return makers_id;
    }

    public void setMakers_id(int makers_id) {
        this.makers_id = makers_id;
    }
}

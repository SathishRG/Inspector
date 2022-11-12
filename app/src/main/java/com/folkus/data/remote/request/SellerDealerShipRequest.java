package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class SellerDealerShipRequest {
    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

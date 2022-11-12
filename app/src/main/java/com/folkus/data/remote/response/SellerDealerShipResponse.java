package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SellerDealerShipResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private ArrayList<SellerDealerShip> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ArrayList<SellerDealerShip> getData() {
        return data;
    }

    public void setData(ArrayList<SellerDealerShip> data) {
        this.data = data;
    }
}

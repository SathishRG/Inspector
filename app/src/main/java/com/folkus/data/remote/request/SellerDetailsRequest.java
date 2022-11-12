package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class SellerDetailsRequest {
    @SerializedName("seller_info_id" )
    private int seller_info_id;

    public int getSeller_info_id() {
        return seller_info_id;
    }

    public void setSeller_info_id(int seller_info_id) {
        this.seller_info_id = seller_info_id;
    }
}

package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class InspectionRequestData {
    public int getSeller_info_id() {
        return seller_info_id;
    }

    public void setSeller_info_id(int seller_info_id) {
        this.seller_info_id = seller_info_id;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNo_cars() {
        return no_cars;
    }

    public void setNo_cars(int no_cars) {
        this.no_cars = no_cars;
    }

    @SerializedName("seller_info_id" )
    public int seller_info_id;

    @SerializedName("dealer_name" )
    public String dealer_name;

    @SerializedName("phone_no" )
    public String phone_no;

    @SerializedName("address" )
    public String address;

    @SerializedName("no_cars" )
    public int no_cars;
}

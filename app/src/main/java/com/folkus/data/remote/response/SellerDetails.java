package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class SellerDetails {
    @SerializedName("seller_dealer_id")
    private int seller_dealer_id;

    @SerializedName("dealer_name")
    private String dealer_name;

    @SerializedName("phone_no")
    private String phone_no;

    @SerializedName("address")
    private String address;

    @SerializedName("no_cars")
    private int no_cars;

    @SerializedName("comments")
    private String comments;

    @SerializedName("vin_no")
    private String vin_no;

    @SerializedName("year")
    private int year;

    @SerializedName("make")
    private String make;

    @SerializedName("model")
    private String model;

    public int getSeller_dealer_id() {
        return seller_dealer_id;
    }

    public void setSeller_dealer_id(int seller_dealer_id) {
        this.seller_dealer_id = seller_dealer_id;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getVin_no() {
        return vin_no;
    }

    public void setVin_no(String vin_no) {
        this.vin_no = vin_no;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}

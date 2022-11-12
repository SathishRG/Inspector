package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class AddVehicleRequest {
    @SerializedName("seller_dealer_id")
    private String seller_dealer_id;

    @SerializedName("address")
    private String address;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("updatedBy")
    private String updatedBy;

    @SerializedName("state_id")
    private String state_id;

    @SerializedName("city_id")
    private String city_id;

    @SerializedName("zipcode_id")
    private String zipcode_id;

    @SerializedName("vin_no")
    private String vin_no;

    @SerializedName("year")
    private String year;

    @SerializedName("make")
    private String make;

    @SerializedName("model")
    private String model;

    @SerializedName("comments")
    private String comments;

    @SerializedName("date")
    private String date;

    @SerializedName("time")
    private String time;

    @SerializedName("inspector_id")
    private String inspector_id;

    public void setSeller_dealer_id(String seller_dealer_id) {
        this.seller_dealer_id = seller_dealer_id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public void setZipcode_id(String zipcode_id) {
        this.zipcode_id = zipcode_id;
    }

    public void setVin_no(String vin_no) {
        this.vin_no = vin_no;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setInspector_id(String inspector_id) {
        this.inspector_id = inspector_id;
    }
}

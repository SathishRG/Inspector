package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class SellerDealerShip {
    @SerializedName("seller_dealer_id")
    private int seller_dealer_id;

    @SerializedName("dealer_name")
    private String dealer_name;

    @SerializedName("active")
    private int active;

    @SerializedName("address")
    private String address;

    @SerializedName("state_id")
    private int state_id;

    @SerializedName("city_id")
    private int city_id;

    @SerializedName("zipcode_id")
    private int zipcode_id;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("dealer_type_id")
    private int dealer_type_id;

    @SerializedName("image")
    private String image;

    @SerializedName("phone_no")
    private String phone_no;

    @SerializedName("email")
    private String email;

    @SerializedName("cell")
    private String cell;

    @SerializedName("customer_type")
    private String customer_type;

    @SerializedName("organisation")
    private String organisation;

    @SerializedName("no_years")
    private String no_years;

    @SerializedName("status")
    private String status;

    @SerializedName("comments")
    private String comments;

    @SerializedName("ssn_fedid")
    private String ssn_fedid;

    @SerializedName("standing_id")
    private int standing_id;

    public int getSeller_dealer_id() {
        return seller_dealer_id;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public int getActive() {
        return active;
    }

    public String getAddress() {
        return address;
    }

    public int getState_id() {
        return state_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public int getZipcode_id() {
        return zipcode_id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public int getDealer_type_id() {
        return dealer_type_id;
    }

    public String getImage() {
        return image;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public String getEmail() {
        return email;
    }

    public String getCell() {
        return cell;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public String getOrganisation() {
        return organisation;
    }

    public String getNo_years() {
        return no_years;
    }

    public String getStatus() {
        return status;
    }

    public String getComments() {
        return comments;
    }

    public String getSsn_fedid() {
        return ssn_fedid;
    }

    public int getStanding_id() {
        return standing_id;
    }
}

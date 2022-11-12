package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class LoginData {

    public int getInspector_id() {
        return inspector_id;
    }

    public String getName() {
        return name;
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

    public String getPhone_no() {
        return phone_no;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getImage() {
        return image;
    }

    public int getPosition_id() {
        return position_id;
    }

    public String getDob() {
        return dob;
    }

    public int getExperience() {
        return experience;
    }

    public int getForgotpassword() {
        return forgotpassword;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public int getActive() {
        return active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public String getComments() {
        return comments;
    }

    public String getLocation() {
        return location;
    }

    @SerializedName("inspector_id")
    private int inspector_id;

    @SerializedName("name")
    private String name;

    @SerializedName("address")
    private String address;

    @SerializedName("state_id")
    private int state_id;

    @SerializedName("city_id")
    private int city_id;

    @SerializedName("zipcode_id")
    private int zipcode_id;

    @SerializedName("phone_no")
    private String phone_no;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    @SerializedName("image")
    private String image;

    @SerializedName("position_id")
    private int position_id;

    @SerializedName("dob")
    private String dob;

    @SerializedName("experience")
    private int experience;

    @SerializedName("forgotpassword")
    private int forgotpassword;

    @SerializedName("createdBy")
    private int createdBy;

    @SerializedName("updatedBy")
    private int updatedBy;

    @SerializedName("active")
    private int active;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("status")
    private String status;

    @SerializedName("comments")
    private String comments;

    @SerializedName("location")
    private String location;
}

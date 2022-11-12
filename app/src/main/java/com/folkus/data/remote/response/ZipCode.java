package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class ZipCode {
    @SerializedName("zipcode_id")
    private int zipcode_id;

    @SerializedName("zipcode")
    private int zipcode;

    @SerializedName("city_id")
    private int city_id;

    @SerializedName("active")
    private int active;

    @SerializedName("state_id")
    private String state_id;

    @SerializedName("comments")
    private String comments;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    public int getZipcode_id() {
        return zipcode_id;
    }

    public void setZipcode_id(int zipcode_id) {
        this.zipcode_id = zipcode_id;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

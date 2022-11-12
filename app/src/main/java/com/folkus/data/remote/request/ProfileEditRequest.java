package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileEditRequest {
    @SerializedName("image")
    ArrayList<ProfileImageData> image;

    @SerializedName("phone_no")
    String phoneNo;

    @SerializedName("address")
    String address;

    @SerializedName("dob")
    String dob;

    @SerializedName("inspector_id")
    String inspectorId;


    public void setImage(ArrayList<ProfileImageData> image) {
        this.image = image;
    }

    public ArrayList<ProfileImageData> getImage() {
        return image;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDob() {
        return dob;
    }

    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
    }

    public String getInspectorId() {
        return inspectorId;
    }
}

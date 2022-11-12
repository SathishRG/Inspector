package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class InteriorTabRequest {
    @SerializedName("car_id")
    String carId;

    @SerializedName("seller_dealer_id")
    String sellerDealerId;

    @SerializedName("inspector_id")
    String inspectorId;

    @SerializedName("visible_damage")
    String visibleDamage;

    @SerializedName("front_seat")
    String frontSeat;

    @SerializedName("back_seat")
    String backSeat;

    @SerializedName("major_damage")
    String majorDamage;

    @SerializedName("comments")
    String comments;


    public void setCarId(String carId) {
        this.carId = carId;
    }
    public String getCarId() {
        return carId;
    }

    public void setSellerDealerId(String sellerDealerId) {
        this.sellerDealerId = sellerDealerId;
    }
    public String getSellerDealerId() {
        return sellerDealerId;
    }

    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
    }
    public String getInspectorId() {
        return inspectorId;
    }

    public void setVisibleDamage(String visibleDamage) {
        this.visibleDamage = visibleDamage;
    }
    public String getVisibleDamage() {
        return visibleDamage;
    }

    public void setFrontSeat(String frontSeat) {
        this.frontSeat = frontSeat;
    }
    public String getFrontSeat() {
        return frontSeat;
    }

    public void setBackSeat(String backSeat) {
        this.backSeat = backSeat;
    }
    public String getBackSeat() {
        return backSeat;
    }

    public void setMajorDamage(String majorDamage) {
        this.majorDamage = majorDamage;
    }
    public String getMajorDamage() {
        return majorDamage;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getComments() {
        return comments;
    }
}

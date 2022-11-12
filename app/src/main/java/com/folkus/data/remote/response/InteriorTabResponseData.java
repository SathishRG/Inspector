package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class InteriorTabResponseData {

    @SerializedName("car_id")
    int carId;

    @SerializedName("seller_dealer_id")
    int sellerDealerId;

    @SerializedName("inspector_id")
    int inspectorId;

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


    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getCarId() {
        return carId;
    }

    public void setSellerDealerId(int sellerDealerId) {
        this.sellerDealerId = sellerDealerId;
    }

    public int getSellerDealerId() {
        return sellerDealerId;
    }

    public void setInspectorId(int inspectorId) {
        this.inspectorId = inspectorId;
    }

    public int getInspectorId() {
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

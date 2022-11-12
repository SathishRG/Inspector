package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class InspectionPowerTrainTabData {
    @SerializedName("car_id")
    int carId;

    @SerializedName("seller_dealer_id")
    int sellerDealerId;

    @SerializedName("inspector_id")
    int inspectorId;

    @SerializedName("noise")
    String noise;

    @SerializedName("auto_transmission")
    String autoTransmission;

    @SerializedName("transfer_case")
    String transferCase;

    @SerializedName("front_door")
    String frontDoor;

    @SerializedName("differential")
    String differential;

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

    public void setNoise(String noise) {
        this.noise = noise;
    }

    public String getNoise() {
        return noise;
    }

    public void setAutoTransmission(String autoTransmission) {
        this.autoTransmission = autoTransmission;
    }

    public String getAutoTransmission() {
        return autoTransmission;
    }

    public void setTransferCase(String transferCase) {
        this.transferCase = transferCase;
    }

    public String getTransferCase() {
        return transferCase;
    }

    public void setFrontDoor(String frontDoor) {
        this.frontDoor = frontDoor;
    }

    public String getFrontDoor() {
        return frontDoor;
    }

    public void setDifferential(String differential) {
        this.differential = differential;
    }

    public String getDifferential() {
        return differential;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }
}

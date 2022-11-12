package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class InspectionPowerTrainRequest {
    @SerializedName("car_id")
    String carId;

    @SerializedName("seller_dealer_id")
    String sellerDealerId;

    @SerializedName("inspector_id")
    String inspectorId;

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

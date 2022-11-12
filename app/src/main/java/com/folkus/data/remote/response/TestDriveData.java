package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class TestDriveData {
    @SerializedName("car_id")
    int carId;

    @SerializedName("seller_dealer_id")
    int sellerDealerId;

    @SerializedName("inspector_id")
    int inspectorId;

    @SerializedName("automatic_transmission")
    String automaticTransmission;

    @SerializedName("manual_transmission")
    String manualTransmission;

    @SerializedName("exilator_level")
    String exilatorLevel;

    @SerializedName("breaking_senses")
    String breakingSenses;

    @SerializedName("steering_controls")
    String steeringControls;

    @SerializedName("transfer_case")
    String transferCase;

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

    public void setAutomaticTransmission(String automaticTransmission) {
        this.automaticTransmission = automaticTransmission;
    }

    public String getAutomaticTransmission() {
        return automaticTransmission;
    }

    public void setManualTransmission(String manualTransmission) {
        this.manualTransmission = manualTransmission;
    }

    public String getManualTransmission() {
        return manualTransmission;
    }

    public void setExilatorLevel(String exilatorLevel) {
        this.exilatorLevel = exilatorLevel;
    }

    public String getExilatorLevel() {
        return exilatorLevel;
    }

    public void setBreakingSenses(String breakingSenses) {
        this.breakingSenses = breakingSenses;
    }

    public String getBreakingSenses() {
        return breakingSenses;
    }

    public void setSteeringControls(String steeringControls) {
        this.steeringControls = steeringControls;
    }

    public String getSteeringControls() {
        return steeringControls;
    }

    public void setTransferCase(String transferCase) {
        this.transferCase = transferCase;
    }

    public String getTransferCase() {
        return transferCase;
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

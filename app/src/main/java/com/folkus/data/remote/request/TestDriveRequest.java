package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class TestDriveRequest {
    @SerializedName("car_id")
    String carId;

    @SerializedName("seller_dealer_id")
    String sellerDealerId;

    @SerializedName("inspector_id")
    String inspectorId;

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

    @SerializedName("no_ads")
    String noAds;

    @SerializedName("no_srs")
    String noSrs;

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

    public void setNoAds(String noAds) {
        this.noAds = noAds;
    }
    public String getNoAds() {
        return noAds;
    }

    public void setNoSrs(String noSrs) {
        this.noSrs = noSrs;
    }
    public String getNoSrs() {
        return noSrs;
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

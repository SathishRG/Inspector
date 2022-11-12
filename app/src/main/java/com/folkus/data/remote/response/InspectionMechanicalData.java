package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class InspectionMechanicalData {

    @SerializedName("car_id")
    int carId;

    @SerializedName("seller_dealer_id")
    int sellerDealerId;

    @SerializedName("inspector_id")
    int inspectorId;

    @SerializedName("upper_end")
    String upperEnd;

    @SerializedName("bottam_end")
    String bottamEnd;

    @SerializedName("catalytic_converter")
    String catalyticConverter;

    @SerializedName("heater_runs")
    String heaterRuns;

    @SerializedName("ac_runs")
    String acRuns;

    @SerializedName("engine_light")
    String engineLight;

    @SerializedName("no_ads")
    String noAds;

    @SerializedName("no_srs")
    String noSrs;

    @SerializedName("differential_operation")
    String differentialOperation;

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

    public void setUpperEnd(String upperEnd) {
        this.upperEnd = upperEnd;
    }

    public String getUpperEnd() {
        return upperEnd;
    }

    public void setBottamEnd(String bottamEnd) {
        this.bottamEnd = bottamEnd;
    }

    public String getBottamEnd() {
        return bottamEnd;
    }

    public void setCatalyticConverter(String catalyticConverter) {
        this.catalyticConverter = catalyticConverter;
    }

    public String getCatalyticConverter() {
        return catalyticConverter;
    }

    public void setHeaterRuns(String heaterRuns) {
        this.heaterRuns = heaterRuns;
    }

    public String getHeaterRuns() {
        return heaterRuns;
    }

    public void setAcRuns(String acRuns) {
        this.acRuns = acRuns;
    }

    public String getAcRuns() {
        return acRuns;
    }

    public void setEngineLight(String engineLight) {
        this.engineLight = engineLight;
    }

    public String getEngineLight() {
        return engineLight;
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

    public void setDifferentialOperation(String differentialOperation) {
        this.differentialOperation = differentialOperation;
    }

    public String getDifferentialOperation() {
        return differentialOperation;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }
}

package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class GeneralInfoRequest {
    @SerializedName("car_id")
    int carId;
    @SerializedName("seller_dealer_id")
    int sellerDealerId;
    @SerializedName("inspector_id")
    int InspectorId;
    @SerializedName("inspection_date")
    String InspectionDate;
    @SerializedName("vin_no")
    String vinNO;
    @SerializedName("year")
    String year;
    @SerializedName("make")
    String make;
    @SerializedName("model")
    String model;
    @SerializedName("odometer")
    String odometer;
    @SerializedName("engine_start")
    String engineStart;
    @SerializedName("driveable")
    String driveable;
    @SerializedName("comments")
    String comments;

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public int getSellerDealerId() {
        return sellerDealerId;
    }

    public void setSellerDealerId(int sellerDealerId) {
        this.sellerDealerId = sellerDealerId;
    }

    public int getInspectorId() {
        return InspectorId;
    }

    public void setInspectorId(int inspectorId) {
        InspectorId = inspectorId;
    }

    public String getInspectionDate() {
        return InspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        InspectionDate = inspectionDate;
    }

    public String getVinNO() {
        return vinNO;
    }

    public void setVinNO(String vinNO) {
        this.vinNO = vinNO;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getEngineStart() {
        return engineStart;
    }

    public void setEngineStart(String engineStart) {
        this.engineStart = engineStart;
    }

    public String getDriveable() {
        return driveable;
    }

    public void setDriveable(String driveable) {
        this.driveable = driveable;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

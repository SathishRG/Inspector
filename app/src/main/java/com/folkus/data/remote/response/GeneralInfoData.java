package com.folkus.data.remote.response;

import androidx.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GeneralInfoData implements Serializable {
    @SerializedName("car_id")
    int carId;

    @SerializedName("seller_dealer_id")
    int sellerDealerId;

    @SerializedName("inspector_id")
    int inspectorId;

    @SerializedName("vin_no")
    String vinNo;

    @SerializedName("year")
    int year;

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

    public void setVinNo(String vinNo) {
        this.vinNo = vinNo;
    }
    public String getVinNo() {
        return vinNo;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public int getYear() {
        return year;
    }

    public void setMake(String make) {
        this.make = make;
    }
    public String getMake() {
        return make;
    }

    public void setModel(String model) {
        this.model = model;
    }
    public String getModel() {
        return model;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }
    public String getOdometer() {
        return odometer;
    }

    public void setEngineStart(String engineStart) {
        this.engineStart = engineStart;
    }
    public String getEngineStart() {
        return engineStart;
    }

    public void setDriveable(String driveable) {
        this.driveable = driveable;
    }
    public String getDriveable() {
        return driveable;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getComments() {
        return comments;
    }
}


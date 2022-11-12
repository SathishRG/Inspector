package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class InspectionTiresWheelData {
    @SerializedName("id")
    int id;

    @SerializedName("car_id")
    int carId;

    @SerializedName("seller_dealer_id")
    int sellerDealerId;

    @SerializedName("inspector_id")
    int inspectorId;

    @SerializedName("tread_depth")
    String treadDepth;

    @SerializedName("four_tires_condition")
    String fourTiresCondition;

    @SerializedName("scratches")
    String scratches;

    @SerializedName("comments")
    String comments;

    @SerializedName("active")
    int active;

    @SerializedName("updatedAt")
    String updatedAt;

    @SerializedName("createdAt")
    String createdAt;


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

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

    public void setTreadDepth(String treadDepth) {
        this.treadDepth = treadDepth;
    }

    public String getTreadDepth() {
        return treadDepth;
    }

    public void setFourTiresCondition(String fourTiresCondition) {
        this.fourTiresCondition = fourTiresCondition;
    }

    public String getFourTiresCondition() {
        return fourTiresCondition;
    }

    public void setScratches(String scratches) {
        this.scratches = scratches;
    }

    public String getScratches() {
        return scratches;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getActive() {
        return active;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

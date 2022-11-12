package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class InspectionTiresWheelsRequest {
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
}

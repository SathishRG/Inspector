package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class InspectionCancelRequest {
    @SerializedName("car_id")
    String carId;
    @SerializedName("ins_cancel_reason_id")
    String insCancelReasonId;
    @SerializedName("comments")
    String comments;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getInsCancelReasonId() {
        return insCancelReasonId;
    }

    public void setInsCancelReasonId(String insCancelReasonId) {
        this.insCancelReasonId = insCancelReasonId;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

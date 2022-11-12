package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class InspectionCancelResponseData {
    @SerializedName("ins_cancel_reason_id")
    int insCancelReasonId;

    @SerializedName("ins_cancel_reason")
    String insCancelReason;

    @SerializedName("active")
    int active;


    public void setInsCancelReasonId(int insCancelReasonId) {
        this.insCancelReasonId = insCancelReasonId;
    }
    public int getInsCancelReasonId() {
        return insCancelReasonId;
    }

    public void setInsCancelReason(String insCancelReason) {
        this.insCancelReason = insCancelReason;
    }
    public String getInsCancelReason() {
        return insCancelReason;
    }

    public void setActive(int active) {
        this.active = active;
    }
    public int getActive() {
        return active;
    }
}

package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class RejectReasonDropDownData {

    @SerializedName("reject_id")
    private int reject_id;

    @SerializedName("rejectreason")
    private String rejectreason;

    @SerializedName("active")
    private int active;

    public int getReject_id() {
        return reject_id;
    }

    public void setReject_id(int reject_id) {
        this.reject_id = reject_id;
    }

    public String getRejectreason() {
        return rejectreason;
    }

    public void setRejectreason(String rejectreason) {
        this.rejectreason = rejectreason;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}

package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class CancelReasonDropDownData {

    @SerializedName("ins_cancel_reason_id")
    private int ins_cancel_reason_id;

    @SerializedName("ins_cancel_reason")
    private String ins_cancel_reason;

    @SerializedName("active")
    private int active;

    public int getIns_cancel_reason_id() {
        return ins_cancel_reason_id;
    }

    public void setIns_cancel_reason_id(int ins_cancel_reason_id) {
        this.ins_cancel_reason_id = ins_cancel_reason_id;
    }

    public String getIns_cancel_reason() {
        return ins_cancel_reason;
    }

    public void setIns_cancel_reason(String ins_cancel_reason) {
        this.ins_cancel_reason = ins_cancel_reason;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}

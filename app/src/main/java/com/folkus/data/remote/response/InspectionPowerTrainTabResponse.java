package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class InspectionPowerTrainTabResponse {
    @SerializedName("success")
    boolean success;

    @SerializedName("data")
    InspectionPowerTrainTabData inspectionPowerTrainTabData;

    @SerializedName("message")
    String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setInspectionPowerTrainTabData(InspectionPowerTrainTabData inspectionPowerTrainTabData) {
        this.inspectionPowerTrainTabData = inspectionPowerTrainTabData;
    }
    public InspectionPowerTrainTabData getInspectionPowerTrainTabData() {
        return inspectionPowerTrainTabData;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

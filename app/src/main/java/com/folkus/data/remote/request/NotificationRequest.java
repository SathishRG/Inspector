package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class NotificationRequest {
    public String getInspector_id() {
        return inspector_id;
    }

    public void setInspector_id(String inspector_id) {
        this.inspector_id = inspector_id;
    }

    @SerializedName("inspector_id")
    private String inspector_id;
}

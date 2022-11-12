package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class DeleteAllNotificationRequest {
    @SerializedName("inspector_id")
    private String inspector_id;

    public String getInspector_id() {
        return inspector_id;
    }

    public void setInspector_id(String inspector_id) {
        this.inspector_id = inspector_id;
    }
}

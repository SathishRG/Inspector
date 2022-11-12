package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class CountRequest {
    public int getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(int inspectorId) {
        this.inspectorId = inspectorId;
    }

    @SerializedName("inspector_id" )
    private int inspectorId;
}

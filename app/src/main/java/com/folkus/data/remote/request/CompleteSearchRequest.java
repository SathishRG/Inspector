package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class CompleteSearchRequest {

    @SerializedName("inspector_id")
    String inspectorId;

    @SerializedName("search")
    String search;


    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
    }

    public String getInspectorId() {
        return inspectorId;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSearch() {
        return search;
    }
}

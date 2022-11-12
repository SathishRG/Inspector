package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class VehicleData {
    @SerializedName("notification_id")
    private int notification_id;

    @SerializedName("seller_dealer_id")
    private String seller_dealer_id;

    @SerializedName("seller_info_id")
    private int seller_info_id;

    @SerializedName("inspector_id")
    private String inspector_id;

    @SerializedName("title")
    private String title;

    @SerializedName("type")
    private String type;

    @SerializedName("active")
    private int active;

    @SerializedName("date")
    private String date;

    @SerializedName("message")
    private String message;

    @SerializedName("updatedBy")
    private String updatedBy;

    @SerializedName("createdBy")
    private String createdBy;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("createdAt")
    private String createdAt;

    public int getNotification_id() {
        return notification_id;
    }

    public String getSeller_dealer_id() {
        return seller_dealer_id;
    }

    public int getSeller_info_id() {
        return seller_info_id;
    }

    public String getInspector_id() {
        return inspector_id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public int getActive() {
        return active;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}

package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class NotificationData {
    @SerializedName("notification_id")
    private int notification_id;

    @SerializedName("seller_info_id")
    private int seller_info_id;

    @SerializedName("seller_dealer_id")
    private int seller_dealer_id;

    @SerializedName("inspector_id")
    private int inspector_id;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("active")
    private int active;

    @SerializedName("date")
    private String date;

    @SerializedName("createdBy")
    private int createdBy;

    @SerializedName("updatedBy")
    private int updatedBy;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("dealer_name")
    private String dealer_name;

    @SerializedName("time")
    private int time;

    @SerializedName("count")
    private int count;

    public int getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public int getSeller_info_id() {
        return seller_info_id;
    }

    public void setSeller_info_id(int seller_info_id) {
        this.seller_info_id = seller_info_id;
    }

    public int getSeller_dealer_id() {
        return seller_dealer_id;
    }

    public void setSeller_dealer_id(int seller_dealer_id) {
        this.seller_dealer_id = seller_dealer_id;
    }

    public int getInspector_id() {
        return inspector_id;
    }

    public void setInspector_id(int inspector_id) {
        this.inspector_id = inspector_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}

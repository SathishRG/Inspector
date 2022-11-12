package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class DeleteNotificationRequest {
    @SerializedName("notification_id")
    private String notification_id;

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }
}

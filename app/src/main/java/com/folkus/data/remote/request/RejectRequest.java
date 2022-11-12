package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class RejectRequest {

    @SerializedName("seller_info_id")
    private int seller_info_id;

    @SerializedName("status")
    private String status;

    @SerializedName("reject_id")
    private int reject_id;

    @SerializedName("comments")
    private String comments;

    public int getSeller_info_id() {
        return seller_info_id;
    }

    public void setSeller_info_id(int seller_info_id) {
        this.seller_info_id = seller_info_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getReject_id() {
        return reject_id;
    }

    public void setReject_id(int reject_id) {
        this.reject_id = reject_id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}

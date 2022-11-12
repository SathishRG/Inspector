package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

public class ExteriorTabRequest {
    @SerializedName("car_id")
    String carId;

    @SerializedName("seller_dealer_id")
    String sellerDealerId;

    @SerializedName("inspector_id")
    String inspectorId;

    @SerializedName("visible_rust")
    String visibleRust;

    @SerializedName("color_fade")
    String colorFade;

    @SerializedName("glass_damage")
    String glassDamage;

    @SerializedName("scratches")
    String scratches;

    @SerializedName("side_mirror")
    String sideMirror;

    @SerializedName("comments")
    String comments;


    public void setCarId(String carId) {
        this.carId = carId;
    }
    public String getCarId() {
        return carId;
    }

    public void setSellerDealerId(String sellerDealerId) {
        this.sellerDealerId = sellerDealerId;
    }
    public String getSellerDealerId() {
        return sellerDealerId;
    }

    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
    }
    public String getInspectorId() {
        return inspectorId;
    }

    public void setVisibleRust(String visibleRust) {
        this.visibleRust = visibleRust;
    }
    public String getVisibleRust() {
        return visibleRust;
    }

    public void setColorFade(String colorFade) {
        this.colorFade = colorFade;
    }
    public String getColorFade() {
        return colorFade;
    }

    public void setGlassDamage(String glassDamage) {
        this.glassDamage = glassDamage;
    }
    public String getGlassDamage() {
        return glassDamage;
    }

    public void setScratches(String scratches) {
        this.scratches = scratches;
    }
    public String getScratches() {
        return scratches;
    }

    public void setSideMirror(String sideMirror) {
        this.sideMirror = sideMirror;
    }
    public String getSideMirror() {
        return sideMirror;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
    public String getComments() {
        return comments;
    }
}

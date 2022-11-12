package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InteriorAddImageRequest {
    @SerializedName("image_type")
    String imageType;

    @SerializedName("iamge_type")
    String iamgeType;

    @SerializedName("car_id")
    String carId;

    @SerializedName("others")
    String others;

    @SerializedName("image")
    ArrayList<InteriorAddImageData> image;

    @SerializedName("page")
    String page;


    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setIamgeType(String iamgeType) {
        this.iamgeType = iamgeType;
    }

    public String getIamgeType() {
        return iamgeType;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarId() {
        return carId;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getOthers() {
        return others;
    }

    public void setImage(ArrayList<InteriorAddImageData> image) {
        this.image = image;
    }

    public ArrayList<InteriorAddImageData> getImage() {
        return image;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }
}

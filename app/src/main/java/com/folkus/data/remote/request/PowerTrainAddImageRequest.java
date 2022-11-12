package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PowerTrainAddImageRequest {
    @SerializedName("image_type")
    String imageType;

    @SerializedName("iamge_type")
    String iamgeType;

    @SerializedName("car_id")
    String carId;

    @SerializedName("others")
    int others;

    @SerializedName("page")
    String page;

    @SerializedName("image")
    ArrayList<PowerTrainAddImagesData> image;


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

    public void setOthers(int others) {
        this.others = others;
    }

    public int getOthers() {
        return others;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPage() {
        return page;
    }

    public void setImage(ArrayList<PowerTrainAddImagesData> image) {
        this.image = image;
    }

    public ArrayList<PowerTrainAddImagesData> getImage() {
        return image;
    }
}

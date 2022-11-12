package com.folkus.data.remote.request;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AddExteriorImagesRequest {
    @SerializedName("image_type")
    String imageType;
    @SerializedName("iamge_type")
    String iamgeType;
    @SerializedName("car_id")
    int carId;
    @SerializedName("others")
    String others;
    @SerializedName("image")
    ArrayList<AddExteriorImage> image;
    @SerializedName("page")
    String page;

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getIamgeType() {
        return iamgeType;
    }

    public void setIamgeType(String iamgeType) {
        this.iamgeType = iamgeType;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public ArrayList<AddExteriorImage> getImage() {
        return image;
    }

    public void setImage(ArrayList<AddExteriorImage> image) {
        this.image = image;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}

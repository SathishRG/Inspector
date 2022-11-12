package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class PowerTrainImagesData {
    @SerializedName("car_images_id")
    int carImagesId;

    @SerializedName("car_id")
    int carId;

    @SerializedName("car_image_type_id")
    int carImageTypeId;

    @SerializedName("image_type_id")
    int imageTypeId;

    @SerializedName("format")
    String format;

    @SerializedName("image")
    String image;

    @SerializedName("others")
    String others;

    @SerializedName("active")
    int active;

    @SerializedName("createdAt")
    String createdAt;

    @SerializedName("updatedAt")
    String updatedAt;


    public void setCarImagesId(int carImagesId) {
        this.carImagesId = carImagesId;
    }
    public int getCarImagesId() {
        return carImagesId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }
    public int getCarId() {
        return carId;
    }

    public void setCarImageTypeId(int carImageTypeId) {
        this.carImageTypeId = carImageTypeId;
    }
    public int getCarImageTypeId() {
        return carImageTypeId;
    }

    public void setImageTypeId(int imageTypeId) {
        this.imageTypeId = imageTypeId;
    }
    public int getImageTypeId() {
        return imageTypeId;
    }

    public void setFormat(String format) {
        this.format = format;
    }
    public String getFormat() {
        return format;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public String getImage() {
        return image;
    }

    public void setOthers(String others) {
        this.others = others;
    }
    public String getOthers() {
        return others;
    }

    public void setActive(int active) {
        this.active = active;
    }
    public int getActive() {
        return active;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    public String getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }

}

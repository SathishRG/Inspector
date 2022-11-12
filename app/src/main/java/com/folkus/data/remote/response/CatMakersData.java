package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

public class CatMakersData {

    @SerializedName("id")
    private int id;

    @SerializedName("car_name")
    private String car_name;

    @SerializedName("country_id")
    private int country_id;

    @SerializedName("description")
    private String description;

    public int getId() {
        return id;
    }

    public String getCar_name() {
        return car_name;
    }

    public int getCountry_id() {
        return country_id;
    }

    public String getDescription() {
        return description;
    }
}

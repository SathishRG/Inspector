package com.folkus.data.remote.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CompletedInspection implements Serializable {
    @SerializedName("seller_dealer_id")
    private int seller_dealer_id;

    @SerializedName("dealer_name")
    private String dealer_name;

    @SerializedName("active")
    private String active;

    @SerializedName("address")
    private String address;

    @SerializedName("state_id")
    private int state_id;

    @SerializedName("city_id")
    private int city_id;

    @SerializedName("zipcode_id")
    private int zipcode_id;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("updatedAt")
    private String updatedAt;

    @SerializedName("dealer_type_id")
    private int dealer_type_id;

    @SerializedName("image")
    private String image;

    @SerializedName("phone_no")
    private String phone_no;

    @SerializedName("email")
    private String email;

    @SerializedName("cell")
    private String cell;

    @SerializedName("customer_type")
    private String customer_type;

    @SerializedName("organisation")
    private String organisation;

    @SerializedName("no_years")
    private String no_years;

    @SerializedName("status")
    private String status;

    @SerializedName("comments")
    private String comments;

    @SerializedName("ssn_fedid")
    private String ssn_fedid;

    @SerializedName("standing_id")
    private int standing_id;

    @SerializedName("reference_id")
    private int reference_id;

    @SerializedName("fax_no")
    private String fax_no;

    @SerializedName("reference_source")
    private String reference_source;

    @SerializedName("alternative_phone")
    private String alternative_phone;

    @SerializedName("promo_method_id")
    private int promo_method_id;

    @SerializedName("standing_date")
    private String standing_date;

    @SerializedName("seller_info_id")
    private int seller_info_id;

    @SerializedName("no_cars")
    private int no_cars;

    @SerializedName("createdBy")
    private int createdBy;

    @SerializedName("updatedBy")
    private int updatedBy;

    @SerializedName("map_id")
    private int map_id;

    @SerializedName("inspector_id")
    private int inspector_id;

    @SerializedName("reject_id")
    private int reject_id;

    @SerializedName("date")
    private String date;

    @SerializedName("approved")
    private String approved;

    @SerializedName("id")
    private int id;

    @SerializedName("car_id")
    private int car_id;

    @SerializedName("location")
    private String location;

    @SerializedName("time")
    private String time;

    @SerializedName("vin_no")
    private String vin_no;

    @SerializedName("year")
    private int year;

    @SerializedName("make")
    private String make;

    @SerializedName("model")
    private String model;

    @SerializedName("inventory_no")
    private int inventory_no;

    @SerializedName("ins_cancel_reason_id")
    private String ins_cancel_reason_id;

    @SerializedName("inspection_date")
    private String inspection_date;

    @SerializedName("odometer")
    private String odometer;

    @SerializedName("engine_start")
    private String engine_start;

    @SerializedName("driveable")
    private String driveable;

    @SerializedName("page")
    private String page;

    @SerializedName("exterior_image_status")
    private String exterior_image_status;

    @SerializedName("exterior_panel_image_status")
    private String exterior_panel_image_status;

    @SerializedName("interior_image_status")
    private String interior_image_status;

    @SerializedName("image_admin_comments")
    private String image_admin_comments;

    public int getSeller_dealer_id() {
        return seller_dealer_id;
    }

    public void setSeller_dealer_id(int seller_dealer_id) {
        this.seller_dealer_id = seller_dealer_id;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getState_id() {
        return state_id;
    }

    public void setState_id(int state_id) {
        this.state_id = state_id;
    }

    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public int getZipcode_id() {
        return zipcode_id;
    }

    public void setZipcode_id(int zipcode_id) {
        this.zipcode_id = zipcode_id;
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

    public int getDealer_type_id() {
        return dealer_type_id;
    }

    public void setDealer_type_id(int dealer_type_id) {
        this.dealer_type_id = dealer_type_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getCustomer_type() {
        return customer_type;
    }

    public void setCustomer_type(String customer_type) {
        this.customer_type = customer_type;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getNo_years() {
        return no_years;
    }

    public void setNo_years(String no_years) {
        this.no_years = no_years;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSsn_fedid() {
        return ssn_fedid;
    }

    public void setSsn_fedid(String ssn_fedid) {
        this.ssn_fedid = ssn_fedid;
    }

    public int getStanding_id() {
        return standing_id;
    }

    public void setStanding_id(int standing_id) {
        this.standing_id = standing_id;
    }

    public int getReference_id() {
        return reference_id;
    }

    public void setReference_id(int reference_id) {
        this.reference_id = reference_id;
    }

    public String getFax_no() {
        return fax_no;
    }

    public void setFax_no(String fax_no) {
        this.fax_no = fax_no;
    }

    public String getReference_source() {
        return reference_source;
    }

    public void setReference_source(String reference_source) {
        this.reference_source = reference_source;
    }

    public String getAlternative_phone() {
        return alternative_phone;
    }

    public void setAlternative_phone(String alternative_phone) {
        this.alternative_phone = alternative_phone;
    }

    public int getPromo_method_id() {
        return promo_method_id;
    }

    public void setPromo_method_id(int promo_method_id) {
        this.promo_method_id = promo_method_id;
    }

    public String getStanding_date() {
        return standing_date;
    }

    public void setStanding_date(String standing_date) {
        this.standing_date = standing_date;
    }

    public int getSeller_info_id() {
        return seller_info_id;
    }

    public void setSeller_info_id(int seller_info_id) {
        this.seller_info_id = seller_info_id;
    }

    public int getNo_cars() {
        return no_cars;
    }

    public void setNo_cars(int no_cars) {
        this.no_cars = no_cars;
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

    public int getMap_id() {
        return map_id;
    }

    public void setMap_id(int map_id) {
        this.map_id = map_id;
    }

    public int getInspector_id() {
        return inspector_id;
    }

    public void setInspector_id(int inspector_id) {
        this.inspector_id = inspector_id;
    }

    public int getReject_id() {
        return reject_id;
    }

    public void setReject_id(int reject_id) {
        this.reject_id = reject_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCar_id() {
        return car_id;
    }

    public void setCar_id(int car_id) {
        this.car_id = car_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVin_no() {
        return vin_no;
    }

    public void setVin_no(String vin_no) {
        this.vin_no = vin_no;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getInventory_no() {
        return inventory_no;
    }

    public void setInventory_no(int inventory_no) {
        this.inventory_no = inventory_no;
    }

    public String getIns_cancel_reason_id() {
        return ins_cancel_reason_id;
    }

    public void setIns_cancel_reason_id(String ins_cancel_reason_id) {
        this.ins_cancel_reason_id = ins_cancel_reason_id;
    }

    public String getInspection_date() {
        return inspection_date;
    }

    public void setInspection_date(String inspection_date) {
        this.inspection_date = inspection_date;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getEngine_start() {
        return engine_start;
    }

    public void setEngine_start(String engine_start) {
        this.engine_start = engine_start;
    }

    public String getDriveable() {
        return driveable;
    }

    public void setDriveable(String driveable) {
        this.driveable = driveable;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getExterior_image_status() {
        return exterior_image_status;
    }

    public void setExterior_image_status(String exterior_image_status) {
        this.exterior_image_status = exterior_image_status;
    }

    public String getExterior_panel_image_status() {
        return exterior_panel_image_status;
    }

    public void setExterior_panel_image_status(String exterior_panel_image_status) {
        this.exterior_panel_image_status = exterior_panel_image_status;
    }

    public String getInterior_image_status() {
        return interior_image_status;
    }

    public void setInterior_image_status(String interior_image_status) {
        this.interior_image_status = interior_image_status;
    }

    public String getImage_admin_comments() {
        return image_admin_comments;
    }

    public void setImage_admin_comments(String image_admin_comments) {
        this.image_admin_comments = image_admin_comments;
    }
}

package com.folkus.data.remote;

public class Endpoints {
    public static final String BASE_URL = "http://service-devinspector.ur2ndlot.com";
    public static final String PATH = BASE_URL+"/urs2ndlot_inspector/v1/";
    // Login & Signup
    public static final String LOGIN = PATH + "inspector/login";
    public static final String FORGOT_PASSWORD = PATH + "mobforgotPassword/condition";
    public static final String COUNTRY = PATH + "country";
    public static final String ADD_INSPECTOR = PATH + "inspector/add";
    public static final String CHANGE_PASSWORD = PATH + "mobchangepassword/update";
    public static final String PROFILE_UPDATE = PATH + "inspectoredit/update";
    public static final String COUNT_DATA = PATH + "inspectioncount/condition";
    public static final String PENDING_INSPECTION_DATA = PATH + "pendinginspection/condition/";
    public static final String COMPLETED_INSPECTION_DATA = PATH + "completedinspection/condition/";
    public static final String THIS_MONTH_COMPLETED_INSPECTION_DATA = PATH + "thismonthinspection/condition";
    public static final String THIS_MONTH_REOPEN_INSPECTION_DATA = PATH + "reopeninspection/condition";
    public static final String NOTIFICATION_API = PATH + "notificationDetails/condition/";
    public static final String API_INSPECTION_REQUEST = PATH + "urpostDeatils/condition/";
    public static final String API_INSPECTOR_PROFILE = PATH + "inspector_profile/condition/";
    public static final String API_REQUEST_DETAILS = PATH + "urpostDeatils/condition/";
    public static final String API_ADD_VEHICLES = PATH + "addvehicle/add/";
    public static final String API_INSPECTOR_POSITION = PATH + "position/condition/";
    public static final String API_CAR_MAKERS = PATH + "car_makers/condition/";
    public static final String API_CAR_MODELS = PATH + "car_model/condition/";
    public static final String API_DELETE_NOTIFICATION = PATH + "deletenotification/update/";
    public static final String API_DELETE_ALL_NOTIFICATION = PATH + "/alldeletenotification/update/";
    public static final String API_STATE_LIST = PATH + "state/condition/";
    public static final String API_CITY_LIST = PATH + "city/condition/";
    public static final String API_ZIP_CODE_LIST = PATH + "zipcode/condition/";
    public static final String API_CAR_INSPECTION = PATH + "inspections/condition/";
    public static final String API_ADD_CAR_IMAGES = PATH + "car_images/add/";
    public static final String API_CAR_INSPECTION_GENERAL_INFO = PATH + "inspectiongeneralinfo/add/";
    public static final String API_CAR_INSPECTION_POWER_TRAIN = PATH + "inscarpowertrain/add/";
    public static final String API_CAR_INSPECTION_MECHANICAL = PATH + "inscarmechanical/add/";
    public static final String API_CAR_INSPECTION_TIRES_WHEELS= PATH + "inscartireswheels/add/";
    public static final String API_CAR_INSPECTION_EXTERIOR= PATH + "inscarexterior/add/";
    public static final String API_CAR_INSPECTION_INTERIOR= PATH + "inscarinterior/add/";
    public static final String API_CAR_INSPECTION_TEST_DRIVE= PATH + "inscartestdrive/add/";
    public static final String API_INSPECTION_CANCEL_REASON_DROP_DOWN = PATH + "inscancelreason/condition/";
    public static final String API_CAR_REQUEST_STATUS= PATH + "requeststatus/update/";
    public static final String API_REJECT_REASON_DROP_DOWN = PATH + "rejectreason/condition/";
    public static final String API_CANCEL_INSPECTION= PATH + "cancelinspection/update/";
    public static final String API_SELLER_DEALERSHIP_NAME= PATH + "sellerdealership/condition/";
    public static final String API_INSPECTION_REQUEST_SELLER_INFO = PATH + "inspectionrequest/condition/";
    public static final String API_REOPEN_INSPECTION= PATH + "reopeninspection/condition/";
    public static final String API_THIS_MONTH_INSPECTION= PATH + "thismonthinspection/condition/";
    public static final String API_CAR_LIST= PATH + "urpostList/condition/";
    public static final String API_URPOST_DEATILS_SEARCH= PATH + "urpostDeatilssearch/condition/";
    public static final String API_PENDING_DEATILS_SEARCH= PATH + "pendingsearch/condition/";
    public static final String API_REOPEN_DEATILS_SEARCH= PATH + "reopensearch/condition/";
    public static final String API_COMPLETED_DEATILS_SEARCH= PATH + "completedsearch/condition/";
    public static final String API_THIS_MONTH_COMPLETED_DEATILS_SEARCH= PATH + "thismonthcompletedsearch/condition/";
    public static final String API_URL_STATUS= PATH + "urlstatus/condition/";
}
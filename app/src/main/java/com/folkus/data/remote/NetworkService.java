package com.folkus.data.remote;

import com.folkus.data.remote.request.AcceptRequest;
import com.folkus.data.remote.request.AddExteriorImagesRequest;
import com.folkus.data.remote.request.AddInspectorRequest;
import com.folkus.data.remote.request.AddVehicleRequest;
import com.folkus.data.remote.request.CarModelRequest;
import com.folkus.data.remote.request.ChangePasswordRequest;
import com.folkus.data.remote.request.CityRequest;
import com.folkus.data.remote.request.CompleteSearchRequest;
import com.folkus.data.remote.request.CountRequest;
import com.folkus.data.remote.request.DeleteAllNotificationRequest;
import com.folkus.data.remote.request.DeleteNotificationRequest;
import com.folkus.data.remote.request.ExteriorAddImageRequest;
import com.folkus.data.remote.request.ExteriorTabRequest;
import com.folkus.data.remote.request.ForgetPasswordRequest;
import com.folkus.data.remote.request.GeneralInfoRequest;
import com.folkus.data.remote.request.InspectionCancelRequest;
import com.folkus.data.remote.request.InspectionMechanicalRequest;
import com.folkus.data.remote.request.InspectionMechanicalVideoRequest;
import com.folkus.data.remote.request.InspectionPowerTrainRequest;
import com.folkus.data.remote.request.InspectionTiresWheelImageRequest;
import com.folkus.data.remote.request.InspectionTiresWheelsRequest;
import com.folkus.data.remote.request.InteriorAddImageRequest;
import com.folkus.data.remote.request.InteriorTabRequest;
import com.folkus.data.remote.request.LoginRequest;
import com.folkus.data.remote.request.MonthSearchRequest;
import com.folkus.data.remote.request.NotificationRequest;
import com.folkus.data.remote.request.PendingSearchRequest;
import com.folkus.data.remote.request.PowerTrainAddImageRequest;
import com.folkus.data.remote.request.ProfileEditRequest;
import com.folkus.data.remote.request.RejectRequest;
import com.folkus.data.remote.request.ReopenSearchRequest;
import com.folkus.data.remote.request.SellerDealerShipRequest;
import com.folkus.data.remote.request.SellerDetailsRequest;
import com.folkus.data.remote.request.StateRequest;
import com.folkus.data.remote.request.TestDriveAddImageRequest;
import com.folkus.data.remote.request.TestDriveRequest;
import com.folkus.data.remote.request.ZipRequest;
import com.folkus.data.remote.response.AcceptResponse;
import com.folkus.data.remote.response.AddExteriorImagesResponse;
import com.folkus.data.remote.response.AddVehicleResponse;
import com.folkus.data.remote.response.CancelInspectionResponse;
import com.folkus.data.remote.response.CancelReasonDropDownResponse;
import com.folkus.data.remote.response.CarMakersResponseDropDown;
import com.folkus.data.remote.response.CarModelResponse;
import com.folkus.data.remote.response.ChangePasswordResponse;
import com.folkus.data.remote.response.CityResponse;
import com.folkus.data.remote.response.CompletedInspectionResponse;
import com.folkus.data.remote.response.CountResponse;
import com.folkus.data.remote.response.DeleteAllNotificationResponse;
import com.folkus.data.remote.response.DeleteNotificationResponse;
import com.folkus.data.remote.response.ExteriorAddImageResponse;
import com.folkus.data.remote.response.ExteriorTabResponse;
import com.folkus.data.remote.response.ForgetPasswordResponse;
import com.folkus.data.remote.response.GeneralInfoResponse;
import com.folkus.data.remote.response.InspectionMechanicalResponse;
import com.folkus.data.remote.response.InspectionMechanicalVideoResponse;
import com.folkus.data.remote.response.InspectionPowerTrainTabResponse;
import com.folkus.data.remote.response.InspectionRequestResponse;
import com.folkus.data.remote.response.InspectionTiresWheelImageResponse;
import com.folkus.data.remote.response.InspectionTiresWheelResponse;
import com.folkus.data.remote.response.InspectorPositionResponse;
import com.folkus.data.remote.response.InspectorResponse;
import com.folkus.data.remote.response.InteriorAddImageResponse;
import com.folkus.data.remote.response.InteriorTabResponse;
import com.folkus.data.remote.response.LoginResponse;
import com.folkus.data.remote.response.NotificationResponse;
import com.folkus.data.remote.response.PowerTrainAddImageResponse;
import com.folkus.data.remote.response.ProfileEditResponse;
import com.folkus.data.remote.response.RejectReasonDropDownResponse;
import com.folkus.data.remote.response.RejectResponse;
import com.folkus.data.remote.response.SellerDealerShipResponse;
import com.folkus.data.remote.response.SellerDetailsResponse;
import com.folkus.data.remote.response.StateResponse;
import com.folkus.data.remote.response.TestDriveAddImageResponse;
import com.folkus.data.remote.response.TestDriveResponse;
import com.folkus.data.remote.response.ZipCodeResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NetworkService {
    @POST(Endpoints.LOGIN)
    Call<LoginResponse> doLoginCall(@Body LoginRequest request);

    @POST(Endpoints.COUNT_DATA)
    Call<CountResponse> getCountData(@Body CountRequest request);

    @POST(Endpoints.CHANGE_PASSWORD)
    Call<ChangePasswordResponse> doChangePasswordCall(@Body ChangePasswordRequest request);

    @POST(Endpoints.FORGOT_PASSWORD)
    Call<ForgetPasswordResponse> doForgotPasswordCall(@Body ForgetPasswordRequest request);

    @POST(Endpoints.API_INSPECTION_REQUEST)
    Call<InspectionRequestResponse> getInspectionRequestData(@Body CountRequest request);

//    @POST(Endpoints.PENDING_INSPECTION_DATA)
//    Call<PendingInspectionResponse> getPendingInspectionData(@Body CountRequest request);

    @POST(Endpoints.PENDING_INSPECTION_DATA)
    Call<CompletedInspectionResponse> getPendingInspectionData(@Body CountRequest request);

    @POST(Endpoints.COMPLETED_INSPECTION_DATA)
    Call<CompletedInspectionResponse> getCompletedInspectionData(@Body CountRequest request);

    @POST(Endpoints.THIS_MONTH_COMPLETED_INSPECTION_DATA)
    Call<CompletedInspectionResponse> getThisMonthCompletedInspectionData(@Body CountRequest request);

    @POST(Endpoints.THIS_MONTH_REOPEN_INSPECTION_DATA)
    Call<CompletedInspectionResponse> getThisMonthReopenedInspectionData(@Body CountRequest request);

    @POST(Endpoints.NOTIFICATION_API)
    Call<NotificationResponse> getNotificationDetails(@Body NotificationRequest request);

    @POST(Endpoints.API_DELETE_NOTIFICATION)
    Call<DeleteNotificationResponse> deleteNotification(@Body DeleteNotificationRequest request);

    @POST(Endpoints.API_DELETE_ALL_NOTIFICATION)
    Call<DeleteAllNotificationResponse> deleteAllNotification(@Body DeleteAllNotificationRequest request);

    @POST(Endpoints.API_SELLER_DEALERSHIP_NAME)
    Call<SellerDealerShipResponse> getSellerDealershipNameList(@Body SellerDealerShipRequest request);

    @POST(Endpoints.API_SELLER_DEALERSHIP_NAME)
    Call<SellerDealerShipResponse> getSellerDealershipNameList();

    @POST(Endpoints.API_INSPECTION_REQUEST_SELLER_INFO)
    Call<SellerDetailsResponse> getInspectionRequestSellerDetail(@Body SellerDetailsRequest request);

    @POST(Endpoints.API_STATE_LIST)
    Call<StateResponse> getStateList(@Body StateRequest request);

    @POST(Endpoints.API_CITY_LIST)
    Call<CityResponse> getCityList(@Body CityRequest request);

    @POST(Endpoints.API_ZIP_CODE_LIST)
    Call<ZipCodeResponse> getZipCodeList(@Body ZipRequest request);

    @POST(Endpoints.API_INSPECTOR_POSITION)
    Call<InspectorPositionResponse> getInspectorPosition();

    @POST(Endpoints.ADD_INSPECTOR)
    Call<InspectorResponse> addInspector(@Body AddInspectorRequest request);

    @POST(Endpoints.API_CAR_REQUEST_STATUS)
    Call<AcceptResponse> acceptRequest(@Body AcceptRequest request);

    @POST(Endpoints.API_CAR_REQUEST_STATUS)
    Call<RejectResponse> rejectRequest(@Body RejectRequest request);

    @POST(Endpoints.API_REJECT_REASON_DROP_DOWN)
    Call<RejectReasonDropDownResponse> getRejectReasonDropDown();

    @POST(Endpoints.API_INSPECTION_CANCEL_REASON_DROP_DOWN)
    Call<CancelReasonDropDownResponse> getCancelReasonDropDown();

    @POST(Endpoints.API_CANCEL_INSPECTION)
    Call<CancelInspectionResponse> cancelInspection(@Body InspectionCancelRequest request);

    @POST(Endpoints.API_CAR_MAKERS)
    Call<CarMakersResponseDropDown> getCarMakersList();

    @POST(Endpoints.API_CAR_MODELS)
    Call<CarModelResponse> getCarModelList(@Body CarModelRequest request);

    @POST(Endpoints.API_ADD_VEHICLES)
    Call<AddVehicleResponse> addVehicle(@Body AddVehicleRequest request);

    @POST(Endpoints.API_CAR_INSPECTION_GENERAL_INFO)
    Call<GeneralInfoResponse> submitGeneralInfoApi(@Body GeneralInfoRequest request);

    @POST(Endpoints.API_ADD_CAR_IMAGES)
    Call<AddExteriorImagesResponse> addExteriorImagesApi(@Body AddExteriorImagesRequest request);

    /* Inspection Power Train*/
    @POST(Endpoints.API_CAR_INSPECTION_POWER_TRAIN)
    Call<InspectionPowerTrainTabResponse> addInspectionPowerTrain(@Body InspectionPowerTrainRequest request);

    @POST(Endpoints.API_ADD_CAR_IMAGES)
    Call<PowerTrainAddImageResponse> powerTrainAddImagesApi(@Body PowerTrainAddImageRequest request);

    /*Inspection Mechanical*/
    @POST(Endpoints.API_CAR_INSPECTION_MECHANICAL)
    Call<InspectionMechanicalResponse> inspectionMechanical(@Body InspectionMechanicalRequest request);

    @POST(Endpoints.API_ADD_CAR_IMAGES)
    Call<InspectionMechanicalVideoResponse> inspectionMechanicalVideo(@Body InspectionMechanicalVideoRequest request);

    /* TiresWheel*/
    @POST(Endpoints.API_CAR_INSPECTION_TIRES_WHEELS)
    Call<InspectionTiresWheelResponse> inspectionTiresWheel(@Body InspectionTiresWheelsRequest request);

    @POST(Endpoints.API_ADD_CAR_IMAGES)
    Call<InspectionTiresWheelImageResponse> inspectionTiresWheelImageApi(@Body InspectionTiresWheelImageRequest request);

    /*Exterior*/
    @POST(Endpoints.API_CAR_INSPECTION_EXTERIOR)
    Call<ExteriorTabResponse> exteriorTabApi(@Body ExteriorTabRequest request);

    @POST(Endpoints.API_ADD_CAR_IMAGES)
    Call<ExteriorAddImageResponse> exteriorAddImageApi(@Body ExteriorAddImageRequest request);

    /* Interior*/
    @POST(Endpoints.API_CAR_INSPECTION_INTERIOR)
    Call<InteriorTabResponse> interiorTabApi(@Body InteriorTabRequest request);

    @POST(Endpoints.API_ADD_CAR_IMAGES)
    Call<InteriorAddImageResponse> interiorAddImageApi(@Body InteriorAddImageRequest request);

    /* Test drive*/
    @POST(Endpoints.API_CAR_INSPECTION_TEST_DRIVE)
    Call<TestDriveResponse> testDriveTabApi(@Body TestDriveRequest request);

    @POST(Endpoints.API_ADD_CAR_IMAGES)
    Call<TestDriveAddImageResponse> testDriveAddImageApi(@Body TestDriveAddImageRequest request);

   /* pending search */
    @POST(Endpoints.API_PENDING_DEATILS_SEARCH)
    Call<CompletedInspectionResponse> pendingSearchApi(@Body PendingSearchRequest request);

    @POST(Endpoints.API_COMPLETED_DEATILS_SEARCH)
    Call<CompletedInspectionResponse> completeSearchApi(@Body CompleteSearchRequest request);

    @POST(Endpoints.API_THIS_MONTH_COMPLETED_DEATILS_SEARCH)
    Call<CompletedInspectionResponse> monthSearchApi(@Body MonthSearchRequest request);

    @POST(Endpoints.API_REOPEN_DEATILS_SEARCH)
    Call<CompletedInspectionResponse> reOpenSearchApi(@Body ReopenSearchRequest request);

    @POST(Endpoints.PROFILE_UPDATE)
    Call<ProfileEditResponse> profileEditApi(@Body ProfileEditRequest request);
}

package com.folkus.data.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.folkus.data.Result;
import com.folkus.data.local.prefs.UserPreferences;
import com.folkus.data.model.User;
import com.folkus.data.remote.NetworkService;
import com.folkus.data.remote.request.AcceptRequest;
import com.folkus.data.remote.request.AddExteriorImagesRequest;
import com.folkus.data.remote.request.AddInspectorRequest;
import com.folkus.data.remote.request.AddVehicleRequest;
import com.folkus.data.remote.request.CarModelRequest;
import com.folkus.data.remote.request.CityRequest;
import com.folkus.data.remote.request.CompleteSearchRequest;
import com.folkus.data.remote.request.CountRequest;
import com.folkus.data.remote.request.ExteriorAddImageRequest;
import com.folkus.data.remote.request.ExteriorTabRequest;
import com.folkus.data.remote.request.GeneralInfoRequest;
import com.folkus.data.remote.request.InspectionCancelRequest;
import com.folkus.data.remote.request.InspectionMechanicalRequest;
import com.folkus.data.remote.request.InspectionMechanicalVideoRequest;
import com.folkus.data.remote.request.InspectionPowerTrainRequest;
import com.folkus.data.remote.request.InspectionTiresWheelImageRequest;
import com.folkus.data.remote.request.InspectionTiresWheelsRequest;
import com.folkus.data.remote.request.InteriorAddImageRequest;
import com.folkus.data.remote.request.InteriorTabRequest;
import com.folkus.data.remote.request.MonthSearchRequest;
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
import com.folkus.data.remote.response.CityResponse;
import com.folkus.data.remote.response.CompletedInspectionResponse;
import com.folkus.data.remote.response.ExteriorAddImageResponse;
import com.folkus.data.remote.response.ExteriorTabResponse;
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
import com.folkus.data.remote.response.LoginError;
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
import com.folkus.ui.login.NetworkChangeReceiver;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InspectionRepository {
    private static volatile InspectionRepository instance;
    private NetworkService networkService;
    private UserPreferences userPreferences;
    private UserRepository userRepository;

    private MutableLiveData<Result<InspectionRequestResponse>> inspectionRequestResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CompletedInspectionResponse>> pendingInspectionResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CompletedInspectionResponse>> completedInspectionResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CompletedInspectionResponse>> reopenedInspectionResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CompletedInspectionResponse>> totalCompletedInspectionResponse = new MutableLiveData<>();
    private MutableLiveData<Result<SellerDealerShipResponse>> sellerDealershipNameResponse = new MutableLiveData<>();
    private MutableLiveData<Result<StateResponse>> stateResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CityResponse>> cityResponse = new MutableLiveData<>();
    private MutableLiveData<Result<ZipCodeResponse>> zipCodeResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InspectorPositionResponse>> inspectorPositionResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InspectorResponse>> inspectorResponse = new MutableLiveData<>();
    private MutableLiveData<Result<SellerDetailsResponse>> sellerDetailsResponse = new MutableLiveData<>();
    private MutableLiveData<Result<AcceptResponse>> acceptResponse = new MutableLiveData<>();
    private MutableLiveData<Result<RejectResponse>> rejectResponse = new MutableLiveData<>();
    private MutableLiveData<Result<RejectReasonDropDownResponse>> rejectReasonDropDownResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CarMakersResponseDropDown>> carMakersDropDown = new MutableLiveData<>();
    private MutableLiveData<Result<CarModelResponse>> carModelResponse = new MutableLiveData<>();
    private MutableLiveData<Result<AddVehicleResponse>> addVehicleResponse = new MutableLiveData<>();
    private MutableLiveData<Result<GeneralInfoResponse>> generalInfoResponse = new MutableLiveData<>();
    private MutableLiveData<Result<AddExteriorImagesResponse>> addExteriorImagesResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CancelReasonDropDownResponse>> cancelReasonDropDownResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CancelInspectionResponse>> cancelInspectionResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InspectionPowerTrainTabResponse>> powerTrainTabResponse = new MutableLiveData<>();
    private MutableLiveData<Result<PowerTrainAddImageResponse>> powerTrainAddMultipleImageResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InspectionMechanicalResponse>> inspectionMechanicalResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InspectionMechanicalVideoResponse>> inspectionMechanicalVideoResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InspectionTiresWheelResponse>> inspectionTiresWheelResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InspectionTiresWheelImageResponse>> inspectionTiresWheelImageResponse = new MutableLiveData<>();
    private MutableLiveData<Result<ExteriorTabResponse>> exteriorTabResponse = new MutableLiveData<>();
    private MutableLiveData<Result<ExteriorAddImageResponse>> exteriorAddImageResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InteriorTabResponse>> interiorTabResponse = new MutableLiveData<>();
    private MutableLiveData<Result<InteriorAddImageResponse>> interiorAddImageResponse = new MutableLiveData<>();
    private MutableLiveData<Result<TestDriveResponse>> testDriveResponse = new MutableLiveData<>();
    private MutableLiveData<Result<TestDriveAddImageResponse>> testDriveAddImageResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CompletedInspectionResponse>> pendingSearchResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CompletedInspectionResponse>> completeSearchResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CompletedInspectionResponse>> monthSearchResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CompletedInspectionResponse>> reOpenSearchResponse = new MutableLiveData<>();
    private MutableLiveData<Result<ProfileEditResponse>> profileEditResponse = new MutableLiveData<>();

    private InspectionRepository(NetworkService networkService, UserPreferences userPreferences, UserRepository userRepository) {
        this.networkService = networkService;
        this.userPreferences = userPreferences;
        this.userRepository = userRepository;
    }

    public static InspectionRepository getInstance(NetworkService networkService, UserPreferences userPreferences, UserRepository userRepository) {
        if (instance == null) {
            instance = new InspectionRepository(networkService, userPreferences, userRepository);
        }
        return instance;
    }

    public MutableLiveData<Result<InspectionRequestResponse>> getInspectionRequestResponse() {
        return inspectionRequestResponse;
    }

    public MutableLiveData<Result<CompletedInspectionResponse>> getPendingInspectionResponse() {
        return pendingInspectionResponse;
    }

    public MutableLiveData<Result<CompletedInspectionResponse>> getCompletedInspectionResponse() {
        return completedInspectionResponse;
    }

    public MutableLiveData<Result<CompletedInspectionResponse>> getReopenedInspectionResponse() {
        return reopenedInspectionResponse;
    }

    public MutableLiveData<Result<CompletedInspectionResponse>> getTotalCompletedInspectionResponse() {
        return totalCompletedInspectionResponse;
    }

    public MutableLiveData<Result<SellerDealerShipResponse>> getSellerDealershipNameResponse() {
        return sellerDealershipNameResponse;
    }

    public MutableLiveData<Result<StateResponse>> getStateResponse() {
        return stateResponse;
    }

    public MutableLiveData<Result<CityResponse>> getCityResponse() {
        return cityResponse;
    }

    public MutableLiveData<Result<ZipCodeResponse>> getZipCodeResponse() {
        return zipCodeResponse;
    }

    public MutableLiveData<Result<InspectorPositionResponse>> getInspectorPositionResponse() {
        return inspectorPositionResponse;
    }

    public MutableLiveData<Result<InspectorResponse>> getInspectorResponse() {
        return inspectorResponse;
    }

    public MutableLiveData<Result<SellerDetailsResponse>> getSellerDetailsResponse() {
        return sellerDetailsResponse;
    }

    public MutableLiveData<Result<AcceptResponse>> getAcceptResponse() {
        return acceptResponse;
    }

    public MutableLiveData<Result<RejectResponse>> getRejectResponse() {
        return rejectResponse;
    }

    public MutableLiveData<Result<RejectReasonDropDownResponse>> getRejectReasonDropDownResponse() {
        return rejectReasonDropDownResponse;
    }

    public MutableLiveData<Result<CarMakersResponseDropDown>> getCarMakersDropDown() {
        return carMakersDropDown;
    }

    public MutableLiveData<Result<CarModelResponse>> getCarModelResponse() {
        return carModelResponse;
    }

    public MutableLiveData<Result<AddVehicleResponse>> getAddVehicleResponse() {
        return addVehicleResponse;
    }

    public MutableLiveData<Result<GeneralInfoResponse>> getGeneralInfoResponse() {
        return generalInfoResponse;
    }

    public MutableLiveData<Result<AddExteriorImagesResponse>> getAddExteriorImagesResponse() {
        return addExteriorImagesResponse;
    }

    public MutableLiveData<Result<CancelReasonDropDownResponse>> getCancelReasonDropDownResponse() {
        return cancelReasonDropDownResponse;
    }

    public MutableLiveData<Result<CancelInspectionResponse>> getCancelInspectionResponse() {
        return cancelInspectionResponse;
    }

    public MutableLiveData<Result<InspectionPowerTrainTabResponse>> getPowerTrainTabResponse() {
        return powerTrainTabResponse;
    }

    public MutableLiveData<Result<PowerTrainAddImageResponse>> getPowerTrainAddMultipleImageResponse() {
        return powerTrainAddMultipleImageResponse;
    }

    public MutableLiveData<Result<InspectionMechanicalResponse>> getInspectionMechanicalResponse() {
        return inspectionMechanicalResponse;
    }

    public MutableLiveData<Result<InspectionMechanicalVideoResponse>> getInspectionMechanicalVideoResponse() {
        return inspectionMechanicalVideoResponse;
    }

    public MutableLiveData<Result<InspectionTiresWheelResponse>> getInspectionTiresWheelResponse() {
        return inspectionTiresWheelResponse;
    }

    public MutableLiveData<Result<InspectionTiresWheelImageResponse>> getInspectionTiresWheelImageResponse() {
        return inspectionTiresWheelImageResponse;
    }

    public MutableLiveData<Result<ExteriorAddImageResponse>> getExteriorAddImageResponse() {
        return exteriorAddImageResponse;
    }

    public MutableLiveData<Result<ExteriorTabResponse>> getExteriorTabResponse() {
        return exteriorTabResponse;
    }

    public MutableLiveData<Result<InteriorAddImageResponse>> getInteriorAddImageResponse() {
        return interiorAddImageResponse;
    }

    public MutableLiveData<Result<InteriorTabResponse>> getInteriorTabResponse() {
        return interiorTabResponse;
    }

    public MutableLiveData<Result<TestDriveResponse>> getTestDriveResponse() {
        return testDriveResponse;
    }

    public MutableLiveData<Result<TestDriveAddImageResponse>> getTestDriveAddImageResponse() {
        return testDriveAddImageResponse;
    }

    public MutableLiveData<Result<CompletedInspectionResponse>> getPendingSearchResponse() {
        return pendingSearchResponse;
    }

    public MutableLiveData<Result<CompletedInspectionResponse>> getCompleteSearchResponse() {
        return completeSearchResponse;
    }

    public MutableLiveData<Result<CompletedInspectionResponse>> getMonthSearchResponse() {
        return monthSearchResponse;
    }

    public MutableLiveData<Result<CompletedInspectionResponse>> getReOpenSearchResponse() {
        return reOpenSearchResponse;
    }

    public MutableLiveData<Result<ProfileEditResponse>> getProfileEditResponse() {
        return profileEditResponse;
    }


    public void inspectionRequest() {
        User currentUser = userRepository.getCurrentUser();
        int userId = currentUser.getUserId();

        CountRequest countRequest = new CountRequest();
        countRequest.setInspectorId(1);
        networkService.getInspectionRequestData(countRequest).enqueue(new Callback<InspectionRequestResponse>() {
            @Override
            public void onResponse(Call<InspectionRequestResponse> call, Response<InspectionRequestResponse> response) {
                InspectionRequestResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    inspectionRequestResponse.postValue(result);
                } else {
                    LoginError error = body.getError();
                    Result result = new Result.Error(error);
                    inspectionRequestResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InspectionRequestResponse> call, Throwable t) {
                InspectionRequestResponse body = new InspectionRequestResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(new IOException("Error"));
                inspectionRequestResponse.postValue(result);
            }
        });
    }

    public void pendingInspection() {
        User currentUser = userRepository.getCurrentUser();
        int userId = currentUser.getUserId();

        CountRequest countRequest = new CountRequest();
        countRequest.setInspectorId(1);

        networkService.getPendingInspectionData(countRequest).enqueue(new Callback<CompletedInspectionResponse>() {
            @Override
            public void onResponse(Call<CompletedInspectionResponse> call, Response<CompletedInspectionResponse> response) {
                CompletedInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    Log.e("TAGs", "onResponses: " + response.body().getData().size());
                    pendingInspectionResponse.postValue(result);
                } else {
                    LoginError error = new LoginError();
                    error.setErr("Failed");
                    Result result = new Result.Error(error);
                    pendingInspectionResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CompletedInspectionResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Error"));
                pendingInspectionResponse.postValue(result);
            }
        });
    }

    public void completedInspection() {
        User currentUser = userRepository.getCurrentUser();
        int userId = currentUser.getUserId();
        CountRequest countRequest = new CountRequest();
        countRequest.setInspectorId(1);
        networkService.getCompletedInspectionData(countRequest).enqueue(new Callback<CompletedInspectionResponse>() {
            @Override
            public void onResponse(Call<CompletedInspectionResponse> call, Response<CompletedInspectionResponse> response) {
                CompletedInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Log.e("TAG", "onResponse: " + body.getData().size());
                    Result result = new Result.Success<>(body);
                    completedInspectionResponse.postValue(result);
                } else {
                    LoginError error = new LoginError();
                    error.setErr("Failed");
                    Result result = new Result.Error(error);
                    completedInspectionResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CompletedInspectionResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Error"));
                completedInspectionResponse.postValue(result);
            }
        });
    }

    public void getThisMonthCompletedInspectionData() {
        User currentUser = userRepository.getCurrentUser();
        int userId = currentUser.getUserId();

        CountRequest countRequest = new CountRequest();
        countRequest.setInspectorId(1);
        networkService.getThisMonthCompletedInspectionData(countRequest).enqueue(new Callback<CompletedInspectionResponse>() {
            @Override
            public void onResponse(Call<CompletedInspectionResponse> call, Response<CompletedInspectionResponse> response) {
                CompletedInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    totalCompletedInspectionResponse.postValue(result);
                } else {
                    LoginError error = new LoginError();
                    error.setErr("Failed");
                    Result result = new Result.Error(error);
                    totalCompletedInspectionResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CompletedInspectionResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Error"));
                totalCompletedInspectionResponse.postValue(result);
            }
        });
    }

    public void getThisMonthReopenedInspectionData() {
        User currentUser = userRepository.getCurrentUser();
        int userId = currentUser.getUserId();

        CountRequest countRequest = new CountRequest();
        countRequest.setInspectorId(1);
        networkService.getThisMonthReopenedInspectionData(countRequest).enqueue(new Callback<CompletedInspectionResponse>() {
            @Override
            public void onResponse(Call<CompletedInspectionResponse> call, Response<CompletedInspectionResponse> response) {
                CompletedInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    reopenedInspectionResponse.postValue(result);
                } else {
                    LoginError error = new LoginError();
                    error.setErr("Failed");
                    Result result = new Result.Error(error);
                    reopenedInspectionResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CompletedInspectionResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Error"));
                reopenedInspectionResponse.postValue(result);
            }
        });
    }

    public void getSellerDealershipNameList(String name) {
        if (name.isEmpty()) {
            networkService.getSellerDealershipNameList().enqueue(new Callback<SellerDealerShipResponse>() {
                @Override
                public void onResponse(Call<SellerDealerShipResponse> call, Response<SellerDealerShipResponse> response) {
                    SellerDealerShipResponse body = response.body();
                    boolean success = body.isSuccess();
                    if (success) {
                        Result result = new Result.Success<>(body);
                        sellerDealershipNameResponse.postValue(result);
                    } else {
                        Log.e("TAG", "Failed: ");
                        LoginError error = new LoginError();
                        error.setErr("Failed");
                        Result result = new Result.Error(error);
                        sellerDealershipNameResponse.postValue(result);
                    }
                }

                @Override
                public void onFailure(Call<SellerDealerShipResponse> call, Throwable t) {
                    Log.e("TAG", "onFailure: ");
                    Result result = new Result.Error(new IOException("Error"));
                    sellerDealershipNameResponse.postValue(result);
                }
            });
        } else {
            SellerDealerShipRequest sellerDealerShipRequest = new SellerDealerShipRequest();
            sellerDealerShipRequest.setName(name);

            networkService.getSellerDealershipNameList(sellerDealerShipRequest).enqueue(new Callback<SellerDealerShipResponse>() {
                @Override
                public void onResponse(Call<SellerDealerShipResponse> call, Response<SellerDealerShipResponse> response) {
                    SellerDealerShipResponse body = response.body();
                    boolean success = body.isSuccess();
                    if (success) {
                        Result result = new Result.Success<>(body);
                        sellerDealershipNameResponse.postValue(result);
                    } else {
                        Log.e("TAG", "Failed: ");
                        LoginError error = new LoginError();
                        error.setErr("Failed");
                        Result result = new Result.Error(error);
                        sellerDealershipNameResponse.postValue(result);
                    }
                }

                @Override
                public void onFailure(Call<SellerDealerShipResponse> call, Throwable t) {
                    Log.e("TAG", "onFailure: ");
                    Result result = new Result.Error(new IOException("Error"));
                    sellerDealershipNameResponse.postValue(result);
                }
            });
        }
    }

    public void getStates() {
        StateRequest stateRequest = new StateRequest();
        stateRequest.setCountry_id("1");
        networkService.getStateList(stateRequest).enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(Call<StateResponse> call, Response<StateResponse> response) {
                StateResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    stateResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    LoginError error = new LoginError();
                    error.setErr("Failed");
                    Result result = new Result.Error(error);
                    stateResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<StateResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                Result result = new Result.Error(new IOException("Error"));
                stateResponse.postValue(result);
            }
        });
    }

    public void getCities(String stateId) {
        CityRequest cityRequest = new CityRequest();
        cityRequest.setState_id(stateId);
        networkService.getCityList(cityRequest).enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(Call<CityResponse> call, Response<CityResponse> response) {
                CityResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    cityResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    LoginError error = new LoginError();
                    error.setErr("Failed");
                    Result result = new Result.Error(error);
                    cityResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CityResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                Result result = new Result.Error(new IOException("Error"));
                cityResponse.postValue(result);
            }
        });
    }

    public void getZipCodeList(String cityId) {
        ZipRequest zipRequest = new ZipRequest();
        zipRequest.setCity_id(cityId);
        networkService.getZipCodeList(zipRequest).enqueue(new Callback<ZipCodeResponse>() {
            @Override
            public void onResponse(Call<ZipCodeResponse> call, Response<ZipCodeResponse> response) {
                ZipCodeResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    zipCodeResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    LoginError error = new LoginError();
                    error.setErr("Failed");
                    Result result = new Result.Error(error);
                    zipCodeResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<ZipCodeResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                Result result = new Result.Error(new IOException("Error"));
                zipCodeResponse.postValue(result);
            }
        });
    }

    public void getInspectorPositionList() {
        networkService.getInspectorPosition().enqueue(new Callback<InspectorPositionResponse>() {
            @Override
            public void onResponse(Call<InspectorPositionResponse> call, Response<InspectorPositionResponse> response) {
                InspectorPositionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    inspectorPositionResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    LoginError error = new LoginError();
                    error.setErr("Failed");
                    Result result = new Result.Error(error);
                    inspectorPositionResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InspectorPositionResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                Result result = new Result.Error(new IOException("Error"));
                inspectorPositionResponse.postValue(result);
            }
        });
    }

    public void addInspector(AddInspectorRequest addInspectorRequest) {
        networkService.addInspector(addInspectorRequest).enqueue(new Callback<InspectorResponse>() {
            @Override
            public void onResponse(Call<InspectorResponse> call, Response<InspectorResponse> response) {
                InspectorResponse body = response.body();
                boolean success = body.getSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    inspectorResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    inspectorResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InspectorResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                InspectorResponse body = new InspectorResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                inspectorResponse.postValue(result);
            }
        });
    }

    public void getInspectionRequestSellerDetail(SellerDetailsRequest sellerDetailsRequest) {
        networkService.getInspectionRequestSellerDetail(sellerDetailsRequest).enqueue(new Callback<SellerDetailsResponse>() {
            @Override
            public void onResponse(Call<SellerDetailsResponse> call, Response<SellerDetailsResponse> response) {
                SellerDetailsResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    sellerDetailsResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    sellerDetailsResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<SellerDetailsResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                SellerDetailsResponse body = new SellerDetailsResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                sellerDetailsResponse.postValue(result);
            }
        });
    }

    public void acceptRequest(AcceptRequest acceptRequest) {
        networkService.acceptRequest(acceptRequest).enqueue(new Callback<AcceptResponse>() {
            @Override
            public void onResponse(Call<AcceptResponse> call, Response<AcceptResponse> response) {
                AcceptResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    acceptResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    acceptResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<AcceptResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                AcceptResponse body = new AcceptResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                acceptResponse.postValue(result);
            }
        });
    }

    public void rejectRequest(RejectRequest rejectRequest) {
        networkService.rejectRequest(rejectRequest).enqueue(new Callback<RejectResponse>() {
            @Override
            public void onResponse(Call<RejectResponse> call, Response<RejectResponse> response) {
                RejectResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    rejectResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    rejectResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<RejectResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                RejectResponse body = new RejectResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                rejectResponse.postValue(result);
            }
        });
    }

    public void getRejectReasonDropDown() {
        networkService.getRejectReasonDropDown().enqueue(new Callback<RejectReasonDropDownResponse>() {
            @Override
            public void onResponse(Call<RejectReasonDropDownResponse> call, Response<RejectReasonDropDownResponse> response) {
                RejectReasonDropDownResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    rejectReasonDropDownResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    rejectReasonDropDownResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<RejectReasonDropDownResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                RejectReasonDropDownResponse body = new RejectReasonDropDownResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                rejectReasonDropDownResponse.postValue(result);
            }
        });
    }

    public void getCarMakersList() {
        networkService.getCarMakersList().enqueue(new Callback<CarMakersResponseDropDown>() {
            @Override
            public void onResponse(Call<CarMakersResponseDropDown> call, Response<CarMakersResponseDropDown> response) {
                CarMakersResponseDropDown body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    carMakersDropDown.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    carMakersDropDown.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CarMakersResponseDropDown> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                CarMakersResponseDropDown body = new CarMakersResponseDropDown();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                carMakersDropDown.postValue(result);
            }
        });
    }

    public void getCarModelList(CarModelRequest request) {
        networkService.getCarModelList(request).enqueue(new Callback<CarModelResponse>() {
            @Override
            public void onResponse(Call<CarModelResponse> call, Response<CarModelResponse> response) {
                CarModelResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    carModelResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    carModelResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CarModelResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                CarModelResponse body = new CarModelResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                carModelResponse.postValue(result);
            }
        });
    }

    public void addVehicle(AddVehicleRequest request) {
        networkService.addVehicle(request).enqueue(new Callback<AddVehicleResponse>() {
            @Override
            public void onResponse(Call<AddVehicleResponse> call, Response<AddVehicleResponse> response) {
                AddVehicleResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    addVehicleResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    addVehicleResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<AddVehicleResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                AddVehicleResponse body = new AddVehicleResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                addVehicleResponse.postValue(result);
            }
        });
    }

    public void submitGeneralInfoApi(GeneralInfoRequest request) {
        networkService.submitGeneralInfoApi(request).enqueue(new Callback<GeneralInfoResponse>() {
            @Override
            public void onResponse(Call<GeneralInfoResponse> call, Response<GeneralInfoResponse> response) {
                GeneralInfoResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    if (body.getData() != null) {
                        Result result = new Result.Success<>(body);
                        generalInfoResponse.postValue(result);
                    }
                } else {
                    Log.e("TAG:submitGeneralInfoApi1", "Failed: ");
                    Result result = new Result.Error(body);
                    generalInfoResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<GeneralInfoResponse> call, Throwable t) {
                Log.e("TAG: submitGeneralInfoApi2", "onFailure: ");

                GeneralInfoResponse body = new GeneralInfoResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                generalInfoResponse.postValue(result);
            }
        });
    }

    public void addExteriorImagesApi(AddExteriorImagesRequest request) {
        networkService.addExteriorImagesApi(request).enqueue(new Callback<AddExteriorImagesResponse>() {
            @Override
            public void onResponse(Call<AddExteriorImagesResponse> call, Response<AddExteriorImagesResponse> response) {
                AddExteriorImagesResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    addExteriorImagesResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    addExteriorImagesResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<AddExteriorImagesResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                AddExteriorImagesResponse body = new AddExteriorImagesResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());


                Result result = new Result.Error(body);
                addExteriorImagesResponse.postValue(result);
            }
        });
    }

    public void getCancelReasonDropDown() {
        networkService.getCancelReasonDropDown().enqueue(new Callback<CancelReasonDropDownResponse>() {
            @Override
            public void onResponse(Call<CancelReasonDropDownResponse> call, Response<CancelReasonDropDownResponse> response) {
                CancelReasonDropDownResponse body = response.body();
                if (response.body().isSuccess()) {
                    Result result = new Result.Success<>(body);
                    cancelReasonDropDownResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body.getError());
                    cancelReasonDropDownResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CancelReasonDropDownResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                CancelReasonDropDownResponse body = new CancelReasonDropDownResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                cancelReasonDropDownResponse.postValue(result);
            }
        });
    }

    public void inspectionCancelReason(InspectionCancelRequest cancelRequest) {
        networkService.cancelInspection(cancelRequest).enqueue(new Callback<CancelInspectionResponse>() {
            @Override
            public void onResponse(Call<CancelInspectionResponse> call, Response<CancelInspectionResponse> response) {
                CancelInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    cancelInspectionResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    cancelInspectionResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CancelInspectionResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");

                CancelInspectionResponse body = new CancelInspectionResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setError(loginError);

                Result result = new Result.Error(body);
                cancelInspectionResponse.postValue(result);
            }
        });
    }

    public void addInspectionPowerTrain(InspectionPowerTrainRequest request) {
        networkService.addInspectionPowerTrain(request).enqueue(new Callback<InspectionPowerTrainTabResponse>() {
            @Override
            public void onResponse(Call<InspectionPowerTrainTabResponse> call, Response<InspectionPowerTrainTabResponse> response) {
                InspectionPowerTrainTabResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    powerTrainTabResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    powerTrainTabResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InspectionPowerTrainTabResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                AddExteriorImagesResponse body = new AddExteriorImagesResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());


                Result result = new Result.Error(body);
                powerTrainTabResponse.postValue(result);
            }
        });
    }

    public void powerTrainAddMultipleImages(PowerTrainAddImageRequest request) {
        networkService.powerTrainAddImagesApi(request).enqueue(new Callback<PowerTrainAddImageResponse>() {
            @Override
            public void onResponse(Call<PowerTrainAddImageResponse> call, Response<PowerTrainAddImageResponse> response) {
                PowerTrainAddImageResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    powerTrainAddMultipleImageResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    powerTrainAddMultipleImageResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<PowerTrainAddImageResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                PowerTrainAddImageResponse body = new PowerTrainAddImageResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                powerTrainAddMultipleImageResponse.postValue(result);
            }
        });
    }

    public void inspectionMechanical(InspectionMechanicalRequest request) {
        networkService.inspectionMechanical(request).enqueue(new Callback<InspectionMechanicalResponse>() {
            @Override
            public void onResponse(Call<InspectionMechanicalResponse> call, Response<InspectionMechanicalResponse> response) {
                InspectionMechanicalResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    inspectionMechanicalResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    inspectionMechanicalResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InspectionMechanicalResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                InspectionMechanicalResponse body = new InspectionMechanicalResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                inspectionMechanicalResponse.postValue(result);
            }
        });
    }


    public void inspectionMechanicalVideo(InspectionMechanicalVideoRequest request) {
        networkService.inspectionMechanicalVideo(request).enqueue(new Callback<InspectionMechanicalVideoResponse>() {
            @Override
            public void onResponse(Call<InspectionMechanicalVideoResponse> call, Response<InspectionMechanicalVideoResponse> response) {
                InspectionMechanicalVideoResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    inspectionMechanicalVideoResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    inspectionMechanicalVideoResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InspectionMechanicalVideoResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                InspectionMechanicalVideoResponse body = new InspectionMechanicalVideoResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                inspectionMechanicalVideoResponse.postValue(result);
            }
        });
    }

    public void inspectionTiresWheel(InspectionTiresWheelsRequest request) {
        networkService.inspectionTiresWheel(request).enqueue(new Callback<InspectionTiresWheelResponse>() {
            @Override
            public void onResponse(Call<InspectionTiresWheelResponse> call, Response<InspectionTiresWheelResponse> response) {
                InspectionTiresWheelResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    inspectionTiresWheelResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    inspectionTiresWheelResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InspectionTiresWheelResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                InspectionTiresWheelResponse body = new InspectionTiresWheelResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                inspectionTiresWheelResponse.postValue(result);
            }
        });
    }

    public void inspectionTiresWheelImageApi(InspectionTiresWheelImageRequest request) {
        networkService.inspectionTiresWheelImageApi(request).enqueue(new Callback<InspectionTiresWheelImageResponse>() {
            @Override
            public void onResponse(Call<InspectionTiresWheelImageResponse> call, Response<InspectionTiresWheelImageResponse> response) {
                InspectionTiresWheelImageResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    inspectionTiresWheelImageResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    inspectionTiresWheelImageResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InspectionTiresWheelImageResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                InspectionTiresWheelImageResponse body = new InspectionTiresWheelImageResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                inspectionTiresWheelImageResponse.postValue(result);
            }
        });
    }

    public void exteriorTab(ExteriorTabRequest request) {
        networkService.exteriorTabApi(request).enqueue(new Callback<ExteriorTabResponse>() {
            @Override
            public void onResponse(Call<ExteriorTabResponse> call, Response<ExteriorTabResponse> response) {
                ExteriorTabResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    exteriorTabResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    exteriorTabResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<ExteriorTabResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                ExteriorTabResponse body = new ExteriorTabResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                exteriorTabResponse.postValue(result);
            }
        });
    }

    public void exteriorAddImageApi(ExteriorAddImageRequest request) {
        networkService.exteriorAddImageApi(request).enqueue(new Callback<ExteriorAddImageResponse>() {
            @Override
            public void onResponse(Call<ExteriorAddImageResponse> call, Response<ExteriorAddImageResponse> response) {
                ExteriorAddImageResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    exteriorAddImageResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    exteriorAddImageResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<ExteriorAddImageResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                ExteriorAddImageResponse body = new ExteriorAddImageResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                exteriorAddImageResponse.postValue(result);
            }
        });
    }


    public void interiorTabApi(InteriorTabRequest request) {
        networkService.interiorTabApi(request).enqueue(new Callback<InteriorTabResponse>() {
            @Override
            public void onResponse(Call<InteriorTabResponse> call, Response<InteriorTabResponse> response) {
                InteriorTabResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    interiorTabResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    interiorTabResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InteriorTabResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                InteriorTabResponse body = new InteriorTabResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                interiorTabResponse.postValue(result);
            }
        });
    }

    public void interiorAddImageApi(InteriorAddImageRequest request) {
        networkService.interiorAddImageApi(request).enqueue(new Callback<InteriorAddImageResponse>() {
            @Override
            public void onResponse(Call<InteriorAddImageResponse> call, Response<InteriorAddImageResponse> response) {
                InteriorAddImageResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    interiorAddImageResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    interiorAddImageResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<InteriorAddImageResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                InteriorAddImageResponse body = new InteriorAddImageResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                interiorAddImageResponse.postValue(result);
            }
        });
    }

    public void testDriveTabApi(TestDriveRequest request) {
        networkService.testDriveTabApi(request).enqueue(new Callback<TestDriveResponse>() {
            @Override
            public void onResponse(Call<TestDriveResponse> call, Response<TestDriveResponse> response) {
                TestDriveResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    testDriveResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    testDriveResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<TestDriveResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                TestDriveResponse body = new TestDriveResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                testDriveResponse.postValue(result);
            }
        });
    }

    public void testDriveAddImageApi(TestDriveAddImageRequest request) {
        networkService.testDriveAddImageApi(request).enqueue(new Callback<TestDriveAddImageResponse>() {
            @Override
            public void onResponse(Call<TestDriveAddImageResponse> call, Response<TestDriveAddImageResponse> response) {
                TestDriveAddImageResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    testDriveAddImageResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    testDriveAddImageResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<TestDriveAddImageResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                TestDriveAddImageResponse body = new TestDriveAddImageResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");
                body.setMessage(loginError.toString());

                Result result = new Result.Error(body);
                testDriveAddImageResponse.postValue(result);
            }
        });
    }

    public void pendingSearchApi(PendingSearchRequest request) {
        networkService.pendingSearchApi(request).enqueue(new Callback<CompletedInspectionResponse>() {
            @Override
            public void onResponse(Call<CompletedInspectionResponse> call, Response<CompletedInspectionResponse> response) {
                CompletedInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    pendingSearchResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    pendingSearchResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CompletedInspectionResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                CompletedInspectionResponse body = new CompletedInspectionResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");

                Result result = new Result.Error(body);
                pendingSearchResponse.postValue(result);
            }
        });
    }

    public void completeSearchApi(CompleteSearchRequest request) {
        networkService.completeSearchApi(request).enqueue(new Callback<CompletedInspectionResponse>() {
            @Override
            public void onResponse(Call<CompletedInspectionResponse> call, Response<CompletedInspectionResponse> response) {
                CompletedInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    completeSearchResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    completeSearchResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CompletedInspectionResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                CompletedInspectionResponse body = new CompletedInspectionResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");

                Result result = new Result.Error(body);
                completeSearchResponse.postValue(result);
            }
        });
    }

    public void monthSearchApi(MonthSearchRequest request) {
        networkService.monthSearchApi(request).enqueue(new Callback<CompletedInspectionResponse>() {
            @Override
            public void onResponse(Call<CompletedInspectionResponse> call, Response<CompletedInspectionResponse> response) {
                CompletedInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    monthSearchResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    monthSearchResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CompletedInspectionResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                CompletedInspectionResponse body = new CompletedInspectionResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");

                Result result = new Result.Error(body);
                monthSearchResponse.postValue(result);
            }
        });
    }

    public void reOpenSearchApi(ReopenSearchRequest request) {
        networkService.reOpenSearchApi(request).enqueue(new Callback<CompletedInspectionResponse>() {
            @Override
            public void onResponse(Call<CompletedInspectionResponse> call, Response<CompletedInspectionResponse> response) {
                CompletedInspectionResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    reOpenSearchResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    reOpenSearchResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<CompletedInspectionResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                CompletedInspectionResponse body = new CompletedInspectionResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");

                Result result = new Result.Error(body);
                reOpenSearchResponse.postValue(result);
            }
        });
    }

    public void profileEditApi(ProfileEditRequest request) {
        networkService.profileEditApi(request).enqueue(new Callback<ProfileEditResponse>() {
            @Override
            public void onResponse(Call<ProfileEditResponse> call, Response<ProfileEditResponse> response) {
                ProfileEditResponse body = response.body();
                boolean success = body.isSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    profileEditResponse.postValue(result);
                } else {
                    Log.e("TAG", "Failed: ");
                    Result result = new Result.Error(body);
                    profileEditResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<ProfileEditResponse> call, Throwable t) {
                Log.e("TAG", "onFailure: ");
                ProfileEditResponse body = new ProfileEditResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error");

                Result result = new Result.Error(body);
                profileEditResponse.postValue(result);
            }
        });
    }

}

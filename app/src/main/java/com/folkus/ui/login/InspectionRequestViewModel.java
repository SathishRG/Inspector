package com.folkus.ui.login;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.folkus.comman.Event;
import com.folkus.data.Result;
import com.folkus.data.remote.request.AcceptRequest;
import com.folkus.data.remote.request.AddExteriorImagesRequest;
import com.folkus.data.remote.request.AddInspectorRequest;
import com.folkus.data.remote.request.AddVehicleRequest;
import com.folkus.data.remote.request.CarModelRequest;
import com.folkus.data.remote.request.CompleteSearchRequest;
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
import com.folkus.data.remote.request.SellerDetailsRequest;
import com.folkus.data.remote.request.TestDriveAddImageRequest;
import com.folkus.data.remote.request.TestDriveRequest;
import com.folkus.data.remote.response.AcceptResponse;
import com.folkus.data.remote.response.AddExteriorImagesResponse;
import com.folkus.data.remote.response.AddVehicleResponse;
import com.folkus.data.remote.response.CancelInspectionResponse;
import com.folkus.data.remote.response.CancelReasonDropDownResponse;
import com.folkus.data.remote.response.CarMakersResponseDropDown;
import com.folkus.data.remote.response.CarModelResponse;
import com.folkus.data.remote.response.CityResponse;
import com.folkus.data.remote.response.CompletedInspection;
import com.folkus.data.remote.response.CompletedInspectionResponse;
import com.folkus.data.remote.response.ExteriorAddImageResponse;
import com.folkus.data.remote.response.ExteriorTabResponse;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.GeneralInfoResponse;
import com.folkus.data.remote.response.InspectionMechanicalResponse;
import com.folkus.data.remote.response.InspectionMechanicalVideoResponse;
import com.folkus.data.remote.response.InspectionPowerTrainTabResponse;
import com.folkus.data.remote.response.InspectionRequestData;
import com.folkus.data.remote.response.InspectionRequestResponse;
import com.folkus.data.remote.response.InspectionTiresWheelImageResponse;
import com.folkus.data.remote.response.InspectionTiresWheelResponse;
import com.folkus.data.remote.response.InspectorPositionResponse;
import com.folkus.data.remote.response.InspectorResponse;
import com.folkus.data.remote.response.InteriorAddImageResponse;
import com.folkus.data.remote.response.InteriorTabResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.PendingInspectionResponse;
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
import com.folkus.data.repository.InspectionRepository;
import com.folkus.ui.login.model.FinalResult;

import java.util.ArrayList;

public class InspectionRequestViewModel extends ViewModel {

    private InspectionRequestData inspectionRequestData;
    private InspectionRepository inspectionRepository;
    private MutableLiveData<FinalResult> inspectionRequestResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> pendingInspectionResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> completedInspectionResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> totalCompletedInspectionResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> reopenedInspectionResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> sellerDealershipNameResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> stateResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> cityResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> zipCodeResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> inspectorPositionResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> inspectorResponseResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> sellerDetailsResponseResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> acceptResponseResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> rejectResponseResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> rejectReasonDropDownResult = new MutableLiveData<>();
    private MutableLiveData<FinalResult> carMakersDropDown = new MutableLiveData<>();
    private MutableLiveData<FinalResult> carModelResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> addVehicleResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> generalInfoResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> addExteriorImagesResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> cancelReasonDropDownResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> cancelInspectionResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> inspectionPowerTrainTabResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> powerTrainAddMultipleImageResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> inspectionMechanicalResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> inspectionMechanicalVideoResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> inspectionTiresWheelResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> inspectionTiresWheelImageResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> exteriorTabResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> exteriorAddImageResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> interiorTabResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> interiorAddImageResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> testDriveResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> testDriveAddImageResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> pendingSearchResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> completeSearchResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> monthSearchResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> reOpenSearchResponse = new MutableLiveData<>();
    private MutableLiveData<FinalResult> profileEditResponse = new MutableLiveData<>();

    /* for this save temp data*/
    public static ArrayList<Uri> powerTrain_picturePathsList = new ArrayList<Uri>();
    public static ArrayList<Uri> powerTrain_uriArrayList = new ArrayList<>();

    public static ArrayList<Uri> mechanical_picturePathsList = new ArrayList<Uri>();
    public static ArrayList<Uri> mechanical_uriArrayList = new ArrayList<>();

    public static ArrayList<Uri> tiresWheel_picturePathsList = new ArrayList<Uri>();
    public static ArrayList<Uri> tiresWheel_uriArrayList = new ArrayList<>();

    public static ArrayList<Uri> exterior_picturePathsList = new ArrayList<Uri>();
    public static ArrayList<Uri> exterior_uriArrayList = new ArrayList<>();

    public static ArrayList<Uri> interior_picturePathsList = new ArrayList<Uri>();
    public static ArrayList<Uri> interior_uriArrayList = new ArrayList<>();

    public static ArrayList<Uri> testDrive_picturePathsList = new ArrayList<Uri>();
    public static ArrayList<Uri> testDrive_uriArrayList = new ArrayList<>();

    public static boolean powerTrainFirstTimeCalled = false;
    public static boolean mechanicalFirstTimeCalled = false;
    public static boolean tiresWheelFirstTimeCalled = false;
    public static boolean exteriorFirstTimeCalled = false;
    public static boolean interiorFirstTimeCalled = false;
    public static boolean testDriveFirstTimeCalled = false;


    public ArrayList<GeneralInfoData> generalInfoData = new ArrayList<>();
    public static GeneralInfoData generalInfo;
    public static CompletedInspection completedInspection;

    public static CompletedInspection getCompletedInspection() {
        return completedInspection;
    }

    public static void setCompletedInspection(CompletedInspection completedInspection) {
        InspectionRequestViewModel.completedInspection = completedInspection;
    }

    public GeneralInfoData getGeneralInfo() {
        return generalInfo;
    }

    public void setGeneralInfo(GeneralInfoData generalInfo) {
        this.generalInfo = generalInfo;
    }

    public ArrayList<GeneralInfoData> getGeneralInfoData() {
        return generalInfoData;
    }

    public void setGeneralInfoData(ArrayList<GeneralInfoData> generalInfoData) {
        this.generalInfoData.addAll(generalInfoData);
    }

    public InspectionRequestData getInspectionRequestData() {
        return inspectionRequestData;
    }

    public void setInspectionRequestData(InspectionRequestData inspectionRequestData) {
        this.inspectionRequestData = inspectionRequestData;
    }

    public InspectionRequestViewModel(InspectionRepository inspectionRepository) {
        this.inspectionRepository = inspectionRepository;
    }

    public MutableLiveData<FinalResult> getInspectionRequestResult() {
        return inspectionRequestResult;
    }

    public MutableLiveData<FinalResult> getPendingInspectionResult() {
        return pendingInspectionResult;
    }

    public MutableLiveData<FinalResult> getCompletedInspectionResult() {
        return completedInspectionResult;
    }

    public MutableLiveData<FinalResult> getTotalCompletedInspectionResult() {
        return totalCompletedInspectionResult;
    }

    public MutableLiveData<FinalResult> getReopenedInspectionResult() {
        return reopenedInspectionResult;
    }

    public MutableLiveData<FinalResult> getSellerDealershipNameResult() {
        return sellerDealershipNameResult;
    }

    public MutableLiveData<FinalResult> getStateResult() {
        return stateResult;
    }

    public InspectionRepository getInspectionRepository() {
        return inspectionRepository;
    }

    public MutableLiveData<FinalResult> getCityResult() {
        return cityResult;
    }

    public MutableLiveData<FinalResult> getZipCodeResult() {
        return zipCodeResult;
    }

    public MutableLiveData<FinalResult> getInspectorPositionResult() {
        return inspectorPositionResult;
    }

    public MutableLiveData<FinalResult> getInspectorResponse() {
        return inspectorResponseResult;
    }

    public MutableLiveData<FinalResult> getSellerDetailsResponseResult() {
        return sellerDetailsResponseResult;
    }

    public MutableLiveData<FinalResult> getAcceptResponseResult() {
        return acceptResponseResult;
    }

    public MutableLiveData<FinalResult> getRejectResponseResult() {
        return rejectResponseResult;
    }

    public MutableLiveData<FinalResult> getRejectReasonDropDownResult() {
        return rejectReasonDropDownResult;
    }

    public MutableLiveData<FinalResult> getCarMakersDropDown() {
        return carMakersDropDown;
    }

    public MutableLiveData<FinalResult> getCarModelResponse() {
        return carModelResponse;
    }

    public MutableLiveData<FinalResult> getAddVehicleResponse() {
        return addVehicleResponse;
    }

    public MutableLiveData<FinalResult> getGeneralInfoResponse() {
        return generalInfoResponse;
    }

    public MutableLiveData<FinalResult> getAddExteriorImagesResponse() {
        return addExteriorImagesResponse;
    }

    public MutableLiveData<FinalResult> getCancelReasonDropDownResponse() {
        return cancelReasonDropDownResponse;
    }

    public MutableLiveData<FinalResult> getCancelInspectionResponse() {
        return cancelInspectionResponse;
    }

    public MutableLiveData<FinalResult> getInspectionPowerTrainTabResponse() {
        return inspectionPowerTrainTabResponse;
    }

    public MutableLiveData<FinalResult> getPowerTrainAddMultipleImageResponse() {
        return powerTrainAddMultipleImageResponse;
    }

    public MutableLiveData<FinalResult> getInspectionMechanicalResponse() {
        return inspectionMechanicalResponse;
    }

    public MutableLiveData<FinalResult> getInspectionMechanicalVideoResponse() {
        return inspectionMechanicalVideoResponse;
    }

    public MutableLiveData<FinalResult> getInspectionTiresWheelResponse() {
        return inspectionTiresWheelResponse;
    }

    public MutableLiveData<FinalResult> getInspectionTiresWheelImageResponse() {
        return inspectionTiresWheelImageResponse;
    }

    public MutableLiveData<FinalResult> getExteriorTabResponse() {
        return exteriorTabResponse;
    }

    public MutableLiveData<FinalResult> getExteriorAddImageResponse() {
        return exteriorAddImageResponse;
    }

    public MutableLiveData<FinalResult> getInteriorTabResponse() {
        return interiorTabResponse;
    }

    public MutableLiveData<FinalResult> getInteriorAddImageResponse() {
        return interiorAddImageResponse;
    }

    public MutableLiveData<FinalResult> getTestDriveResponse() {
        return testDriveResponse;
    }

    public MutableLiveData<FinalResult> getTestDriveAddImageResponse() {
        return testDriveAddImageResponse;
    }

    public MutableLiveData<FinalResult> getPendingSearchResponse() {
        return pendingSearchResponse;
    }

    public MutableLiveData<FinalResult> getCompleteSearchResponse() {
        return completeSearchResponse;
    }

    public MutableLiveData<FinalResult> getMonthSearchResponse() {
        return monthSearchResponse;
    }

    public MutableLiveData<FinalResult> getReOpenSearchResponse() {
        return reOpenSearchResponse;
    }

    public MutableLiveData<FinalResult> getProfileEditResponse() {
        return profileEditResponse;
    }


    private final MutableLiveData<Event<CompletedInspection>> _isSelectInspectionDetail = new MutableLiveData();
    public LiveData<Event<CompletedInspection>> isSelectInspectionDetail = _isSelectInspectionDetail;

    public void onSelectInspectionDetail(CompletedInspection completedInspection) {
        _isSelectInspectionDetail.postValue(new Event<>(completedInspection));
    }


    public void inspectionRequest() {
        inspectionRepository.inspectionRequest();
        inspectionRepository.getInspectionRequestResponse().observeForever(inspectionRequestResponseResult -> {
            try {
                if (inspectionRequestResponseResult instanceof Result.Success) {
                    InspectionRequestResponse data = ((Result.Success<InspectionRequestResponse>) inspectionRequestResponseResult).getData();
                    inspectionRequestResult.setValue(new FinalResult((InspectionRequestResponse) data));

                } else if (inspectionRequestResponseResult instanceof Result.Error) {
                    LoginError error = ((Result.Error<LoginError>) inspectionRequestResponseResult).getError();
                    inspectionRequestResult.setValue(new FinalResult((LoginError) error));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void pendingInspection() {
        inspectionRepository.pendingInspection();
        inspectionRepository.getPendingInspectionResponse().observeForever(pendingInspectionResponseResult -> {
            if (pendingInspectionResponseResult instanceof Result.Success) {
                PendingInspectionResponse data = ((Result.Success<PendingInspectionResponse>) pendingInspectionResponseResult).getData();
                pendingInspectionResult.setValue(new FinalResult((PendingInspectionResponse) data));

            } else if (pendingInspectionResponseResult instanceof Result.Error) {
                LoginError error = ((Result.Error<LoginError>) pendingInspectionResponseResult).getError();
                pendingInspectionResult.setValue(new FinalResult((LoginError) error));
            }
        });
    }

    public void completedInspection(int pos, String pendingInspectionsFragment) {
        Log.e("TAG", "completedInspection: " + pos + pendingInspectionsFragment);
        try {
            if (pos == 0) {
                inspectionRepository.completedInspection();
                inspectionRepository.getCompletedInspectionResponse().observeForever(completedInspectionResponseResult -> {
                    if (completedInspectionResponseResult instanceof Result.Success) {
                        CompletedInspectionResponse data = ((Result.Success<CompletedInspectionResponse>) completedInspectionResponseResult).getData();
                        completedInspectionResult.setValue(new FinalResult((CompletedInspectionResponse) data));
                    } else if (completedInspectionResponseResult instanceof Result.Error) {
                        LoginError error = ((Result.Error<LoginError>) completedInspectionResponseResult).getError();
                        completedInspectionResult.setValue(new FinalResult((LoginError) error));
                    }
                });
            } else if (pos == 1) {
                inspectionRepository.pendingInspection();
                inspectionRepository.getPendingInspectionResponse().observeForever(pendingInspectionResponseResult -> {
                    try {
                        if (pendingInspectionResponseResult instanceof Result.Success) {
                            CompletedInspectionResponse data = ((Result.Success<CompletedInspectionResponse>) pendingInspectionResponseResult).getData();
                            pendingInspectionResult.setValue(new FinalResult((CompletedInspectionResponse) data));
                        } else if (pendingInspectionResponseResult instanceof Result.Error) {
                            LoginError error = ((Result.Error<LoginError>) pendingInspectionResponseResult).getError();
                            pendingInspectionResult.setValue(new FinalResult((LoginError) error));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (pos == 2) {
                inspectionRepository.getThisMonthCompletedInspectionData();
                inspectionRepository.getTotalCompletedInspectionResponse().observeForever(pendingInspectionResponseResult -> {
                    try {
                        if (pendingInspectionResponseResult instanceof Result.Success) {
                            CompletedInspectionResponse data = ((Result.Success<CompletedInspectionResponse>) pendingInspectionResponseResult).getData();
                            totalCompletedInspectionResult.setValue(new FinalResult((CompletedInspectionResponse) data));
                        } else if (pendingInspectionResponseResult instanceof Result.Error) {
                            LoginError error = ((Result.Error<LoginError>) pendingInspectionResponseResult).getError();
                            totalCompletedInspectionResult.setValue(new FinalResult((LoginError) error));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else if (pos == 3) {
                inspectionRepository.getThisMonthReopenedInspectionData();
                inspectionRepository.getReopenedInspectionResponse().observeForever(pendingInspectionResponseResult -> {
                    try {
                        if (pendingInspectionResponseResult instanceof Result.Success) {
                            CompletedInspectionResponse data = ((Result.Success<CompletedInspectionResponse>) pendingInspectionResponseResult).getData();
                            reopenedInspectionResult.setValue(new FinalResult((CompletedInspectionResponse) data));
                        } else if (pendingInspectionResponseResult instanceof Result.Error) {
                            LoginError error = ((Result.Error<LoginError>) pendingInspectionResponseResult).getError();
                            reopenedInspectionResult.setValue(new FinalResult((LoginError) error));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getSellerDealershipNameList(String name) {
        inspectionRepository.getSellerDealershipNameList(name);
        inspectionRepository.getSellerDealershipNameResponse().observeForever(sellerDealerShipResponse -> {
            try {
                if (sellerDealerShipResponse instanceof Result.Success) {
                    SellerDealerShipResponse data = ((Result.Success<SellerDealerShipResponse>) sellerDealerShipResponse).getData();
                    sellerDealershipNameResult.setValue(new FinalResult((SellerDealerShipResponse) data));

                } else if (sellerDealerShipResponse instanceof Result.Error) {
                    LoginError error = ((Result.Error<LoginError>) sellerDealerShipResponse).getError();
                    sellerDealershipNameResult.setValue(new FinalResult((LoginError) error));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getStates() {
        inspectionRepository.getStates();
        inspectionRepository.getStateResponse().observeForever(stateResponse -> {
            try {
                if (stateResponse instanceof Result.Success) {
                    StateResponse data = ((Result.Success<StateResponse>) stateResponse).getData();
                    stateResult.setValue(new FinalResult((StateResponse) data));

                } else if (stateResponse instanceof Result.Error) {
                    LoginError error = ((Result.Error<LoginError>) stateResponse).getError();
                    stateResult.setValue(new FinalResult((LoginError) error));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getCityList(String stateId) {
        inspectionRepository.getCities(stateId);
        inspectionRepository.getCityResponse().observeForever(cityResponse -> {
            try {
                if (cityResponse instanceof Result.Success) {
                    CityResponse data = ((Result.Success<CityResponse>) cityResponse).getData();
                    cityResult.setValue(new FinalResult((CityResponse) data));

                } else if (cityResponse instanceof Result.Error) {
                    LoginError error = ((Result.Error<LoginError>) cityResponse).getError();
                    cityResult.setValue(new FinalResult((LoginError) error));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getZipCodeList(String cityId) {
        inspectionRepository.getZipCodeList(cityId);
        inspectionRepository.getZipCodeResponse().observeForever(zipCodeResponse -> {
            try {
                if (zipCodeResponse instanceof Result.Success) {
                    ZipCodeResponse data = ((Result.Success<ZipCodeResponse>) zipCodeResponse).getData();
                    zipCodeResult.setValue(new FinalResult((ZipCodeResponse) data));

                } else if (zipCodeResponse instanceof Result.Error) {
                    LoginError error = ((Result.Error<LoginError>) zipCodeResponse).getError();
                    zipCodeResult.setValue(new FinalResult((LoginError) error));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getInspectorPositionList() {
        inspectionRepository.getInspectorPositionList();
        inspectionRepository.getInspectorPositionResponse().observeForever(inspectorPositionResponse -> {
            try {
                if (inspectorPositionResponse instanceof Result.Success) {
                    InspectorPositionResponse data = ((Result.Success<InspectorPositionResponse>) inspectorPositionResponse).getData();
                    inspectorPositionResult.setValue(new FinalResult((InspectorPositionResponse) data));

                } else if (inspectorPositionResponse instanceof Result.Error) {
                    LoginError error = ((Result.Error<LoginError>) inspectorPositionResponse).getError();
                    inspectorPositionResult.setValue(new FinalResult((LoginError) error));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addInspector(AddInspectorRequest addInspectorRequest) {
        inspectionRepository.addInspector(addInspectorRequest);
        inspectionRepository.getInspectorResponse().observeForever(inspectorResponse -> {
            try {
                if (inspectorResponse instanceof Result.Success) {
                    InspectorResponse data = ((Result.Success<InspectorResponse>) inspectorResponse).getData();
                    inspectorResponseResult.setValue(new FinalResult((InspectorResponse) data));

                } else if (inspectorResponse instanceof Result.Error) {
                    InspectorResponse error = ((Result.Error<InspectorResponse>) inspectorResponse).getError();
                    inspectorResponseResult.setValue(new FinalResult((LoginError) error.getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getInspectionRequestSellerDetail(int sellerInfoId) {
        SellerDetailsRequest sellerDetailsRequest = new SellerDetailsRequest();
        sellerDetailsRequest.setSeller_info_id(sellerInfoId);
        inspectionRepository.getInspectionRequestSellerDetail(sellerDetailsRequest);
        inspectionRepository.getSellerDetailsResponse().observeForever(sellerDetailsResponse -> {
            try {
                if (sellerDetailsResponse instanceof Result.Success) {
                    SellerDetailsResponse data = ((Result.Success<SellerDetailsResponse>) sellerDetailsResponse).getData();
                    sellerDetailsResponseResult.setValue(new FinalResult((SellerDetailsResponse) data));

                } else if (sellerDetailsResponse instanceof Result.Error) {
                    SellerDetailsResponse error = ((Result.Error<SellerDetailsResponse>) sellerDetailsResponse).getError();
                    sellerDetailsResponseResult.setValue(new FinalResult((LoginError) error.getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void acceptRequest(int sellerInfoId) {
        AcceptRequest acceptRequest = new AcceptRequest();
        acceptRequest.setSeller_info_id(sellerInfoId);
        acceptRequest.setStatus("ACCEPT");
        inspectionRepository.acceptRequest(acceptRequest);
        inspectionRepository.getAcceptResponse().observeForever(acceptResponse -> {
            try {
                if (acceptResponse instanceof Result.Success) {
                    AcceptResponse data = ((Result.Success<AcceptResponse>) acceptResponse).getData();
                    acceptResponseResult.setValue(new FinalResult((AcceptResponse) data));

                } else if (acceptResponse instanceof Result.Error) {
                    AcceptResponse error = ((Result.Error<AcceptResponse>) acceptResponse).getError();
                    acceptResponseResult.setValue(new FinalResult((LoginError) error.getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void rejectRequest(int sellerInfoId, int rejectId, String comments) {
        RejectRequest rejectRequest = new RejectRequest();
        rejectRequest.setSeller_info_id(sellerInfoId);
        rejectRequest.setStatus("REJECT");
        rejectRequest.setComments(comments);
        rejectRequest.setReject_id(rejectId);

        inspectionRepository.rejectRequest(rejectRequest);
        inspectionRepository.getRejectResponse().observeForever(rejectResponse -> {
            try {
                if (rejectResponse instanceof Result.Success) {
                    RejectResponse data = ((Result.Success<RejectResponse>) rejectResponse).getData();
                    rejectResponseResult.setValue(new FinalResult((RejectResponse) data));

                } else if (rejectResponse instanceof Result.Error) {
                    RejectResponse error = ((Result.Error<RejectResponse>) rejectResponse).getError();
                    rejectResponseResult.setValue(new FinalResult((LoginError) error.getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getRejectReasonDropDown() {
        inspectionRepository.getRejectReasonDropDown();
        inspectionRepository.getRejectReasonDropDownResponse().observeForever(rejectReasonDropDownResponse -> {
            try {
                if (rejectReasonDropDownResponse instanceof Result.Success) {
                    RejectReasonDropDownResponse data = ((Result.Success<RejectReasonDropDownResponse>) rejectReasonDropDownResponse).getData();
                    rejectReasonDropDownResult.setValue(new FinalResult((RejectReasonDropDownResponse) data));

                } else if (rejectReasonDropDownResponse instanceof Result.Error) {
                    RejectReasonDropDownResponse error = ((Result.Error<RejectReasonDropDownResponse>) rejectReasonDropDownResponse).getError();
                    rejectReasonDropDownResult.setValue(new FinalResult((LoginError) error.getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getCarMakersList() {
        inspectionRepository.getCarMakersList();
        inspectionRepository.getCarMakersDropDown().observeForever(carMakersResponseDropDown -> {
            try {
                if (carMakersResponseDropDown instanceof Result.Success) {
                    CarMakersResponseDropDown data = ((Result.Success<CarMakersResponseDropDown>) carMakersResponseDropDown).getData();
                    carMakersDropDown.setValue(new FinalResult((CarMakersResponseDropDown) data));

                } else if (carMakersResponseDropDown instanceof Result.Error) {
                    CarMakersResponseDropDown error = ((Result.Error<CarMakersResponseDropDown>) carMakersResponseDropDown).getError();
                    carMakersDropDown.setValue(new FinalResult((LoginError) error.getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getCarModelList(String makersID) {
        CarModelRequest request = new CarModelRequest();
        request.setMakers_id(makersID);

        inspectionRepository.getCarModelList(request);
        inspectionRepository.getCarModelResponse().observeForever(carModelResponseResult -> {
            try {
                if (carModelResponseResult instanceof Result.Success) {
                    CarModelResponse data = ((Result.Success<CarModelResponse>) carModelResponseResult).getData();
                    carModelResponse.setValue(new FinalResult((CarModelResponse) data));

                } else if (carModelResponseResult instanceof Result.Error) {
                    CarModelResponse error = ((Result.Error<CarModelResponse>) carModelResponseResult).getError();
                    carModelResponse.setValue(new FinalResult((LoginError) error.getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void addVehicle(AddVehicleRequest request) {
        inspectionRepository.addVehicle(request);
        inspectionRepository.getAddVehicleResponse().observeForever(addVehicleResponseResult -> {
            try {
                if (addVehicleResponseResult instanceof Result.Success) {
                    AddVehicleResponse data = ((Result.Success<AddVehicleResponse>) addVehicleResponseResult).getData();
                    addVehicleResponse.setValue(new FinalResult((AddVehicleResponse) data));

                } else if (addVehicleResponseResult instanceof Result.Error) {
                    AddVehicleResponse error = ((Result.Error<AddVehicleResponse>) addVehicleResponseResult).getError();
                    addVehicleResponse.setValue(new FinalResult((LoginError) error.getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void submitGeneralInformation(GeneralInfoRequest request) {
        inspectionRepository.submitGeneralInfoApi(request);
        inspectionRepository.getGeneralInfoResponse().observeForever(generalInfoResponseResult -> {
            try {
                if (generalInfoResponseResult instanceof Result.Success) {
                    GeneralInfoResponse data = ((Result.Success<GeneralInfoResponse>) generalInfoResponseResult).getData();
                    generalInfoResponse.setValue(new FinalResult((GeneralInfoResponse) data));
                    Log.e("generalInfoResponseResult: ", "success");
                } else if (generalInfoResponseResult instanceof Result.Error) {
                    GeneralInfoResponse error = ((Result.Error<GeneralInfoResponse>) generalInfoResponseResult).getError();
                    generalInfoResponse.setValue(new FinalResult(new FinalResult((LoginError) error.getError())));
                    Log.e("generalInfoResponseResult: ", "failed");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void addExteriorImages(AddExteriorImagesRequest request) {
        inspectionRepository.addExteriorImagesApi(request);
        inspectionRepository.getAddExteriorImagesResponse().observeForever(exteriorImagesResponseResult -> {
            try {
                if (exteriorImagesResponseResult instanceof Result.Success) {
                    AddExteriorImagesResponse data = ((Result.Success<AddExteriorImagesResponse>) exteriorImagesResponseResult).getData();
                    addExteriorImagesResponse.setValue(new FinalResult((AddExteriorImagesResponse) data));
                } else if (exteriorImagesResponseResult instanceof Result.Error) {
                    AddExteriorImagesResponse error = ((Result.Error<AddExteriorImagesResponse>) exteriorImagesResponseResult).getError();
                    addExteriorImagesResponse.setValue(new FinalResult(new FinalResult(error.getMessage())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void getCancelReasonDropDownData() {
        inspectionRepository.getCancelReasonDropDown();
        inspectionRepository.getCancelReasonDropDownResponse().observeForever(cancelReasonDropDownResponseResult -> {
            try {
                if (cancelReasonDropDownResponseResult instanceof Result.Success) {
                   // CancelReasonDropDownResponse data = ((Result.Success<CancelReasonDropDownResponse>) cancelReasonDropDownResponseResult).getData();
                    cancelReasonDropDownResponse.postValue(new FinalResult((CancelReasonDropDownResponse) ((Result.Success<?>) cancelReasonDropDownResponseResult).getData()));
                } else if (cancelReasonDropDownResponseResult instanceof Result.Error) {
                    CancelReasonDropDownResponse error = ((Result.Error<CancelReasonDropDownResponse>) cancelReasonDropDownResponseResult).getError();
                    cancelReasonDropDownResponse.postValue(new FinalResult(new FinalResult((LoginError) error.getError())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void submitInspectionCancelReason(InspectionCancelRequest request) {
        inspectionRepository.inspectionCancelReason(request);
        inspectionRepository.getCancelInspectionResponse().observeForever(cancelInspectionResponseResult -> {
            try {
                if (cancelInspectionResponseResult instanceof Result.Success) {
                    CancelInspectionResponse data = ((Result.Success<CancelInspectionResponse>) cancelInspectionResponseResult).getData();
                    cancelInspectionResponse.setValue(new FinalResult((CancelInspectionResponse) data));
                } else if (cancelInspectionResponseResult instanceof Result.Error) {
                    CancelInspectionResponse error = ((Result.Error<CancelInspectionResponse>) cancelInspectionResponseResult).getError();
                    cancelInspectionResponse.setValue(new FinalResult(new FinalResult(error.getMessage())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void inspectionPowerTrainTab(InspectionPowerTrainRequest request) {
        inspectionRepository.addInspectionPowerTrain(request);
        inspectionRepository.getPowerTrainTabResponse().observeForever(powerTrainTabResponseResult -> {
            try {
                if (powerTrainTabResponseResult instanceof Result.Success) {
                    InspectionPowerTrainTabResponse data = ((Result.Success<InspectionPowerTrainTabResponse>) powerTrainTabResponseResult).getData();
                    inspectionPowerTrainTabResponse.setValue(new FinalResult((InspectionPowerTrainTabResponse) data));
                } else if (powerTrainTabResponseResult instanceof Result.Error) {
                    //  InspectionPowerTrainTabResponse error =  ((Result.Error<InspectionPowerTrainTabResponse>) powerTrainTabResponseResult).getError();
                    inspectionPowerTrainTabResponse.setValue(new FinalResult(new FinalResult(((Result.Error<InspectionPowerTrainTabResponse>) powerTrainTabResponseResult).getError())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void powerTrainAddMultipleImages(PowerTrainAddImageRequest request) {
        inspectionRepository.powerTrainAddMultipleImages(request);
        inspectionRepository.getPowerTrainAddMultipleImageResponse().observeForever(powerTrainAddImageResponseResult -> {
            try {
                if (powerTrainAddImageResponseResult instanceof Result.Success) {
                    PowerTrainAddImageResponse data = ((Result.Success<PowerTrainAddImageResponse>) powerTrainAddImageResponseResult).getData();
                    powerTrainAddMultipleImageResponse.setValue(new FinalResult((PowerTrainAddImageResponse) data));
                } else if (powerTrainAddImageResponseResult instanceof Result.Error) {
                    PowerTrainAddImageResponse error = ((Result.Error<PowerTrainAddImageResponse>) powerTrainAddImageResponseResult).getError();
                    powerTrainAddMultipleImageResponse.setValue(new FinalResult(new FinalResult(error.getMessage())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void inspectionMechanical(InspectionMechanicalRequest request) {
        inspectionRepository.inspectionMechanical(request);
        inspectionRepository.getInspectionMechanicalResponse().observeForever(mechanicalResponseResult -> {
            try {
                if (mechanicalResponseResult instanceof Result.Success) {
                    InspectionMechanicalResponse data = ((Result.Success<InspectionMechanicalResponse>) mechanicalResponseResult).getData();
                    inspectionMechanicalResponse.setValue(new FinalResult((InspectionMechanicalResponse) data));
                } else if (mechanicalResponseResult instanceof Result.Error) {
                    //  InspectionPowerTrainTabResponse error =  ((Result.Error<InspectionPowerTrainTabResponse>) mechanicalResponseResult).getError();
                    inspectionMechanicalResponse.setValue(new FinalResult(new FinalResult(((Result.Error<InspectionMechanicalResponse>) mechanicalResponseResult).getError())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void inspectionMechanicalVideo(InspectionMechanicalVideoRequest request) {
        inspectionRepository.inspectionMechanicalVideo(request);
        inspectionRepository.getInspectionMechanicalVideoResponse().observeForever(inspectionMechanicalVideoResponseResult -> {
            try {
                try {
                    if (inspectionMechanicalVideoResponseResult instanceof Result.Success) {
                        InspectionMechanicalVideoResponse data = ((Result.Success<InspectionMechanicalVideoResponse>) inspectionMechanicalVideoResponseResult).getData();
                        inspectionMechanicalVideoResponse.setValue(new FinalResult((InspectionMechanicalVideoResponse) data));
                    } else if (inspectionMechanicalVideoResponseResult instanceof Result.Error) {
                        InspectionMechanicalVideoResponse error = ((Result.Error<InspectionMechanicalVideoResponse>) inspectionMechanicalVideoResponseResult).getError();
                        inspectionMechanicalVideoResponse.setValue(new FinalResult(new FinalResult(error.getMessage())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void inspectionTiresWheel(InspectionTiresWheelsRequest request) {
        inspectionRepository.inspectionTiresWheel(request);
        inspectionRepository.getInspectionTiresWheelResponse().observeForever(inspectionTiresWheelResponseResult -> {
            try {
                if (inspectionTiresWheelResponseResult instanceof Result.Success) {
                    InspectionTiresWheelResponse data = ((Result.Success<InspectionTiresWheelResponse>) inspectionTiresWheelResponseResult).getData();
                    inspectionTiresWheelResponse.setValue(new FinalResult((InspectionTiresWheelResponse) data));
                } else if (inspectionTiresWheelResponseResult instanceof Result.Error) {
                    //  InspectionPowerTrainTabResponse error =  ((Result.Error<InspectionPowerTrainTabResponse>) mechanicalResponseResult).getError();
                    inspectionTiresWheelResponse.setValue(new FinalResult(new FinalResult(((Result.Error<InspectionTiresWheelResponse>) inspectionTiresWheelResponseResult).getError())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void inspectionTiresWheelImageApi(InspectionTiresWheelImageRequest request) {
        inspectionRepository.inspectionTiresWheelImageApi(request);
        inspectionRepository.getInspectionTiresWheelImageResponse().observeForever(inspectionMechanicalVideoResponseResult -> {
            try {
                if (inspectionMechanicalVideoResponseResult instanceof Result.Success) {
                    InspectionTiresWheelImageResponse data = ((Result.Success<InspectionTiresWheelImageResponse>) inspectionMechanicalVideoResponseResult).getData();
                    inspectionTiresWheelImageResponse.setValue(new FinalResult((InspectionTiresWheelImageResponse) data));
                } else if (inspectionMechanicalVideoResponseResult instanceof Result.Error) {
                    InspectionTiresWheelImageResponse error = ((Result.Error<InspectionTiresWheelImageResponse>) inspectionMechanicalVideoResponseResult).getError();
                    inspectionTiresWheelImageResponse.setValue(new FinalResult(new FinalResult(error.getMessage())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void exteriorTab(ExteriorTabRequest request) {
        inspectionRepository.exteriorTab(request);
        inspectionRepository.getExteriorTabResponse().observeForever(exteriorTabResponseResult -> {
            try {
                if (exteriorTabResponseResult instanceof Result.Success) {
                    ExteriorTabResponse data = ((Result.Success<ExteriorTabResponse>) exteriorTabResponseResult).getData();
                    exteriorTabResponse.setValue(new FinalResult((ExteriorTabResponse) data));
                } else if (exteriorTabResponseResult instanceof Result.Error) {
                    exteriorTabResponse.setValue(new FinalResult(((Result.Error<ExteriorTabResponse>) exteriorTabResponseResult).getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void exteriorAddImageApi(ExteriorAddImageRequest request) {
        inspectionRepository.exteriorAddImageApi(request);
        inspectionRepository.getExteriorAddImageResponse().observeForever(exteriorAddImageResponseResult -> {
            try {
                if (exteriorAddImageResponseResult instanceof Result.Success) {
                    ExteriorAddImageResponse data = ((Result.Success<ExteriorAddImageResponse>) exteriorAddImageResponseResult).getData();
                    exteriorAddImageResponse.setValue(new FinalResult((ExteriorAddImageResponse) data));
                } else if (exteriorAddImageResponseResult instanceof Result.Error) {
                    ExteriorAddImageResponse error = ((Result.Error<ExteriorAddImageResponse>) exteriorAddImageResponseResult).getError();
                    exteriorAddImageResponse.setValue(new FinalResult(new FinalResult(error.getMessage())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void interiorTab(InteriorTabRequest request) {
        inspectionRepository.interiorTabApi(request);
        inspectionRepository.getInteriorTabResponse().observeForever(interiorTabResponseResult -> {
            try {
                if (interiorTabResponseResult instanceof Result.Success) {
                    InteriorTabResponse data = ((Result.Success<InteriorTabResponse>) interiorTabResponseResult).getData();
                    interiorTabResponse.setValue(new FinalResult((InteriorTabResponse) data));
                } else if (interiorTabResponseResult instanceof Result.Error) {
                    interiorTabResponse.setValue(new FinalResult(((Result.Error<InteriorTabResponse>) interiorTabResponseResult).getError()));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void interiorAddImageApi(InteriorAddImageRequest request) {
        inspectionRepository.interiorAddImageApi(request);
        inspectionRepository.getInteriorAddImageResponse().observeForever(interiorAddImageResponseResult -> {
            try {
                if (interiorAddImageResponseResult instanceof Result.Success) {
                    InteriorAddImageResponse data = ((Result.Success<InteriorAddImageResponse>) interiorAddImageResponseResult).getData();
                    interiorAddImageResponse.setValue(new FinalResult((InteriorAddImageResponse) data));
                } else if (interiorAddImageResponseResult instanceof Result.Error) {
                    InteriorAddImageResponse error = ((Result.Error<InteriorAddImageResponse>) interiorAddImageResponseResult).getError();
                    interiorAddImageResponse.setValue(new FinalResult(new FinalResult(error.getMessage())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void testDriveTabApi(TestDriveRequest request) {
        inspectionRepository.testDriveTabApi(request);
        inspectionRepository.getTestDriveResponse().observeForever(testDriveResponseResult -> {
            try {
                if (testDriveResponseResult instanceof Result.Success) {
                    TestDriveResponse data = ((Result.Success<TestDriveResponse>) testDriveResponseResult).getData();
                    testDriveResponse.setValue(new FinalResult((TestDriveResponse) data));
                } else if (testDriveResponseResult instanceof Result.Error) {
                    //  InspectionPowerTrainTabResponse error =  ((Result.Error<InspectionPowerTrainTabResponse>) mechanicalResponseResult).getError();
                    testDriveResponse.setValue(new FinalResult(new FinalResult(((Result.Error<TestDriveResponse>) testDriveResponseResult).getError())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void testDriveAddImageApi(TestDriveAddImageRequest request) {
        inspectionRepository.testDriveAddImageApi(request);
        inspectionRepository.getTestDriveAddImageResponse().observeForever(testDriveAddImageResponseResult -> {
            try {
                if (testDriveAddImageResponseResult instanceof Result.Success) {
                    TestDriveAddImageResponse data = ((Result.Success<TestDriveAddImageResponse>) testDriveAddImageResponseResult).getData();
                    testDriveAddImageResponse.setValue(new FinalResult((TestDriveAddImageResponse) data));
                } else if (testDriveAddImageResponseResult instanceof Result.Error) {
                    TestDriveAddImageResponse error = ((Result.Error<TestDriveAddImageResponse>) testDriveAddImageResponseResult).getError();
                    testDriveAddImageResponse.setValue(new FinalResult(new FinalResult(error.getMessage())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void pendingSearchApi(PendingSearchRequest request) {
        inspectionRepository.pendingSearchApi(request);
        inspectionRepository.getPendingSearchResponse().observeForever(pendingSearchResponseResult -> {
            try {
                if (pendingSearchResponseResult instanceof Result.Success) {
                    CompletedInspectionResponse data = ((Result.Success<CompletedInspectionResponse>) pendingSearchResponseResult).getData();
                    pendingSearchResponse.setValue(new FinalResult((CompletedInspectionResponse) data));
                } else if (pendingSearchResponseResult instanceof Result.Error) {
                    CompletedInspectionResponse error = ((Result.Error<CompletedInspectionResponse>) pendingSearchResponseResult).getError();
                    pendingSearchResponse.setValue(new FinalResult(new FinalResult("Error")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void completeSearchApi(CompleteSearchRequest request) {
        inspectionRepository.completeSearchApi(request);
        inspectionRepository.getCompleteSearchResponse().observeForever(testDriveAddImageResponseResult -> {
            try {
                if (testDriveAddImageResponseResult instanceof Result.Success) {
                    CompletedInspectionResponse data = ((Result.Success<CompletedInspectionResponse>) testDriveAddImageResponseResult).getData();
                    completeSearchResponse.setValue(new FinalResult((CompletedInspectionResponse) data));
                } else if (testDriveAddImageResponseResult instanceof Result.Error) {
                    CompletedInspectionResponse error = ((Result.Error<CompletedInspectionResponse>) testDriveAddImageResponseResult).getError();
                    completeSearchResponse.setValue(new FinalResult(new FinalResult("Error")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void monthSearchApi(MonthSearchRequest request) {
        inspectionRepository.monthSearchApi(request);
        inspectionRepository.getMonthSearchResponse().observeForever(monthSearchResponseResult -> {
            try {
                if (monthSearchResponseResult instanceof Result.Success) {
                    CompletedInspectionResponse data = ((Result.Success<CompletedInspectionResponse>) monthSearchResponseResult).getData();
                    monthSearchResponse.setValue(new FinalResult((CompletedInspectionResponse) data));
                } else if (monthSearchResponseResult instanceof Result.Error) {
                    CompletedInspectionResponse error = ((Result.Error<CompletedInspectionResponse>) monthSearchResponseResult).getError();
                    monthSearchResponse.setValue(new FinalResult(new FinalResult("Error")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void reOpenSearchApi(ReopenSearchRequest request) {
        inspectionRepository.reOpenSearchApi(request);
        inspectionRepository.getReOpenSearchResponse().observeForever(reopenSearchResponseResult -> {
            try {
                if (reopenSearchResponseResult instanceof Result.Success) {
                    CompletedInspectionResponse data = ((Result.Success<CompletedInspectionResponse>) reopenSearchResponseResult).getData();
                    reOpenSearchResponse.setValue(new FinalResult((CompletedInspectionResponse) data));
                } else if (reopenSearchResponseResult instanceof Result.Error) {
                    CompletedInspectionResponse error = ((Result.Error<CompletedInspectionResponse>) reopenSearchResponseResult).getError();
                    reOpenSearchResponse.setValue(new FinalResult(new FinalResult("Error")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void profileEditApi(ProfileEditRequest request) {
        inspectionRepository.profileEditApi(request);
        inspectionRepository.getProfileEditResponse().observeForever(editResponseResult -> {
            try {
                if (editResponseResult instanceof Result.Success) {
                    ProfileEditResponse data = ((Result.Success<ProfileEditResponse>) editResponseResult).getData();
                    profileEditResponse.setValue(new FinalResult((ProfileEditResponse) data));
                } else if (editResponseResult instanceof Result.Error) {
                    ProfileEditResponse error = ((Result.Error<ProfileEditResponse>) editResponseResult).getError();
                    profileEditResponse.setValue(new FinalResult(new FinalResult("Error")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

}

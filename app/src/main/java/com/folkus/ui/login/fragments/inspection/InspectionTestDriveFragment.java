package com.folkus.ui.login.fragments.inspection;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import static com.folkus.ui.login.ActivityHome.clearExteriorTempData;
import static com.folkus.ui.login.ActivityHome.clearFirstTimeCalledTempData;
import static com.folkus.ui.login.ActivityHome.clearInsExteriorImagesTempData;
import static com.folkus.ui.login.ActivityHome.clearInsInteriorImagesTempData;
import static com.folkus.ui.login.ActivityHome.clearInteriorTempData;
import static com.folkus.ui.login.ActivityHome.clearMechanicalTempData;
import static com.folkus.ui.login.ActivityHome.clearMultipleImageAndVideoTempData;
import static com.folkus.ui.login.ActivityHome.clearPowerTrainTempData;
import static com.folkus.ui.login.ActivityHome.clearTestDriveTempData;
import static com.folkus.ui.login.ActivityHome.clearTiresWheelTempData;
import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.generalInfo;
import static com.folkus.ui.login.InspectionRequestViewModel.testDriveFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.testDrive_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.testDrive_uriArrayList;
import static com.folkus.ui.login.fragments.inspection.InspectionPowerTrainFragment.getImageUri;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.automaticTransmission_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.breakingSenese_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.differential_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.exilatorLevel_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.manualTransmission_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.steeringControls_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.testDriveComments_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.transferCase_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.uploadTestDriveVideoEncode_;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.folkus.BuildConfig;
import com.folkus.R;
import com.folkus.data.remote.request.TestDriveAddImageData;
import com.folkus.data.remote.request.TestDriveAddImageRequest;
import com.folkus.data.remote.request.TestDriveRequest;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.TestDriveAddImageResponse;
import com.folkus.data.remote.response.TestDriveResponse;
import com.folkus.databinding.FragmentInspectionTestDriveBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.google.android.material.tabs.TabLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InspectionTestDriveFragment extends Fragment {
    private static final String TAG = "InspectionTestDriveFragment";
    private FragmentInspectionTestDriveBinding binding;
    private TestDriveAdapter testDriveAdapter;

    private String automaticTransmission = "";
    private String manualTransmission = "";
    private String exilatorLevel = "";
    private String breakingSenese = "";
    private String steeringControls = "";
    private String transferCase = "";
    private String differential = "";
    private String comments = null;

    String imagePath;
    private String uploadTestDriveVideoEncode = null;
    ArrayList<GeneralInfoData> generalInfoData = new ArrayList<>();
    int carId;
    private int sellerDealerId;
    private int inspectorId;
    InspectionRequestViewModel inspectionRequestViewModel;


    private final String Image_Type = "Test Drive Images";
    private final String Iamge_Type = "testdrivevideo";
    private final String Type = "video/mp4 or image/jpeg";
    private final String Base64_Prefix = "data:image/jpeg;base64,";
    private final String Base64_Video_Prefix = "data:video/mp4;base64,";
    String addMultipleImage = null;
    String fileName = "";
    String fileSize = "";
    static boolean isCalledTestDrive = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInspectionTestDriveBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.title_inspection));
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            carId = generalInfo.getCarId();
            sellerDealerId = generalInfo.getSellerDealerId();
            inspectorId = generalInfo.getInspectorId();
            Log.e(TAG, "onViewCreated: " + carId + "/" + sellerDealerId + "/" + inspectorId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        subscribeUI();
    }

    private void subscribeUI() {
        if (testDrive_uriArrayList != null) {
            if (!testDriveFirstTimeCalled) {
                testDrive_uriArrayList.add(Uri.parse(""));
                testDriveFirstTimeCalled = true;
            }
        }

        if (testDrive_picturePathsList != null) {
            testDrive_picturePathsList.clear();
            testDrive_picturePathsList.addAll(testDrive_uriArrayList);
        }
        testDriveAdapter = new TestDriveAdapter(requireActivity(), testDrive_picturePathsList, InspectionTestDriveFragment.this);
        binding.addMultipleImageVideoRecylerView.setAdapter(testDriveAdapter);
        binding.addMultipleImageVideoRecylerView.hasFixedSize();

        if (testDrive_picturePathsList != null) {
            testDriveAdapter.updateList(testDrive_picturePathsList);
        }

        binding.addTestDriveVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTestDriveVideo();
            }
        });

        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                navController.popBackStack();
            }
        });

        binding.testDriveSubmitBtn.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                automaticTransmission = currentTabName(binding.automaticTransmissionSwitch.getSelectedTabPosition());
                manualTransmission = currentTabName(binding.manualTransmissionSwitch.getSelectedTabPosition());
                exilatorLevel = currentTabName(binding.exilatorLevelSwitch.getSelectedTabPosition());
                breakingSenese = currentTabName(binding.breakingSenseSwitch.getSelectedTabPosition());
                steeringControls = currentTabName(binding.steeringControlsSwitch.getSelectedTabPosition());
                transferCase = currentTabName(binding.transferCaseSwitch.getSelectedTabPosition());
                differential = currentTabName(binding.differentialSwitch.getSelectedTabPosition());
                comments = binding.comments.getText().toString().trim();
                automaticTransmission_ = binding.automaticTransmissionSwitch.getSelectedTabPosition();
                manualTransmission_ = binding.manualTransmissionSwitch.getSelectedTabPosition();
                exilatorLevel_ = binding.exilatorLevelSwitch.getSelectedTabPosition();
                breakingSenese_ = binding.breakingSenseSwitch.getSelectedTabPosition();
                steeringControls_ = binding.steeringControlsSwitch.getSelectedTabPosition();
                transferCase_ = binding.transferCaseSwitch.getSelectedTabPosition();
                differential_ = binding.differentialSwitch.getSelectedTabPosition();
                testDriveComments_ = comments;
                Log.d("submit", automaticTransmission + "\n" + manualTransmission + "\n" + exilatorLevel + "\n" + breakingSenese + "\n" + steeringControls + "\n" + transferCase + "\n" + differential + "\n" + comments);

                try {
                    TestDriveRequest testDriveRequest = new TestDriveRequest();
                    testDriveRequest.setCarId(carId + "");
                    testDriveRequest.setSellerDealerId(sellerDealerId + "");
                    testDriveRequest.setInspectorId(inspectorId + "");
                    testDriveRequest.setAutomaticTransmission(automaticTransmission);
                    testDriveRequest.setManualTransmission(manualTransmission);
                    testDriveRequest.setExilatorLevel(exilatorLevel);
                    testDriveRequest.setBreakingSenses(breakingSenese);
                    testDriveRequest.setSteeringControls(steeringControls);
                    testDriveRequest.setTransferCase(transferCase);
                    testDriveRequest.setDifferential(differential);
                    testDriveRequest.setComments(comments);
                    isCalledTestDrive = false;
                    inspectionRequestViewModel.testDriveTabApi(testDriveRequest);
                    binding.progressBarTestDrive.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        binding.automaticTransmissionSwitch.addTab(binding.automaticTransmissionSwitch.newTab().setText("Good"));
        binding.automaticTransmissionSwitch.addTab(binding.automaticTransmissionSwitch.newTab().setText("Bad"));
        binding.automaticTransmissionSwitch.addTab(binding.automaticTransmissionSwitch.newTab().setText("N/A"));
        binding.automaticTransmissionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.automaticTransmissionSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.automaticTransmissionSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.automaticTransmissionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.automaticTransmissionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.automaticTransmissionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.automaticTransmissionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.automaticTransmissionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.automaticTransmissionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
                        break;
                }
                Log.e("TAG", "onTabSelected1: " + position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //binding.automaticTransmissionSwitch.getTabAt(0).select();


        binding.manualTransmissionSwitch.addTab(binding.manualTransmissionSwitch.newTab().setText("Good"));
        binding.manualTransmissionSwitch.addTab(binding.manualTransmissionSwitch.newTab().setText("Bad"));
        binding.manualTransmissionSwitch.addTab(binding.manualTransmissionSwitch.newTab().setText("N/A"));
        binding.manualTransmissionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.manualTransmissionSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.manualTransmissionSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.manualTransmissionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.manualTransmissionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.manualTransmissionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.manualTransmissionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.manualTransmissionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.manualTransmissionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
                        break;
                }
                Log.e("TAG", "onTabSelected1: " + position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //binding.manualTransmissionSwitch.getTabAt(0).select();


        binding.exilatorLevelSwitch.addTab(binding.exilatorLevelSwitch.newTab().setText("Good"));
        binding.exilatorLevelSwitch.addTab(binding.exilatorLevelSwitch.newTab().setText("Bad"));
        binding.exilatorLevelSwitch.addTab(binding.exilatorLevelSwitch.newTab().setText("N/A"));
        binding.exilatorLevelSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.exilatorLevelSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.exilatorLevelSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.exilatorLevelSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.exilatorLevelSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.exilatorLevelSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.exilatorLevelSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.exilatorLevelSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.exilatorLevelSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
                        break;
                }
                Log.e("TAG", "onTabSelected1: " + position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //  binding.exilatorLevelSwitch.getTabAt(0).select();

        binding.breakingSenseSwitch.addTab(binding.breakingSenseSwitch.newTab().setText("Good"));
        binding.breakingSenseSwitch.addTab(binding.breakingSenseSwitch.newTab().setText("Bad"));
        binding.breakingSenseSwitch.addTab(binding.breakingSenseSwitch.newTab().setText("N/A"));
        binding.breakingSenseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.breakingSenseSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.breakingSenseSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.breakingSenseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.breakingSenseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.breakingSenseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.breakingSenseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.breakingSenseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.breakingSenseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
                        break;
                }
                Log.e("TAG", "onTabSelected1: " + position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // binding.breakingSenseSwitch.getTabAt(0).select();

        binding.steeringControlsSwitch.addTab(binding.steeringControlsSwitch.newTab().setText("Good"));
        binding.steeringControlsSwitch.addTab(binding.steeringControlsSwitch.newTab().setText("Bad"));
        binding.steeringControlsSwitch.addTab(binding.steeringControlsSwitch.newTab().setText("N/A"));
        binding.steeringControlsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.steeringControlsSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.steeringControlsSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.steeringControlsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.steeringControlsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.steeringControlsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.steeringControlsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.steeringControlsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.steeringControlsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
                        break;
                }
                Log.e("TAG", "onTabSelected1: " + position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //binding.steeringControlsSwitch.getTabAt(0).select();

        binding.transferCaseSwitch.addTab(binding.transferCaseSwitch.newTab().setText("Good"));
        binding.transferCaseSwitch.addTab(binding.transferCaseSwitch.newTab().setText("Bad"));
        binding.transferCaseSwitch.addTab(binding.transferCaseSwitch.newTab().setText("N/A"));
        binding.transferCaseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.transferCaseSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.transferCaseSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.transferCaseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.transferCaseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.transferCaseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.transferCaseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.transferCaseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.transferCaseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
                        break;
                }
                Log.e("TAG", "onTabSelected1: " + position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //binding.transferCaseSwitch.getTabAt(0).select();

        binding.differentialSwitch.addTab(binding.differentialSwitch.newTab().setText("Good"));
        binding.differentialSwitch.addTab(binding.differentialSwitch.newTab().setText("Bad"));
        binding.differentialSwitch.addTab(binding.differentialSwitch.newTab().setText("N/A"));
        binding.differentialSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.differentialSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.differentialSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.differentialSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.differentialSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.differentialSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.differentialSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.differentialSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.differentialSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
                        break;
                }
                Log.e("TAG", "onTabSelected1: " + position);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //  binding.differentialSwitch.getTabAt(0).select();
        if (testDriveComments_ != null) {
            binding.comments.setText(testDriveComments_);
        }
        /* for load temp data*/
        binding.automaticTransmissionSwitch.selectTab(binding.automaticTransmissionSwitch.getTabAt(automaticTransmission_));
        binding.manualTransmissionSwitch.selectTab(binding.manualTransmissionSwitch.getTabAt(manualTransmission_));
        binding.exilatorLevelSwitch.selectTab(binding.exilatorLevelSwitch.getTabAt(exilatorLevel_));
        binding.breakingSenseSwitch.selectTab(binding.breakingSenseSwitch.getTabAt(breakingSenese_));
        binding.steeringControlsSwitch.selectTab(binding.steeringControlsSwitch.getTabAt(steeringControls_));
        binding.transferCaseSwitch.selectTab(binding.transferCaseSwitch.getTabAt(transferCase_));
        binding.differentialSwitch.selectTab(binding.differentialSwitch.getTabAt(differential_));

        if (uploadTestDriveVideoEncode_ != null) {
            binding.addTestDriveVideoBtn.setBackground(uploadTestDriveVideoEncode_);
        }

        if (!isCalledTestDrive) {
            inspectionRequestViewModel.getTestDriveResponse().observe(requireActivity(), finalResult -> {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    binding.progressBarTestDrive.setVisibility(View.GONE);
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    binding.progressBarTestDrive.setVisibility(View.GONE);
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof TestDriveResponse) {
                        TestDriveResponse testDriveResponse = (TestDriveResponse) success1;
                        boolean success = testDriveResponse.isSuccess();
                        if (success) {
                            binding.progressBarTestDrive.setVisibility(View.GONE);
                            naviagetToInspectionTab();
                            isCalledTestDrive = true;
                        } else {
                            binding.progressBarTestDrive.setVisibility(View.GONE);
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        binding.progressBarTestDrive.setVisibility(View.GONE);
                    }
                }
            });
        }

        inspectionRequestViewModel.getTestDriveAddImageResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarTestDrive.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarTestDrive.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof TestDriveAddImageResponse) {
                    TestDriveAddImageResponse testDriveAddImageResponse = (TestDriveAddImageResponse) success1;
                    boolean success = testDriveAddImageResponse.isSuccess();
                    binding.progressBarTestDrive.setVisibility(View.GONE);
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarTestDrive.setVisibility(View.GONE);
                }
            }
        });
    }

    private void selectTestDriveVideo() {
        final CharSequence[] options = {"Record Video", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Record Video")) {
                    File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/" + formatVideoFileName());
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri fileUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", mediaFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    uploadTestDriveVideoActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    uploadTestDriveVideoActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public void addMultipleImageAndVideos() {
        final CharSequence[] options = {"Take Photo", "Record Video", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.upload_add_image_video);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    addImageActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Record Video")) {
                    File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/" + formatVideoFileName());
                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri fileUri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", mediaFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    addImageActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/* video/*");
                    addImageActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void naviagetToInspectionTab() {
        try {
            navController.navigate(R.id.testDriveToInspectionFragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        findNavController(InspectionTestDriveFragment.this).
//                navigate(R.id.nav_inspection_tab, null, new NavOptions.Builder().
//                        setPopUpTo(findNavController(InspectionTestDriveFragment.this)
//                                .getGraph().getStartDestinationId(), true).build());
        /*clear all temp data*/
        clearInsExteriorImagesTempData();
        clearInsInteriorImagesTempData();
        clearPowerTrainTempData();
        clearMechanicalTempData();
        clearTiresWheelTempData();
        clearExteriorTempData();
        clearInteriorTempData();
        clearTestDriveTempData();
        clearMultipleImageAndVideoTempData();
        clearFirstTimeCalledTempData();
    }

    ActivityResultLauncher<Intent> addImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri imageUri = null;
                        Uri selectedImage = null;
                        if (data != null) {
                            Uri selectedUri = data.getData();
                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                imageUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "onActivityResult: " + imageUri);
                                if (imageUri != null) {
                                    if (testDriveAdapter.isImageFile(imageUri)) {
                                        Cursor returnCursor = requireContext().getContentResolver().query(imageUri, null, null, null, null);
                                        try {
                                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                            int size = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                                            returnCursor.moveToFirst();
                                            fileName = returnCursor.getString(nameIndex);
                                            fileSize = returnCursor.getString(size);
                                            Log.e("path1", fileName + " /" + bytesIntoHumanReadable(Long.parseLong(fileSize)));
                                        } catch (Exception e) {
                                            //handle the failure cases here
                                        } finally {
                                            returnCursor.close();
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            selectedImage = selectedUri != null ? selectedUri : imageUri;
                            testDrive_uriArrayList.add(selectedImage);
                            testDrive_picturePathsList.clear();
                            testDrive_picturePathsList.addAll(testDrive_uriArrayList);
                            testDriveAdapter.updateList(testDrive_picturePathsList); // add this

                            if (testDriveAdapter.isImageFile(selectedImage)) {
                                Cursor returnCursor = requireContext().getContentResolver().query(selectedImage, null, null, null, null);
                                try {
                                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                    int size = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                                    returnCursor.moveToFirst();
                                    fileName = returnCursor.getString(nameIndex);
                                    fileSize = returnCursor.getString(size);
                                } catch (Exception e) {
                                    //handle the failure cases here
                                } finally {
                                    returnCursor.close();
                                }

                                String[] filePath = {MediaStore.Images.Media.DATA};
                                Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                                c.moveToFirst();
                                int columnIndex = c.getColumnIndex(filePath[0]);
                                imagePath = c.getString(columnIndex);
                                c.close();

                                Bitmap bitmap = (BitmapFactory.decodeFile(imagePath));
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                addMultipleImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            }

                            if (testDriveAdapter.isVideoFile(selectedImage)) {
                                Cursor returnCursor = requireContext().getContentResolver().query(selectedImage, null, null, null, null);
                                try {
                                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                    int size = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                                    returnCursor.moveToFirst();
                                    fileName = returnCursor.getString(nameIndex);
                                    fileSize = returnCursor.getString(size);
                                } catch (Exception e) {
                                    //handle the failure cases here
                                } finally {
                                    returnCursor.close();
                                }

                                String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
                                Cursor cursor = requireActivity().getContentResolver().query(selectedImage, projection, null, null, null);
                                cursor.moveToFirst();
                                InputStream inputStream = null;
                                // Converting the video in to the bytes
                                try {
                                    inputStream = requireActivity().getContentResolver().openInputStream(selectedImage);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                int bufferSize = Integer.parseInt(fileSize);
                                byte[] buffer = new byte[bufferSize];
                                ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                                int len = 0;
                                try {
                                    while ((len = inputStream.read(buffer)) != -1) {
                                        byteBuffer.write(buffer, 0, len);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                addMultipleImage = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                            }

                            if (addMultipleImage != null) {
                                TestDriveAddImageRequest testDriveAddImageRequest = new TestDriveAddImageRequest();
                                testDriveAddImageRequest.setImageType(Image_Type);
                                testDriveAddImageRequest.setIamgeType(Iamge_Type);
                                testDriveAddImageRequest.setCarId(carId + "");
                                testDriveAddImageRequest.setOthers("2");
                                testDriveAddImageRequest.setPage("9");

                                ArrayList<TestDriveAddImageData> testDriveDataArrayList = new ArrayList<>();
                                TestDriveAddImageData testDriveAddImageData = new TestDriveAddImageData();
                                testDriveAddImageData.setName(fileName);
                                if (testDriveAdapter.isImageFile(selectedImage))
                                    testDriveAddImageData.setType(Type);
                                else
                                    testDriveAddImageData.setType("video/mp4");

                                testDriveAddImageData.setSize(fileSize);
                                testDriveAddImageData.setBase64(Base64_Prefix + addMultipleImage);
                                testDriveDataArrayList.add(testDriveAddImageData);

                                testDriveAddImageRequest.setImage(testDriveDataArrayList);
                                inspectionRequestViewModel.testDriveAddImageApi(testDriveAddImageRequest);
                                binding.progressBarTestDrive.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> uploadTestDriveVideoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedVideoUri = data.getData();
                            uploadTestDriveVideoEncode = null;
                            Cursor returnCursor = requireContext().getContentResolver().query(selectedVideoUri, null, null, null, null);
                            try {
                                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                int size = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                                returnCursor.moveToFirst();
                                fileName = returnCursor.getString(nameIndex);
                                fileSize = returnCursor.getString(size);
                            } catch (Exception e) {
                                //handle the failure cases here
                            } finally {
                                returnCursor.close();
                            }

                            String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
                            Cursor cursor = requireActivity().getContentResolver().query(selectedVideoUri, projection, null, null, null);
                            cursor.moveToFirst();
                            String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                            // Setting the thumbnail of the video in to the image view
                            BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                            binding.addTestDriveVideoBtn.setBackground(bitmapD);
                            uploadTestDriveVideoEncode_ = bitmapD;
                            InputStream inputStream = null;
                            // Converting the video in to the bytes
                            try {
                                inputStream = requireActivity().getContentResolver().openInputStream(selectedVideoUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            int bufferSize = Integer.parseInt(fileSize);
                            byte[] buffer = new byte[bufferSize];
                            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                            int len = 0;
                            try {
                                while ((len = inputStream.read(buffer)) != -1) {
                                    byteBuffer.write(buffer, 0, len);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Converting bytes into base64
                            uploadTestDriveVideoEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                            if (uploadTestDriveVideoEncode != null) {
                                TestDriveAddImageRequest testDriveAddImageRequest = new TestDriveAddImageRequest();
                                testDriveAddImageRequest.setImageType(Image_Type);
                                testDriveAddImageRequest.setIamgeType(Iamge_Type);
                                testDriveAddImageRequest.setCarId(carId + "");
                                testDriveAddImageRequest.setOthers("2");
                                testDriveAddImageRequest.setPage("9");

                                ArrayList<TestDriveAddImageData> testDriveDataArrayList = new ArrayList<>();
                                TestDriveAddImageData testDriveAddImageData = new TestDriveAddImageData();
                                testDriveAddImageData.setName(fileName);
                                testDriveAddImageData.setType(Type);
                                testDriveAddImageData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));

                                testDriveAddImageData.setBase64(Base64_Video_Prefix + uploadTestDriveVideoEncode);
                                testDriveDataArrayList.add(testDriveAddImageData);

                                testDriveAddImageRequest.setImage(testDriveDataArrayList);
                                inspectionRequestViewModel.testDriveAddImageApi(testDriveAddImageRequest);
                                binding.progressBarTestDrive.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });
    ActivityResultLauncher<Intent> uploadTestDriveVideoActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedVideoUri = data.getData();
                            uploadTestDriveVideoEncode = null;
                            Cursor returnCursor = requireContext().getContentResolver().query(selectedVideoUri, null, null, null, null);
                            try {
                                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                int size = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                                returnCursor.moveToFirst();
                                fileName = returnCursor.getString(nameIndex);
                                fileSize = returnCursor.getString(size);
                            } catch (Exception e) {
                                //handle the failure cases here
                            } finally {
                                returnCursor.close();
                            }

                            String[] projection = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
                            Cursor cursor = requireActivity().getContentResolver().query(selectedVideoUri, projection, null, null, null);
                            cursor.moveToFirst();
                            String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
                            // Setting the thumbnail of the video in to the image view
                            BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                            binding.addTestDriveVideoBtn.setBackground(bitmapD);
                            uploadTestDriveVideoEncode_ = bitmapD;
                            InputStream inputStream = null;
                            // Converting the video in to the bytes
                            try {
                                inputStream = requireActivity().getContentResolver().openInputStream(selectedVideoUri);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            int bufferSize = Integer.parseInt(fileSize);
                            byte[] buffer = new byte[bufferSize];
                            ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
                            int len = 0;
                            try {
                                while ((len = inputStream.read(buffer)) != -1) {
                                    byteBuffer.write(buffer, 0, len);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            //Converting bytes into base64
                            uploadTestDriveVideoEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                            if (uploadTestDriveVideoEncode != null) {
                                TestDriveAddImageRequest testDriveAddImageRequest = new TestDriveAddImageRequest();
                                testDriveAddImageRequest.setImageType(Image_Type);
                                testDriveAddImageRequest.setIamgeType(Iamge_Type);
                                testDriveAddImageRequest.setCarId(carId + "");
                                testDriveAddImageRequest.setOthers("2");
                                testDriveAddImageRequest.setPage("9");

                                ArrayList<TestDriveAddImageData> testDriveDataArrayList = new ArrayList<>();
                                TestDriveAddImageData testDriveAddImageData = new TestDriveAddImageData();
                                testDriveAddImageData.setName(fileName);
                                testDriveAddImageData.setType(Type);
                                testDriveAddImageData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));

                                testDriveAddImageData.setBase64(Base64_Video_Prefix + uploadTestDriveVideoEncode);
                                testDriveDataArrayList.add(testDriveAddImageData);

                                testDriveAddImageRequest.setImage(testDriveDataArrayList);
                                inspectionRequestViewModel.testDriveAddImageApi(testDriveAddImageRequest);
                                binding.progressBarTestDrive.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });


    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (requireContext().getContentResolver() != null) {
            Cursor cursor = requireContext().getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private String currentTabName(int selectedPosition) {
        String name = "";
        if (selectedPosition == 0) {
            name = "Good";
        } else if (selectedPosition == 1) {
            name = "Bad";
        } else if (selectedPosition == 2) {
            name = "not applicable";
        }
        return name;
    }

    private String bytesIntoHumanReadable(long bytes) {
        long kilobyte = 1024;
        long megabyte = kilobyte * 1024;
        long gigabyte = megabyte * 1024;
        long terabyte = gigabyte * 1024;

        if ((bytes >= 0) && (bytes < kilobyte)) {
            return bytes + " B";

        } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
            return (bytes / kilobyte) + " KB";

        } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
            return (bytes / megabyte) + " MB";

        } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
            return (bytes / gigabyte) + " GB";

        } else if (bytes >= terabyte) {
            return (bytes / terabyte) + " TB";

        } else {
            return bytes + " Bytes";
        }
    }

    private String formatVideoFileName() {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String videoFileName = "InspectionCarVideo_" + timeStamp + ".mp4";
        return videoFileName;
    }
}
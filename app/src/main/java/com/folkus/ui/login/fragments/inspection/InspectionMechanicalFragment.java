package com.folkus.ui.login.fragments.inspection;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.generalInfo;
import static com.folkus.ui.login.InspectionRequestViewModel.mechanicalFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.mechanical_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.mechanical_uriArrayList;
import static com.folkus.ui.login.InspectionRequestViewModel.powerTrain_picturePathsList;
import static com.folkus.ui.login.fragments.inspection.InspectionPowerTrainFragment.getImageUri;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.acRunsHot_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.addMultipleImagesEncode_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.catalyticConverter_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.differentialOperation_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.engineBottomEnd_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.engineLightStartUp_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.engineUpperEnd_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.heaterRunsHot_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.mechanicalComments_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.noAds_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.noSRS_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.uploadVideoExactionEncode_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.uploadVideoStandEncode_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.uploadVideoStartCarEncode_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.engineBottomNoise_;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.BuildConfig;
import com.folkus.R;
import com.folkus.data.remote.request.InspectionMechanicalRequest;
import com.folkus.data.remote.request.InspectionMechanicalVideoData;
import com.folkus.data.remote.request.InspectionMechanicalVideoRequest;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.InspectionMechanicalResponse;
import com.folkus.data.remote.response.InspectionMechanicalVideoResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.FragmentInspectionMechanicalBinding;
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

public class InspectionMechanicalFragment extends Fragment {
    private static final String TAG = "InspectionMechanicalFragment";
    private FragmentInspectionMechanicalBinding binding;
    private InspectionMechanicalAdapter inspectionMechanicalAdapter;

    private String engineUpperEnd = "";
    private String engineBottomEnd = "";
    private String catalyticConverter = "";
    private String heaterRunsHot = "";
    private String acRunsHot = "";
    private String engineLightStartUp = "";
    private String noAds = "";
    private String noSRS = "";
    private String differentialOperation = "";
    private String comments = null;

    private String uploadVideoExactionEncode = null;
    private String uploadVideoStandEncode = null;
    private String uploadVideoStartCarEncode = null;
    private String addMultipleImagesEncode = null;

    String imagePath;
    ArrayList<GeneralInfoData> generalInfoData = new ArrayList<>();
    int carId;
    private int sellerDealerId;
    private int inspectorId;
    InspectionRequestViewModel inspectionRequestViewModel;

    private final String Mechanical_Images = "Mechanical Images";
    private final String Type = "video/mp4";
    private final String Base64_Prefix = "data:image/jpeg;base64,";
    private final String Base64_Video_Prefix = "data:video/mp4;base64,";
    private String fileName = "";
    private String fileSize = "";
    static boolean isCalledMechanical = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInspectionMechanicalBinding.inflate(inflater, container, false);
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

    private void selectUploadStartVideo() {
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
                    uploadVideoStartCarActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    uploadVideoStartCarActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void selectUploadVideoStand() {
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
                    uploadVideoStandActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    uploadVideoStandActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void selectUploadVideoExactionStand() {
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
                    uploadVideoExactionActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("video/*");
                    uploadVideoExactionActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }


    private void subscribeUI() {
        if (mechanical_uriArrayList != null) {
            if (!mechanicalFirstTimeCalled) {
                mechanical_uriArrayList.add(Uri.parse(""));
                mechanicalFirstTimeCalled = true;
            }
        }

        if (mechanical_picturePathsList != null) {
            mechanical_picturePathsList.clear();
            mechanical_picturePathsList.addAll(mechanical_uriArrayList);
        }

        inspectionMechanicalAdapter = new InspectionMechanicalAdapter(requireActivity(), mechanical_picturePathsList, InspectionMechanicalFragment.this);
        binding.addMultipleImageVideoRecylerView.setAdapter(inspectionMechanicalAdapter);
        binding.addMultipleImageVideoRecylerView.hasFixedSize();

        if (mechanical_picturePathsList != null) {
            inspectionMechanicalAdapter.updateList(mechanical_picturePathsList);
        }

        if (mechanicalComments_ != null)
            binding.comments.setText(mechanicalComments_);

        binding.uploadVideoStartCarBtn.setOnTouchListener((view, motionEvent) -> {
            selectUploadStartVideo();
            return false;
        });


        binding.uploadVideoStandFrontBtn.setOnTouchListener((view, motionEvent) -> {
            selectUploadVideoStand();
            return false;
        });

        binding.uploadVideoExactionSmokeBtn.setOnTouchListener((view, motionEvent) -> {
            selectUploadVideoExactionStand();
            return false;
        });

        binding.mechanicalContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engineUpperEnd = currentTabName(binding.engineUpperEndSwitch.getSelectedTabPosition());
                engineBottomEnd = currentTabName(binding.engineBottomEndSwitch.getSelectedTabPosition());
                catalyticConverter = currentTabName(binding.catalyticConverterPresentSwitch.getSelectedTabPosition());
                heaterRunsHot = currentTabName(binding.heaterRunsHotSwitch.getSelectedTabPosition());
                acRunsHot = currentTabName(binding.acRunsHotSwitch.getSelectedTabPosition());
                engineLightStartUp = currentTabName(binding.engineLightStartupSwitch.getSelectedTabPosition());
                noAds = currentTabName(binding.noAbsSwitch.getSelectedTabPosition());
                noSRS = currentTabName(binding.noSrsSwitch.getSelectedTabPosition());
                differentialOperation = currentTabName(binding.mechDifferentialOperationSwitch.getSelectedTabPosition());
                comments = binding.comments.getText().toString().trim();
                engineUpperEnd_ = binding.engineUpperEndSwitch.getSelectedTabPosition();
                engineBottomEnd_ = binding.engineBottomEndSwitch.getSelectedTabPosition();
                catalyticConverter_ = binding.catalyticConverterPresentSwitch.getSelectedTabPosition();
                heaterRunsHot_ = binding.heaterRunsHotSwitch.getSelectedTabPosition();
                engineLightStartUp_ = binding.engineLightStartupSwitch.getSelectedTabPosition();
                noAds_ = binding.noAbsSwitch.getSelectedTabPosition();
                noSRS_ = binding.noSrsSwitch.getSelectedTabPosition();
                differentialOperation_ = binding.mechDifferentialOperationSwitch.getSelectedTabPosition();
                mechanicalComments_ = comments;

                Log.d(TAG, "onClick: " + engineUpperEnd + "\n" + engineBottomEnd + "\n" + catalyticConverter + "\n" + heaterRunsHot + "\n" + acRunsHot + "\n" + engineLightStartUp + "\n" + noAds + "\n" + noSRS + "\n" + differentialOperation + "\n" + comments);

                try {
                    InspectionMechanicalRequest inspectionMechanicalRequest = new InspectionMechanicalRequest();
                    inspectionMechanicalRequest.setCarId(carId + "");
                    inspectionMechanicalRequest.setSellerDealerId(sellerDealerId + "");
                    inspectionMechanicalRequest.setInspectorId(inspectorId + "");
                    inspectionMechanicalRequest.setUpperEnd(engineUpperEnd);
                    inspectionMechanicalRequest.setBottamEnd(engineBottomEnd);
                    inspectionMechanicalRequest.setCatalyticConverter(catalyticConverter);
                    inspectionMechanicalRequest.setHeaterRuns(heaterRunsHot);
                    inspectionMechanicalRequest.setAcRuns(acRunsHot);
                    inspectionMechanicalRequest.setEngineLight(engineLightStartUp);
                    inspectionMechanicalRequest.setNoAds(noAds);
                    inspectionMechanicalRequest.setNoSrs(noSRS);
                    inspectionMechanicalRequest.setDifferentialOperation(differentialOperation);
                    inspectionMechanicalRequest.setComments(comments);
                    isCalledMechanical = false;
                    inspectionRequestViewModel.inspectionMechanical(inspectionMechanicalRequest);
                    binding.progressBarInspectionMechanical.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.engineUpperEndSwitch.addTab(binding.engineUpperEndSwitch.newTab().setText("Good"));
        binding.engineUpperEndSwitch.addTab(binding.engineUpperEndSwitch.newTab().setText("Bad"));
        binding.engineUpperEndSwitch.addTab(binding.engineUpperEndSwitch.newTab().setText("N/A"));
        binding.engineUpperEndSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.engineUpperEndSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.engineUpperEndSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.engineUpperEndSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.engineUpperEndSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.engineUpperEndSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.engineUpperEndSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.engineUpperEndSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.engineUpperEndSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //binding.engineUpperEndSwitch.getTabAt(0).select();


        binding.engineBottomEndSwitch.addTab(binding.engineBottomEndSwitch.newTab().setText("Good"));
        binding.engineBottomEndSwitch.addTab(binding.engineBottomEndSwitch.newTab().setText("Bad"));
        binding.engineBottomEndSwitch.addTab(binding.engineBottomEndSwitch.newTab().setText("N/A"));
        binding.engineBottomEndSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.engineBottomEndSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.engineBottomEndSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.engineBottomEndSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.engineBottomEndSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.engineBottomEndSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.engineBottomEndSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.engineBottomEndSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.engineBottomEndSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.engineBottomEndSwitch.getTabAt(0).select();


        binding.catalyticConverterPresentSwitch.addTab(binding.catalyticConverterPresentSwitch.newTab().setText("Good"));
        binding.catalyticConverterPresentSwitch.addTab(binding.catalyticConverterPresentSwitch.newTab().setText("Bad"));
        binding.catalyticConverterPresentSwitch.addTab(binding.catalyticConverterPresentSwitch.newTab().setText("N/A"));
        binding.catalyticConverterPresentSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.catalyticConverterPresentSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.catalyticConverterPresentSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.catalyticConverterPresentSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.catalyticConverterPresentSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.catalyticConverterPresentSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.catalyticConverterPresentSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.catalyticConverterPresentSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.catalyticConverterPresentSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.catalyticConverterPresentSwitch.getTabAt(0).select();

        binding.heaterRunsHotSwitch.addTab(binding.heaterRunsHotSwitch.newTab().setText("Good"));
        binding.heaterRunsHotSwitch.addTab(binding.heaterRunsHotSwitch.newTab().setText("Bad"));
        binding.heaterRunsHotSwitch.addTab(binding.heaterRunsHotSwitch.newTab().setText("N/A"));
        binding.heaterRunsHotSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.heaterRunsHotSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.heaterRunsHotSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.heaterRunsHotSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.heaterRunsHotSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.heaterRunsHotSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.heaterRunsHotSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.heaterRunsHotSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.heaterRunsHotSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //  binding.heaterRunsHotSwitch.getTabAt(0).select();

        binding.acRunsHotSwitch.addTab(binding.acRunsHotSwitch.newTab().setText("Good"));
        binding.acRunsHotSwitch.addTab(binding.acRunsHotSwitch.newTab().setText("Bad"));
        binding.acRunsHotSwitch.addTab(binding.acRunsHotSwitch.newTab().setText("N/A"));
        binding.acRunsHotSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.acRunsHotSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.acRunsHotSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.acRunsHotSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.acRunsHotSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.acRunsHotSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.acRunsHotSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.acRunsHotSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.acRunsHotSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.acRunsHotSwitch.getTabAt(0).select();

        binding.engineLightStartupSwitch.addTab(binding.engineLightStartupSwitch.newTab().setText("Good"));
        binding.engineLightStartupSwitch.addTab(binding.engineLightStartupSwitch.newTab().setText("Bad"));
        binding.engineLightStartupSwitch.addTab(binding.engineLightStartupSwitch.newTab().setText("N/A"));
        binding.engineLightStartupSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.engineLightStartupSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.engineLightStartupSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.engineLightStartupSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.engineLightStartupSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.engineLightStartupSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.engineLightStartupSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.engineLightStartupSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.engineLightStartupSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //binding.engineLightStartupSwitch.getTabAt(0).select();

        binding.noAbsSwitch.addTab(binding.noAbsSwitch.newTab().setText("Good"));
        binding.noAbsSwitch.addTab(binding.noAbsSwitch.newTab().setText("Bad"));
        binding.noAbsSwitch.addTab(binding.noAbsSwitch.newTab().setText("N/A"));
        binding.noAbsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.noAbsSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.noAbsSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.noAbsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.noAbsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.noAbsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.noAbsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.noAbsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.noAbsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.noAbsSwitch.getTabAt(0).select();

        binding.noSrsSwitch.addTab(binding.noSrsSwitch.newTab().setText("Good"));
        binding.noSrsSwitch.addTab(binding.noSrsSwitch.newTab().setText("Bad"));
        binding.noSrsSwitch.addTab(binding.noSrsSwitch.newTab().setText("N/A"));
        binding.noSrsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.noSrsSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.noSrsSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.noSrsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.noSrsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.noSrsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.noSrsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.noSrsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.noSrsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //  binding.noSrsSwitch.getTabAt(0).select();

        binding.mechDifferentialOperationSwitch.addTab(binding.mechDifferentialOperationSwitch.newTab().setText("Good"));
        binding.mechDifferentialOperationSwitch.addTab(binding.mechDifferentialOperationSwitch.newTab().setText("Bad"));
        binding.mechDifferentialOperationSwitch.addTab(binding.mechDifferentialOperationSwitch.newTab().setText("N/A"));
        binding.mechDifferentialOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.mechDifferentialOperationSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.mechDifferentialOperationSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.mechDifferentialOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.mechDifferentialOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.mechDifferentialOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.mechDifferentialOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.mechDifferentialOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.mechDifferentialOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.mechDifferentialOperationSwitch.getTabAt(0).select();

        /* for load temp data*/
        binding.engineUpperEndSwitch.selectTab(binding.engineUpperEndSwitch.getTabAt(engineUpperEnd_));
        binding.engineBottomEndSwitch.selectTab(binding.engineBottomEndSwitch.getTabAt(engineBottomEnd_));
        binding.catalyticConverterPresentSwitch.selectTab(binding.catalyticConverterPresentSwitch.getTabAt(catalyticConverter_));
        binding.heaterRunsHotSwitch.selectTab(binding.heaterRunsHotSwitch.getTabAt(heaterRunsHot_));
        binding.acRunsHotSwitch.selectTab(binding.acRunsHotSwitch.getTabAt(acRunsHot_));
        binding.engineLightStartupSwitch.selectTab(binding.engineLightStartupSwitch.getTabAt(engineLightStartUp_));
        binding.noAbsSwitch.selectTab(binding.noAbsSwitch.getTabAt(noAds_));
        binding.noSrsSwitch.selectTab(binding.noSrsSwitch.getTabAt(noSRS_));
        binding.mechDifferentialOperationSwitch.selectTab(binding.mechDifferentialOperationSwitch.getTabAt(differentialOperation_));

        if (uploadVideoStartCarEncode_ != null) {
            binding.uploadVideoStartCarBtn.setBackground(uploadVideoStartCarEncode_);
        }
        if (uploadVideoStandEncode_ != null) {
            binding.uploadVideoStandFrontBtn.setBackground(uploadVideoStandEncode_);
        }
        if (uploadVideoExactionEncode_ != null) {
            binding.uploadVideoExactionSmokeBtn.setBackground(uploadVideoExactionEncode_);
        }
        /*end */

        if (!isCalledMechanical) {
            inspectionRequestViewModel.getInspectionMechanicalResponse().observe(requireActivity(), finalResult -> {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    binding.progressBarInspectionMechanical.setVisibility(View.GONE);
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    binding.progressBarInspectionMechanical.setVisibility(View.GONE);
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof InspectionMechanicalResponse) {
                        InspectionMechanicalResponse inspectionMechanicalResponse = (InspectionMechanicalResponse) success1;
                        boolean success = inspectionMechanicalResponse.isSuccess();
                        binding.progressBarInspectionMechanical.setVisibility(View.GONE);
                        if (success) {
                            //   Toast.makeText(requireActivity(), inspectionMechanicalResponse.getMessage(), Toast.LENGTH_LONG).show();
                            try {
                                navController.navigate(R.id.mechanicalToTiresWheels);
                                isCalledMechanical = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        binding.progressBarInspectionMechanical.setVisibility(View.GONE);
                    }
                } else {
                    binding.progressBarInspectionMechanical.setVisibility(View.GONE);
                }
            });
        }

        inspectionRequestViewModel.getInspectionMechanicalVideoResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarInspectionMechanical.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarInspectionMechanical.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof InspectionMechanicalVideoResponse) {
                    InspectionMechanicalVideoResponse inspectionMechanicalVideoResponse = (InspectionMechanicalVideoResponse) success1;
                    boolean success = inspectionMechanicalVideoResponse.isSuccess();
                    binding.progressBarInspectionMechanical.setVisibility(View.GONE);
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarInspectionMechanical.setVisibility(View.GONE);
                }
            }
        });

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

    ActivityResultLauncher<Intent> uploadVideoStartCarActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedVideoUri = data.getData();
                            String fileName = "";
                            String fileSize = "";
                            uploadVideoStartCarEncode = null;
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
                            String filePath1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            Log.e(TAG, "onActivityResult: " + filePath1);
                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath1, MediaStore.Video.Thumbnails.MINI_KIND);
                            // Setting the thumbnail of the video in to the image view
                            BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                            binding.uploadVideoStartCarBtn.setBackground(bitmapD);
                            uploadVideoStartCarEncode_ = bitmapD;
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
                            uploadVideoStartCarEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);

                            if (uploadVideoStartCarEncode != null) {
                                InspectionMechanicalVideoRequest mechanicalVideoRequest = new InspectionMechanicalVideoRequest();
                                mechanicalVideoRequest.setImageType(Mechanical_Images);
                                mechanicalVideoRequest.setIamgeType("others");
                                mechanicalVideoRequest.setCarId(carId + "");
                                mechanicalVideoRequest.setOthers("1");

                                ArrayList<InspectionMechanicalVideoData> dataArrayList = new ArrayList<>();
                                InspectionMechanicalVideoData mechanicalVideoData = new InspectionMechanicalVideoData();
                                mechanicalVideoData.setType(Type);
                                mechanicalVideoData.setName(fileName);
                                mechanicalVideoData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));
                                mechanicalVideoData.setBase64(Base64_Video_Prefix + uploadVideoStartCarEncode);
                                dataArrayList.add(mechanicalVideoData);

                                mechanicalVideoRequest.setImage(dataArrayList);
                                mechanicalVideoRequest.setPage("5");

                                inspectionRequestViewModel.inspectionMechanicalVideo(mechanicalVideoRequest);
                                binding.progressBarInspectionMechanical.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> uploadVideoStartCarActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedVideoUri = data.getData();
                            String fileName = "";
                            String fileSize = "";
                            uploadVideoStartCarEncode = null;
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
                            String filePath1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
                            Log.e(TAG, "onActivityResult: " + filePath1);
                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath1, MediaStore.Video.Thumbnails.MINI_KIND);
                            // Setting the thumbnail of the video in to the image view
                            BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                            binding.uploadVideoStartCarBtn.setBackground(bitmapD);
                            uploadVideoStartCarEncode_ = bitmapD;
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
                            uploadVideoStartCarEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);

                            if (uploadVideoStartCarEncode != null) {
                                InspectionMechanicalVideoRequest mechanicalVideoRequest = new InspectionMechanicalVideoRequest();
                                mechanicalVideoRequest.setImageType(Mechanical_Images);
                                mechanicalVideoRequest.setIamgeType("others");
                                mechanicalVideoRequest.setCarId(carId + "");
                                mechanicalVideoRequest.setOthers("1");

                                ArrayList<InspectionMechanicalVideoData> dataArrayList = new ArrayList<>();
                                InspectionMechanicalVideoData mechanicalVideoData = new InspectionMechanicalVideoData();
                                mechanicalVideoData.setType(Type);
                                mechanicalVideoData.setName(fileName);
                                mechanicalVideoData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));
                                mechanicalVideoData.setBase64(Base64_Video_Prefix + uploadVideoStartCarEncode);
                                dataArrayList.add(mechanicalVideoData);

                                mechanicalVideoRequest.setImage(dataArrayList);
                                mechanicalVideoRequest.setPage("5");

                                inspectionRequestViewModel.inspectionMechanicalVideo(mechanicalVideoRequest);
                                binding.progressBarInspectionMechanical.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });


    ActivityResultLauncher<Intent> uploadVideoStandActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedVideoUri = data.getData();
                            String fileName = "";
                            String fileSize = "";
                            uploadVideoStandEncode = null;
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
                            String filePath1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath1, MediaStore.Video.Thumbnails.MINI_KIND);
                            // Setting the thumbnail of the video in to the image view
                            BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                            binding.uploadVideoStandFrontBtn.setBackground(bitmapD);
                            uploadVideoStandEncode_ = bitmapD;
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
                            uploadVideoStandEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                            ;
                            if (uploadVideoStandEncode != null) {
                                InspectionMechanicalVideoRequest mechanicalVideoRequest = new InspectionMechanicalVideoRequest();
                                mechanicalVideoRequest.setImageType(Mechanical_Images);
                                mechanicalVideoRequest.setIamgeType("others");
                                mechanicalVideoRequest.setCarId(carId + "");
                                mechanicalVideoRequest.setOthers("1");

                                ArrayList<InspectionMechanicalVideoData> dataArrayList = new ArrayList<>();
                                InspectionMechanicalVideoData mechanicalVideoData = new InspectionMechanicalVideoData();
                                mechanicalVideoData.setType(Type);
                                mechanicalVideoData.setName("");
                                mechanicalVideoData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));
                                mechanicalVideoData.setBase64(Base64_Video_Prefix + uploadVideoStandEncode);
                                dataArrayList.add(mechanicalVideoData);

                                mechanicalVideoRequest.setImage(dataArrayList);
                                mechanicalVideoRequest.setPage("5");

                                inspectionRequestViewModel.inspectionMechanicalVideo(mechanicalVideoRequest);
                                binding.progressBarInspectionMechanical.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> uploadVideoStandActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            uploadVideoStandEncode = null;
                            Uri selectedVideoUri = data.getData();
                            String fileName = "";
                            String fileSize = "";
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
                            String filePath1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath1, MediaStore.Video.Thumbnails.MINI_KIND);
                            // Setting the thumbnail of the video in to the image view
                            BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                            binding.uploadVideoStandFrontBtn.setBackground(bitmapD);
                            uploadVideoStandEncode_ = bitmapD;
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
                            uploadVideoStandEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                            ;
                            if (uploadVideoStandEncode != null) {
                                InspectionMechanicalVideoRequest mechanicalVideoRequest = new InspectionMechanicalVideoRequest();
                                mechanicalVideoRequest.setImageType(Mechanical_Images);
                                mechanicalVideoRequest.setIamgeType("others");
                                mechanicalVideoRequest.setCarId(carId + "");
                                mechanicalVideoRequest.setOthers("1");

                                ArrayList<InspectionMechanicalVideoData> dataArrayList = new ArrayList<>();
                                InspectionMechanicalVideoData mechanicalVideoData = new InspectionMechanicalVideoData();
                                mechanicalVideoData.setType(Type);
                                mechanicalVideoData.setName("");
                                mechanicalVideoData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));
                                mechanicalVideoData.setBase64(Base64_Video_Prefix + uploadVideoStandEncode);
                                dataArrayList.add(mechanicalVideoData);

                                mechanicalVideoRequest.setImage(dataArrayList);
                                mechanicalVideoRequest.setPage("5");

                                inspectionRequestViewModel.inspectionMechanicalVideo(mechanicalVideoRequest);
                                binding.progressBarInspectionMechanical.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> uploadVideoExactionActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedVideoUri = data.getData();
                            String fileName = "";
                            String fileSize = "";
                            uploadVideoExactionEncode = null;
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
                            String filePath1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath1, MediaStore.Video.Thumbnails.MINI_KIND);
                            // Setting the thumbnail of the video in to the image view
                            BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                            binding.uploadVideoExactionSmokeBtn.setBackground(bitmapD);
                            uploadVideoExactionEncode_ = bitmapD;
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
                            uploadVideoExactionEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);

                            if (uploadVideoExactionEncode != null) {
                                InspectionMechanicalVideoRequest mechanicalVideoRequest = new InspectionMechanicalVideoRequest();
                                mechanicalVideoRequest.setImageType(Mechanical_Images);
                                mechanicalVideoRequest.setIamgeType("others");
                                mechanicalVideoRequest.setCarId(carId + "");
                                mechanicalVideoRequest.setOthers("1");

                                ArrayList<InspectionMechanicalVideoData> dataArrayList = new ArrayList<>();
                                InspectionMechanicalVideoData mechanicalVideoData = new InspectionMechanicalVideoData();
                                mechanicalVideoData.setType(Type);
                                mechanicalVideoData.setName(fileName);
                                mechanicalVideoData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));
                                mechanicalVideoData.setBase64(Base64_Video_Prefix + uploadVideoExactionEncode);
                                dataArrayList.add(mechanicalVideoData);

                                mechanicalVideoRequest.setImage(dataArrayList);
                                mechanicalVideoRequest.setPage("5");

                                inspectionRequestViewModel.inspectionMechanicalVideo(mechanicalVideoRequest);
                                binding.progressBarInspectionMechanical.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> uploadVideoExactionActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if (data != null) {
                            Uri selectedVideoUri = data.getData();
                            uploadVideoExactionEncode = null;
                            String fileName = "";
                            String fileSize = "";
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
                            String filePath1 = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));

                            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath1, MediaStore.Video.Thumbnails.MINI_KIND);
                            // Setting the thumbnail of the video in to the image view
                            BitmapDrawable bitmapD = new BitmapDrawable(thumbnail);
                            binding.uploadVideoExactionSmokeBtn.setBackground(bitmapD);
                            uploadVideoExactionEncode_ = bitmapD;
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
                            uploadVideoExactionEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);

                            if (uploadVideoExactionEncode != null) {
                                InspectionMechanicalVideoRequest mechanicalVideoRequest = new InspectionMechanicalVideoRequest();
                                mechanicalVideoRequest.setImageType(Mechanical_Images);
                                mechanicalVideoRequest.setIamgeType("others");
                                mechanicalVideoRequest.setCarId(carId + "");
                                mechanicalVideoRequest.setOthers("1");

                                ArrayList<InspectionMechanicalVideoData> dataArrayList = new ArrayList<>();
                                InspectionMechanicalVideoData mechanicalVideoData = new InspectionMechanicalVideoData();
                                mechanicalVideoData.setType(Type);
                                mechanicalVideoData.setName(fileName);
                                mechanicalVideoData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));
                                mechanicalVideoData.setBase64(Base64_Video_Prefix + uploadVideoExactionEncode);
                                dataArrayList.add(mechanicalVideoData);

                                mechanicalVideoRequest.setImage(dataArrayList);
                                mechanicalVideoRequest.setPage("5");

                                inspectionRequestViewModel.inspectionMechanicalVideo(mechanicalVideoRequest);
                                binding.progressBarInspectionMechanical.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> addImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        Uri imageUri = null;
                        Uri selectedImage = null;
                        try {
                            if (data != null) {
                                Uri selectedUri = data.getData();
                                try {
                                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                                    imageUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                    Log.e(TAG, "onActivityResult: " + imageUri);
                                    if (imageUri != null) {
                                        if (inspectionMechanicalAdapter.isImageFile(imageUri)) {
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
                                addMultipleImagesEncode = null;
                                mechanical_uriArrayList.add(selectedImage);
                                mechanical_picturePathsList.clear();
                                mechanical_picturePathsList.addAll(mechanical_uriArrayList);
                                inspectionMechanicalAdapter.updateList(mechanical_picturePathsList);

                                if (inspectionMechanicalAdapter.isImageFile(selectedImage)) {
                                    Cursor returnCursor = requireContext().getContentResolver().query(selectedImage, null, null, null, null);
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

                                    String[] filePath = {MediaStore.Images.Media.DATA};
                                    Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                                    c.moveToFirst();
                                    int columnIndex = c.getColumnIndex(filePath[0]);
                                    imagePath = c.getString(columnIndex);
                                    c.close();
                                    //    for (Uri picturePath : uriArrayList) {
                                    Bitmap bitmap = (BitmapFactory.decodeFile(imagePath));
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                                    addMultipleImagesEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    //}
                                }
                                if (inspectionMechanicalAdapter.isVideoFile(selectedImage)) {
                                    Cursor returnCursor = requireContext().getContentResolver().query(selectedImage, null, null, null, null);
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
                                    addMultipleImagesEncode = Base64.encodeToString(byteBuffer.toByteArray(), Base64.DEFAULT);
                                }

                                if (addMultipleImagesEncode != null) {
                                    InspectionMechanicalVideoRequest mechanicalVideoRequest = new InspectionMechanicalVideoRequest();
                                    mechanicalVideoRequest.setImageType(Mechanical_Images);
                                    mechanicalVideoRequest.setIamgeType("others");
                                    mechanicalVideoRequest.setCarId(carId + "");
                                    mechanicalVideoRequest.setOthers("1");

                                    ArrayList<InspectionMechanicalVideoData> dataArrayList = new ArrayList<>();
                                    InspectionMechanicalVideoData mechanicalVideoData = new InspectionMechanicalVideoData();

                                    if (inspectionMechanicalAdapter.isVideoFile(selectedImage))
                                        mechanicalVideoData.setType(Type);
                                    else
                                        mechanicalVideoData.setType("image/jpg");

                                    mechanicalVideoData.setBase64(Base64_Video_Prefix + addMultipleImagesEncode);
                                    dataArrayList.add(mechanicalVideoData);

                                    mechanicalVideoRequest.setImage(dataArrayList);
                                    mechanicalVideoRequest.setPage("5");
                                    inspectionRequestViewModel.inspectionMechanicalVideo(mechanicalVideoRequest);
                                    binding.progressBarInspectionMechanical.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            binding.progressBarInspectionMechanical.setVisibility(View.GONE);
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
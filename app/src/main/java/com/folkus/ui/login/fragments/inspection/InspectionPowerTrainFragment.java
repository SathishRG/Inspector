package com.folkus.ui.login.fragments.inspection;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.generalInfo;
import static com.folkus.ui.login.InspectionRequestViewModel.powerTrainFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.powerTrain_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.powerTrain_uriArrayList;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsFrontCarpet;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.engineLower;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.automaticTransmission_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.differentialOperation_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.dsFrontDoor_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.engineBottomNoise_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.powerComments_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.transferCaseOperation_;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.folkus.BuildConfig;
import com.folkus.R;
import com.folkus.data.remote.request.AddExteriorImage;
import com.folkus.data.remote.request.AddExteriorImagesRequest;
import com.folkus.data.remote.request.InspectionPowerTrainRequest;
import com.folkus.data.remote.request.PowerTrainAddImageRequest;
import com.folkus.data.remote.request.PowerTrainAddImagesData;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.InspectionPowerTrainTabResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.PowerTrainAddImageResponse;
import com.folkus.databinding.FragmentInspectionPowerTrainBinding;
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

public class InspectionPowerTrainFragment extends Fragment {
    private static final String TAG = "InspectionPowerTrainFragment";
    private FragmentInspectionPowerTrainBinding binding;
    private PowerTrainImageAdapter powerTrainImageAdapter;
    private InspectionRequestViewModel inspectionRequestViewModel;

    private String engineBottomNoise = "";
    private String automaticTransmission = "";
    private String transferCaseOperation = "";
    private String dsFrontDoor = "";
    private String differentialOperation = "";
    private String comments = null;
    String imagePath;
    private String fileName = "";
    private String fileSize = "";


    private final String Image_Type = "Covered Power Train Images";
    private final String Type = "image/jpg";
    private final String Iamge_Type = "others";
    private final String Base64_Prefix = "data:image/jpg;base64,";

    private String addMultipleImage = null;
    ArrayList<GeneralInfoData> generalInfoData = new ArrayList<>();
    private int carId;
    private int sellerDealerId;
    private int inspectorId;
    static boolean isCalledPowerTrain = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInspectionPowerTrainBinding.inflate(inflater, container, false);
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
        if (powerTrain_uriArrayList != null) {
            if (!powerTrainFirstTimeCalled) {
                powerTrain_uriArrayList.add(Uri.parse(""));
                powerTrainFirstTimeCalled = true;
            }
        }

        if (powerTrain_picturePathsList != null) {
            powerTrain_picturePathsList.clear();
            powerTrain_picturePathsList.addAll(powerTrain_uriArrayList);
        }

        powerTrainImageAdapter = new PowerTrainImageAdapter(requireActivity(), powerTrain_picturePathsList, InspectionPowerTrainFragment.this);
        binding.addMultipleImageVideoRecylerView.setAdapter(powerTrainImageAdapter);

        if (powerTrain_picturePathsList != null) {
            powerTrainImageAdapter.updateList(powerTrain_picturePathsList);
        }
        binding.powerTrainContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comments = binding.comments.getText().toString().trim();
                engineBottomNoise = currentTabName(binding.engineBottomEndNoiseSwitch.getSelectedTabPosition());
                automaticTransmission = currentTabName(binding.automaticTransmissionOperationSwitch.getSelectedTabPosition());
                transferCaseOperation = currentTabName(binding.transferCaseOperationSwitch.getSelectedTabPosition());
                dsFrontDoor = currentTabName(binding.dsFrontDoorSwitch.getSelectedTabPosition());
                differentialOperation = currentTabName(binding.differentialOperationSwitch.getSelectedTabPosition());
                Log.d(TAG, "onClick: " + engineBottomNoise + "\n" + automaticTransmission + "\n" + transferCaseOperation + "\n" + dsFrontDoor + "\n" + differentialOperation + "\n" + comments);
                engineBottomNoise_ = binding.engineBottomEndNoiseSwitch.getSelectedTabPosition();
                automaticTransmission_ = binding.automaticTransmissionOperationSwitch.getSelectedTabPosition();
                transferCaseOperation_ = binding.transferCaseOperationSwitch.getSelectedTabPosition();
                dsFrontDoor_ = binding.dsFrontDoorSwitch.getSelectedTabPosition();
                differentialOperation_ = binding.differentialOperationSwitch.getSelectedTabPosition();
                powerComments_ = comments;
                try {
                    InspectionPowerTrainRequest inspectionPowerTrainRequest = new InspectionPowerTrainRequest();
                    inspectionPowerTrainRequest.setCarId(carId + "");
                    inspectionPowerTrainRequest.setSellerDealerId(sellerDealerId + "");
                    inspectionPowerTrainRequest.setInspectorId(inspectorId + "");
                    inspectionPowerTrainRequest.setNoise(engineBottomNoise);
                    inspectionPowerTrainRequest.setAutoTransmission(automaticTransmission);
                    inspectionPowerTrainRequest.setTransferCase(transferCaseOperation);
                    inspectionPowerTrainRequest.setFrontDoor(dsFrontDoor);
                    inspectionPowerTrainRequest.setDifferential(differentialOperation);
                    inspectionPowerTrainRequest.setComments(comments);
                    isCalledPowerTrain = false;
                    inspectionRequestViewModel.inspectionPowerTrainTab(inspectionPowerTrainRequest);
                    binding.progressBarPowerTrain.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.engineBottomEndNoiseSwitch.addTab(binding.engineBottomEndNoiseSwitch.newTab().setText("Good"));
        binding.engineBottomEndNoiseSwitch.addTab(binding.engineBottomEndNoiseSwitch.newTab().setText("Bad"));
        binding.engineBottomEndNoiseSwitch.addTab(binding.engineBottomEndNoiseSwitch.newTab().setText("N/A"));
        binding.engineBottomEndNoiseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.engineBottomEndNoiseSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.engineBottomEndNoiseSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.engineBottomEndNoiseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.engineBottomEndNoiseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.engineBottomEndNoiseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.engineBottomEndNoiseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.engineBottomEndNoiseSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.engineBottomEndNoiseSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.engineBottomEndNoiseSwitch.getTabAt(0).select();

        binding.automaticTransmissionOperationSwitch.addTab(binding.automaticTransmissionOperationSwitch.newTab().setText("Good"));
        binding.automaticTransmissionOperationSwitch.addTab(binding.automaticTransmissionOperationSwitch.newTab().setText("Bad"));
        binding.automaticTransmissionOperationSwitch.addTab(binding.automaticTransmissionOperationSwitch.newTab().setText("N/A"));
        binding.automaticTransmissionOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.automaticTransmissionOperationSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.automaticTransmissionOperationSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.automaticTransmissionOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.automaticTransmissionOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.automaticTransmissionOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.automaticTransmissionOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.automaticTransmissionOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.automaticTransmissionOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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

        binding.transferCaseOperationSwitch.addTab(binding.transferCaseOperationSwitch.newTab().setText("Good"));
        binding.transferCaseOperationSwitch.addTab(binding.transferCaseOperationSwitch.newTab().setText("Bad"));
        binding.transferCaseOperationSwitch.addTab(binding.transferCaseOperationSwitch.newTab().setText("N/A"));
        binding.transferCaseOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.transferCaseOperationSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.transferCaseOperationSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.transferCaseOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.transferCaseOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.transferCaseOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.transferCaseOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.transferCaseOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.transferCaseOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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

        binding.dsFrontDoorSwitch.addTab(binding.dsFrontDoorSwitch.newTab().setText("Good"));
        binding.dsFrontDoorSwitch.addTab(binding.dsFrontDoorSwitch.newTab().setText("Bad"));
        binding.dsFrontDoorSwitch.addTab(binding.dsFrontDoorSwitch.newTab().setText("N/A"));
        binding.dsFrontDoorSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.dsFrontDoorSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.dsFrontDoorSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.dsFrontDoorSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.dsFrontDoorSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.dsFrontDoorSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.dsFrontDoorSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.dsFrontDoorSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.dsFrontDoorSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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

        binding.differentialOperationSwitch.addTab(binding.differentialOperationSwitch.newTab().setText("Good"));
        binding.differentialOperationSwitch.addTab(binding.differentialOperationSwitch.newTab().setText("Bad"));
        binding.differentialOperationSwitch.addTab(binding.differentialOperationSwitch.newTab().setText("N/A"));
        binding.differentialOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.differentialOperationSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.differentialOperationSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.differentialOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.differentialOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.differentialOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.differentialOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.differentialOperationSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.differentialOperationSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //binding.differentialOperationSwitch.getTabAt(0).select();

        Log.e(TAG, "subscribeUI: " + "" + engineBottomNoise_ + "/" + automaticTransmission_ + "/" + transferCaseOperation_ + "/" + dsFrontDoor_ + "/" + differentialOperation_);
        binding.engineBottomEndNoiseSwitch.selectTab(binding.engineBottomEndNoiseSwitch.getTabAt(engineBottomNoise_));
        binding.automaticTransmissionOperationSwitch.selectTab(binding.engineBottomEndNoiseSwitch.getTabAt(automaticTransmission_));
        binding.transferCaseOperationSwitch.selectTab(binding.transferCaseOperationSwitch.getTabAt(transferCaseOperation_));
        binding.dsFrontDoorSwitch.selectTab(binding.dsFrontDoorSwitch.getTabAt(dsFrontDoor_));
        binding.differentialOperationSwitch.selectTab(binding.differentialOperationSwitch.getTabAt(differentialOperation_));

        if (powerComments_ != null) {
            binding.comments.setText(powerComments_);
        }

        if (!isCalledPowerTrain) {
            inspectionRequestViewModel.getInspectionPowerTrainTabResponse().observe(requireActivity(), finalResult -> {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    binding.progressBarPowerTrain.setVisibility(View.GONE);
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    Toast.makeText(requireActivity(), error, Toast.LENGTH_LONG).show();
                    binding.progressBarPowerTrain.setVisibility(View.GONE);
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof InspectionPowerTrainTabResponse) {
                        InspectionPowerTrainTabResponse powerTrainTabResponse = (InspectionPowerTrainTabResponse) success1;
                        boolean success = powerTrainTabResponse.isSuccess();
                        binding.progressBarPowerTrain.setVisibility(View.GONE);
                        if (success) {
                            try {
                                //Toast.makeText(requireActivity(), powerTrainTabResponse.getMessage(), Toast.LENGTH_LONG).show();
                                try {
                                    NavDirections navDirections = InspectionPowerTrainFragmentDirections.powerTrainToMechanical();
                                    navController.navigate(navDirections);
                                    isCalledPowerTrain = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                        binding.progressBarPowerTrain.setVisibility(View.GONE);
                    }
                }
            });
        }

        inspectionRequestViewModel.getPowerTrainAddMultipleImageResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarPowerTrain.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarPowerTrain.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof PowerTrainAddImageResponse) {
                    PowerTrainAddImageResponse powerTrainAddImageResponse = (PowerTrainAddImageResponse) success1;
                    boolean success = powerTrainAddImageResponse.isSuccess();
                    if (success) {
                        binding.progressBarPowerTrain.setVisibility(View.GONE);
                    } else {
                        binding.progressBarPowerTrain.setVisibility(View.GONE);
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarPowerTrain.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
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

    private String formatVideoFileName() {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String videoFileName = "InspectionCarVideo_" + timeStamp + ".mp4";
        return videoFileName;
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, formatImageFileName(), null);
        return Uri.parse(path);
    }

    public static String formatImageFileName() {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_.jpg";
        return imageFileName;
    }

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

    ActivityResultLauncher<Intent> addImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        addMultipleImage = null;
                        Uri imageUri = null;
                        Uri selectedImage = null;
                        try {
                            if (data != null) {
                                Uri selectedUri = data.getData();
                                try {
                                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                                    imageUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                    Log.e(TAG, "onActivityResult: "+ imageUri);
                                    if (imageUri != null) {
                                        if (powerTrainImageAdapter.isImageFile(imageUri)) {
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
                                powerTrain_uriArrayList.add(selectedImage);
                                powerTrain_picturePathsList.clear();
                                powerTrain_picturePathsList.addAll(powerTrain_uriArrayList);
                                powerTrainImageAdapter.updateList(powerTrain_picturePathsList);

                                if (powerTrainImageAdapter.isImageFile(selectedImage)) {
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
                                    addMultipleImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                    //}
                                } else {
                                    Cursor returnCursor = requireContext().getContentResolver().query(selectedImage, null, null, null, null);
                                    try {
                                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                        int size = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                                        returnCursor.moveToFirst();
                                        fileName = returnCursor.getString(nameIndex);
                                        fileSize = returnCursor.getString(size);
                                        Log.e("path2", fileName + " /" + bytesIntoHumanReadable(Long.parseLong(fileSize)));
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
                                    PowerTrainAddImageRequest powerTrainAddImageRequest = new PowerTrainAddImageRequest();
                                    powerTrainAddImageRequest.setImageType(Image_Type);
                                    powerTrainAddImageRequest.setIamgeType(Iamge_Type);
                                    powerTrainAddImageRequest.setCarId(carId + "");
                                    powerTrainAddImageRequest.setOthers(2);
                                    powerTrainAddImageRequest.setPage("4");

                                    ArrayList<PowerTrainAddImagesData> powerTrainAddImagesDataArrayList = new ArrayList<>();
                                    PowerTrainAddImagesData powerTrainAddImagesData = new PowerTrainAddImagesData();
                                    powerTrainAddImagesData.setName(fileName);

                                    if (powerTrainImageAdapter.isImageFile(selectedImage))
                                        powerTrainAddImagesData.setType(Type);
                                    else
                                        powerTrainAddImagesData.setType("video/mp4");

                                    powerTrainAddImagesData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));

                                    powerTrainAddImagesData.setBase64(Base64_Prefix + addMultipleImage);
                                    powerTrainAddImagesDataArrayList.add(powerTrainAddImagesData);

                                    powerTrainAddImageRequest.setImage(powerTrainAddImagesDataArrayList);
                                    inspectionRequestViewModel.powerTrainAddMultipleImages(powerTrainAddImageRequest);
                                    binding.progressBarPowerTrain.setVisibility(View.VISIBLE);
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            binding.progressBarPowerTrain.setVisibility(View.GONE);
                        }
                    }
                }
            });

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
}
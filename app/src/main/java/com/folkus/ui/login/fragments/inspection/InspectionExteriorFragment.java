package com.folkus.ui.login.fragments.inspection;


import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.exteriorFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.exterior_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.exterior_uriArrayList;
import static com.folkus.ui.login.InspectionRequestViewModel.generalInfo;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.exteriorComments_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noColourFade_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noExteriorScratches_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noGlassDamaged_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noSideMirrorDamage_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noVisibleRust_;
import static com.folkus.ui.login.fragments.inspection.InspectionPowerTrainFragment.getImageUri;
import static com.folkus.ui.login.fragments.inspection.TiresWheelData.treadDepth_;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.folkus.data.remote.request.ExteriorAddImageData;
import com.folkus.data.remote.request.ExteriorAddImageRequest;
import com.folkus.data.remote.request.ExteriorTabRequest;
import com.folkus.data.remote.response.ExteriorAddImageResponse;
import com.folkus.data.remote.response.ExteriorTabResponse;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.FragmentInspectionExteriorBinding;
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

public class InspectionExteriorFragment extends Fragment {
    private static final String TAG = "InspectionExteriorFragment";
    private FragmentInspectionExteriorBinding binding;
    private ExteriorAdapter exteriorAdapter;

    private String noVisibleRust = "";
    private String noColourFade = "";
    private String noGlassDamaged = "";
    private String noExteriorScratches = "";
    private String noSideMirrorDamage = "";
    private String comments = null;

    String imagePath;
    String addMultipleImage = null;
    int carId;
    private int sellerDealerId;
    private int inspectorId;

    private final String Image_Type = "Exterior";
    private final String Type = "image/jpg";
    private final String Iamge_Type = "others";
    private final String Base64_Prefix = "data:image/jpg;base64,";
    InspectionRequestViewModel inspectionRequestViewModel;
    ArrayList<GeneralInfoData> generalInfoData = new ArrayList<>();
    String fileName = "";
    String fileSize = "";
    static boolean isCalledExterior = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInspectionExteriorBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
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
        if (exterior_uriArrayList != null) {
            if (!exteriorFirstTimeCalled) {
                exterior_uriArrayList.add(Uri.parse(""));
                exteriorFirstTimeCalled = true;
            }
        }

        if (exterior_picturePathsList != null) {
            exterior_picturePathsList.clear();
            exterior_picturePathsList.addAll(exterior_uriArrayList);
        }
        exteriorAdapter = new ExteriorAdapter(requireActivity(), exterior_picturePathsList, InspectionExteriorFragment.this);
        binding.addMultipleImageVideoRecylerView.setAdapter(exteriorAdapter);
        binding.addMultipleImageVideoRecylerView.hasFixedSize();

        if (exterior_picturePathsList != null) {
            exteriorAdapter.updateList(exterior_picturePathsList);
        }

        binding.exteriorContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noVisibleRust = currentTabName(binding.noVisibleRustSwitch.getSelectedTabPosition());
                noColourFade = currentTabName(binding.noColourFadeSwitch.getSelectedTabPosition());
                noGlassDamaged = currentTabName(binding.noGlassDamagedSwitch.getSelectedTabPosition());
                noExteriorScratches = currentTabName(binding.noExteriorScratchesSwitch.getSelectedTabPosition());
                noSideMirrorDamage = currentTabName(binding.noSideMirrorDamageSwitch.getSelectedTabPosition());
                comments = binding.comments.getText().toString().trim();
                noVisibleRust_ = binding.noVisibleRustSwitch.getSelectedTabPosition();
                noColourFade_ = binding.noColourFadeSwitch.getSelectedTabPosition();
                noGlassDamaged_ = binding.noGlassDamagedSwitch.getSelectedTabPosition();
                noExteriorScratches_ = binding.noExteriorScratchesSwitch.getSelectedTabPosition();
                noSideMirrorDamage_ = binding.noSideMirrorDamageSwitch.getSelectedTabPosition();
                exteriorComments_ = comments;
                Log.d("submit", noVisibleRust + "\n" + noColourFade + "\n" + noGlassDamaged + "\n" + noExteriorScratches + "\n" + noSideMirrorDamage + "\n" + comments);

                try {
                    ExteriorTabRequest exteriorTabRequest = new ExteriorTabRequest();
                    exteriorTabRequest.setCarId(carId + "");
                    exteriorTabRequest.setSellerDealerId(sellerDealerId + "");
                    exteriorTabRequest.setInspectorId(inspectorId + "");
                    exteriorTabRequest.setVisibleRust(noVisibleRust);
                    exteriorTabRequest.setColorFade(noColourFade);
                    exteriorTabRequest.setGlassDamage(noGlassDamaged);
                    exteriorTabRequest.setScratches(noExteriorScratches);
                    exteriorTabRequest.setSideMirror(noSideMirrorDamage);
                    exteriorTabRequest.setComments(comments);
                    isCalledExterior = false;
                    inspectionRequestViewModel.exteriorTab(exteriorTabRequest);
                    binding.progressBarInspectionExterior.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

//                Bundle bundle = new Bundle();
//                bundle.putSerializable("interior", generalInfoData);
//                setArguments(bundle);
//                navController.navigate(R.id.exteriorFragment_to_inspectionInteriorFragment, bundle);
            }
        });

        binding.noVisibleRustSwitch.addTab(binding.noVisibleRustSwitch.newTab().setText("Good"));
        binding.noVisibleRustSwitch.addTab(binding.noVisibleRustSwitch.newTab().setText("Bad"));
        binding.noVisibleRustSwitch.addTab(binding.noVisibleRustSwitch.newTab().setText("N/A"));
        binding.noVisibleRustSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.noVisibleRustSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.noVisibleRustSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.noVisibleRustSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.noVisibleRustSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.noVisibleRustSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.noVisibleRustSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.noVisibleRustSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.noVisibleRustSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //   binding.noVisibleRustSwitch.getTabAt(0).select();


        binding.noColourFadeSwitch.addTab(binding.noColourFadeSwitch.newTab().setText("Good"));
        binding.noColourFadeSwitch.addTab(binding.noColourFadeSwitch.newTab().setText("Bad"));
        binding.noColourFadeSwitch.addTab(binding.noColourFadeSwitch.newTab().setText("N/A"));
        binding.noColourFadeSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.noColourFadeSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.noColourFadeSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.noColourFadeSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.noColourFadeSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.noColourFadeSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.noColourFadeSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.noColourFadeSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.noColourFadeSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.noColourFadeSwitch.getTabAt(0).select();


        binding.noGlassDamagedSwitch.addTab(binding.noGlassDamagedSwitch.newTab().setText("Good"));
        binding.noGlassDamagedSwitch.addTab(binding.noGlassDamagedSwitch.newTab().setText("Bad"));
        binding.noGlassDamagedSwitch.addTab(binding.noGlassDamagedSwitch.newTab().setText("N/A"));
        binding.noGlassDamagedSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.noGlassDamagedSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.noGlassDamagedSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.noGlassDamagedSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.noGlassDamagedSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.noGlassDamagedSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.noGlassDamagedSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.noGlassDamagedSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.noGlassDamagedSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //binding.noGlassDamagedSwitch.getTabAt(0).select();

        binding.noExteriorScratchesSwitch.addTab(binding.noExteriorScratchesSwitch.newTab().setText("Good"));
        binding.noExteriorScratchesSwitch.addTab(binding.noExteriorScratchesSwitch.newTab().setText("Bad"));
        binding.noExteriorScratchesSwitch.addTab(binding.noExteriorScratchesSwitch.newTab().setText("N/A"));
        binding.noExteriorScratchesSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.noExteriorScratchesSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.noExteriorScratchesSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.noExteriorScratchesSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.noExteriorScratchesSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.noExteriorScratchesSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.noExteriorScratchesSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.noExteriorScratchesSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.noExteriorScratchesSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //  binding.noExteriorScratchesSwitch.getTabAt(0).select();

        binding.noSideMirrorDamageSwitch.addTab(binding.noSideMirrorDamageSwitch.newTab().setText("Good"));
        binding.noSideMirrorDamageSwitch.addTab(binding.noSideMirrorDamageSwitch.newTab().setText("Bad"));
        binding.noSideMirrorDamageSwitch.addTab(binding.noSideMirrorDamageSwitch.newTab().setText("N/A"));
        binding.noSideMirrorDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.noSideMirrorDamageSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.noSideMirrorDamageSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.noSideMirrorDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.noSideMirrorDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.noSideMirrorDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.noSideMirrorDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.noSideMirrorDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.noSideMirrorDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.noSideMirrorDamageSwitch.getTabAt(0).select();
        if (exteriorComments_ != null) {
            binding.comments.setText(exteriorComments_);
        }
        /* for load temp data*/
        binding.noVisibleRustSwitch.selectTab(binding.noVisibleRustSwitch.getTabAt(noVisibleRust_));
        binding.noColourFadeSwitch.selectTab(binding.noColourFadeSwitch.getTabAt(noColourFade_));
        binding.noGlassDamagedSwitch.selectTab(binding.noGlassDamagedSwitch.getTabAt(noGlassDamaged_));
        binding.noExteriorScratchesSwitch.selectTab(binding.noExteriorScratchesSwitch.getTabAt(noExteriorScratches_));
        binding.noSideMirrorDamageSwitch.selectTab(binding.noSideMirrorDamageSwitch.getTabAt(noSideMirrorDamage_));

        if (!isCalledExterior) {
            inspectionRequestViewModel.getExteriorTabResponse().observe(requireActivity(), finalResult -> {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    binding.progressBarInspectionExterior.setVisibility(View.GONE);
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    binding.progressBarInspectionExterior.setVisibility(View.GONE);
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof ExteriorTabResponse) {
                        ExteriorTabResponse exteriorTabResponse = (ExteriorTabResponse) success1;
                        boolean success = exteriorTabResponse.isSuccess();
                        binding.progressBarInspectionExterior.setVisibility(View.GONE);
                        if (success) {
                            // Toast.makeText(requireActivity(),exteriorTabResponse.getMessage(), Toast.LENGTH_LONG).show();
                            try {
                                navController.navigate(R.id.exteriorFragment_to_inspectionInteriorFragment);
                                isCalledExterior = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                        binding.progressBarInspectionExterior.setVisibility(View.GONE);
                    }
                }
            });
        }
        inspectionRequestViewModel.getExteriorAddImageResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarInspectionExterior.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarInspectionExterior.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof ExteriorAddImageResponse) {
                    ExteriorAddImageResponse exteriorAddImageResponse = (ExteriorAddImageResponse) success1;
                    boolean success = exteriorAddImageResponse.isSuccess();
                    if (success) {
                        binding.progressBarInspectionExterior.setVisibility(View.GONE);
                    } else {
                        binding.progressBarInspectionExterior.setVisibility(View.GONE);
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarInspectionExterior.setVisibility(View.GONE);
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
                        if (data != null) {
                            Uri selectedUri = data.getData();
                            addMultipleImage = null;

                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                imageUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "onActivityResult: " + imageUri);
                                if (imageUri != null) {
                                    if (exteriorAdapter.isImageFile(imageUri)) {
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
                            exterior_uriArrayList.add(selectedImage);
                            exterior_picturePathsList.clear();
                            exterior_picturePathsList.addAll(exterior_uriArrayList);
                            exteriorAdapter.updateList(exterior_picturePathsList); // add this
                            if (exteriorAdapter.isImageFile(selectedImage)) {
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
                                //    for (Uri picturePath : uriArrayList) {
                                Bitmap bitmap = (BitmapFactory.decodeFile(imagePath));
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                addMultipleImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                //}
                            }
                            if (exteriorAdapter.isVideoFile(selectedImage)) {
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
                                ExteriorAddImageRequest interiorAddImageRequest = new ExteriorAddImageRequest();
                                interiorAddImageRequest.setImageType(Image_Type);
                                interiorAddImageRequest.setIamgeType(Iamge_Type);
                                interiorAddImageRequest.setCarId(carId + "");
                                interiorAddImageRequest.setOthers("1");
                                interiorAddImageRequest.setPage("9");

                                ArrayList<ExteriorAddImageData> addImageDataArrayList = new ArrayList<>();
                                ExteriorAddImageData exteriorAddImageData = new ExteriorAddImageData();
                                exteriorAddImageData.setName(fileName);
                                if (exteriorAdapter.isImageFile(selectedImage))
                                    exteriorAddImageData.setType(Type);
                                else
                                    exteriorAddImageData.setType("video/mp4");

                                exteriorAddImageData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));
                                exteriorAddImageData.setBase64(Base64_Prefix + addMultipleImage);
                                addImageDataArrayList.add(exteriorAddImageData);

                                interiorAddImageRequest.setImage(addImageDataArrayList);
                                inspectionRequestViewModel.exteriorAddImageApi(interiorAddImageRequest);
                                binding.progressBarInspectionExterior.setVisibility(View.VISIBLE);
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

}
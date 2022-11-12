package com.folkus.ui.login.fragments.inspection;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.generalInfo;
import static com.folkus.ui.login.InspectionRequestViewModel.interiorFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.interior_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.interior_uriArrayList;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.exteriorComments_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noVisibleRust_;
import static com.folkus.ui.login.fragments.inspection.InspectionPowerTrainFragment.getImageUri;
import static com.folkus.ui.login.fragments.inspection.InteriorData.backSeatCondition_;
import static com.folkus.ui.login.fragments.inspection.InteriorData.frontSeatCondition_;
import static com.folkus.ui.login.fragments.inspection.InteriorData.interiorComments_;
import static com.folkus.ui.login.fragments.inspection.InteriorData.noMajorVisibleDamage_;
import static com.folkus.ui.login.fragments.inspection.InteriorData.noVisibleDamage_;

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
import com.folkus.data.remote.request.InteriorAddImageData;
import com.folkus.data.remote.request.InteriorAddImageRequest;
import com.folkus.data.remote.request.InteriorTabRequest;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.InteriorAddImageResponse;
import com.folkus.data.remote.response.InteriorTabResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.FragmentInspectionInteriorBinding;
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

public class InspectionInteriorFragment extends Fragment {
    private static final String TAG = "InspectionInteriorFragment";
    private FragmentInspectionInteriorBinding binding;
    private InteriorAdapter interiorAdapter;

    private String noVisibleDamage = "";
    private String frontSeatCondition = "";
    private String backSeatCondition = "";
    private String noMajorVisibleDamage = "";
    private String comments = null;
    String fileName = "";
    String fileSize = "";

    String imagePath;
    String addMultipleImage = null;
    int carId;
    private int sellerDealerId;
    private int inspectorId;


    private final String Image_Type = "Interior";
    private final String Type = "image/jpg";
    private final String Iamge_Type = "others";
    private final String Base64_Prefix = "data:image/jpg;base64,";
    InspectionRequestViewModel inspectionRequestViewModel;
    ArrayList<GeneralInfoData> generalInfoData = new ArrayList<>();
    static boolean isCalledInterior = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInspectionInteriorBinding.inflate(inflater, container, false);
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
        if (interior_uriArrayList != null) {
            if (!interiorFirstTimeCalled) {
                interior_uriArrayList.add(Uri.parse(""));
                interiorFirstTimeCalled = true;
            }
        }

        if (interior_picturePathsList != null) {
            interior_picturePathsList.clear();
            interior_picturePathsList.addAll(interior_uriArrayList);
        }
        interiorAdapter = new InteriorAdapter(requireActivity(), interior_picturePathsList, InspectionInteriorFragment.this);
        binding.addMultipleImageVideoRecylerView.setAdapter(interiorAdapter);
        binding.addMultipleImageVideoRecylerView.hasFixedSize();

        if (interior_picturePathsList != null) {
            interiorAdapter.updateList(interior_picturePathsList);
        }

        binding.interiorContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noVisibleDamage = currentTabName(binding.noVisibleDamageSwitch.getSelectedTabPosition());
                frontSeatCondition = currentTabName(binding.frontSeatConditionSwitch.getSelectedTabPosition());
                backSeatCondition = currentTabName(binding.backSeatConditionSwitch.getSelectedTabPosition());
                noMajorVisibleDamage = currentTabName(binding.majorVisibleDamageSwitch.getSelectedTabPosition());
                comments = binding.comments.getText().toString().trim();
                noVisibleDamage_ = binding.noVisibleDamageSwitch.getSelectedTabPosition();
                frontSeatCondition_ = binding.frontSeatConditionSwitch.getSelectedTabPosition();
                backSeatCondition_ = binding.backSeatConditionSwitch.getSelectedTabPosition();
                noMajorVisibleDamage_ = binding.majorVisibleDamageSwitch.getSelectedTabPosition();
                interiorComments_ = comments;
                Log.d("submit", noVisibleDamage + "\n" + frontSeatCondition + "\n" + backSeatCondition + "\n" + noMajorVisibleDamage + "\n" + comments);

                try {
                    InteriorTabRequest interiorTabRequest = new InteriorTabRequest();
                    interiorTabRequest.setCarId(carId + "");
                    interiorTabRequest.setSellerDealerId(sellerDealerId + "");
                    interiorTabRequest.setInspectorId(inspectorId + "");
                    interiorTabRequest.setComments(comments);
                    interiorTabRequest.setVisibleDamage(noVisibleDamage);
                    interiorTabRequest.setFrontSeat(noVisibleDamage);
                    interiorTabRequest.setBackSeat(noVisibleDamage);
                    interiorTabRequest.setMajorDamage(noVisibleDamage);
                    interiorTabRequest.setComments(noVisibleDamage);
                    isCalledInterior = false;
                    inspectionRequestViewModel.interiorTab(interiorTabRequest);
                    binding.progressBarInspectionInterior.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        binding.noVisibleDamageSwitch.addTab(binding.noVisibleDamageSwitch.newTab().setText("Good"));
        binding.noVisibleDamageSwitch.addTab(binding.noVisibleDamageSwitch.newTab().setText("Bad"));
        binding.noVisibleDamageSwitch.addTab(binding.noVisibleDamageSwitch.newTab().setText("N/A"));
        binding.noVisibleDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.noVisibleDamageSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.noVisibleDamageSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.noVisibleDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.noVisibleDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.noVisibleDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.noVisibleDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.noVisibleDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.noVisibleDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //binding.noVisibleDamageSwitch.getTabAt(0).select();


        binding.frontSeatConditionSwitch.addTab(binding.frontSeatConditionSwitch.newTab().setText("Good"));
        binding.frontSeatConditionSwitch.addTab(binding.frontSeatConditionSwitch.newTab().setText("Bad"));
        binding.frontSeatConditionSwitch.addTab(binding.frontSeatConditionSwitch.newTab().setText("N/A"));
        binding.frontSeatConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.frontSeatConditionSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.frontSeatConditionSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.frontSeatConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.frontSeatConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.frontSeatConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.frontSeatConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.frontSeatConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.frontSeatConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.frontSeatConditionSwitch.getTabAt(0).select();


        binding.backSeatConditionSwitch.addTab(binding.backSeatConditionSwitch.newTab().setText("Good"));
        binding.backSeatConditionSwitch.addTab(binding.backSeatConditionSwitch.newTab().setText("Bad"));
        binding.backSeatConditionSwitch.addTab(binding.backSeatConditionSwitch.newTab().setText("N/A"));
        binding.backSeatConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.backSeatConditionSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.backSeatConditionSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.backSeatConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.backSeatConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.backSeatConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.backSeatConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.backSeatConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.backSeatConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //binding.backSeatConditionSwitch.getTabAt(0).select();

        binding.majorVisibleDamageSwitch.addTab(binding.majorVisibleDamageSwitch.newTab().setText("Good"));
        binding.majorVisibleDamageSwitch.addTab(binding.majorVisibleDamageSwitch.newTab().setText("Bad"));
        binding.majorVisibleDamageSwitch.addTab(binding.majorVisibleDamageSwitch.newTab().setText("N/A"));
        binding.majorVisibleDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.majorVisibleDamageSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.majorVisibleDamageSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.majorVisibleDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.majorVisibleDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.majorVisibleDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.majorVisibleDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.majorVisibleDamageSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.majorVisibleDamageSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //binding.majorVisibleDamageSwitch.getTabAt(0).select();

        if (interiorComments_ != null) {
            binding.comments.setText(interiorComments_);
        }
        /* for load temp data*/
        binding.noVisibleDamageSwitch.selectTab(binding.noVisibleDamageSwitch.getTabAt(noVisibleDamage_));
        binding.frontSeatConditionSwitch.selectTab(binding.frontSeatConditionSwitch.getTabAt(frontSeatCondition_));
        binding.backSeatConditionSwitch.selectTab(binding.backSeatConditionSwitch.getTabAt(backSeatCondition_));
        binding.majorVisibleDamageSwitch.selectTab(binding.majorVisibleDamageSwitch.getTabAt(noMajorVisibleDamage_));

        if (!isCalledInterior) {
            inspectionRequestViewModel.getInteriorTabResponse().observe(requireActivity(), finalResult -> {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    binding.progressBarInspectionInterior.setVisibility(View.GONE);
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    binding.progressBarInspectionInterior.setVisibility(View.GONE);
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof InteriorTabResponse) {
                        InteriorTabResponse interiorTabResponse = (InteriorTabResponse) success1;
                        boolean success = interiorTabResponse.isSuccess();
                        if (success) {
                            binding.progressBarInspectionInterior.setVisibility(View.GONE);
                            try {
                                // Toast.makeText(requireActivity(),interiorTabResponse.getMessage(), Toast.LENGTH_LONG).show();
                                try {
                                    navController.navigate(R.id.interiorToTestDrive);
                                    isCalledInterior = true;
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
                        binding.progressBarInspectionInterior.setVisibility(View.GONE);
                    }
                } else {
                    Log.e(TAG, "subscribeUI: failed1");
                }
            });
        }

        inspectionRequestViewModel.getInteriorAddImageResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarInspectionInterior.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarInspectionInterior.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof InteriorAddImageResponse) {
                    InteriorAddImageResponse interiorAddImageResponse = (InteriorAddImageResponse) success1;
                    boolean success = interiorAddImageResponse.isSuccess();
                    if (success) {
                        binding.progressBarInspectionInterior.setVisibility(View.GONE);
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarInspectionInterior.setVisibility(View.GONE);
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
                                    if (interiorAdapter.isImageFile(imageUri)) {
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
                            interior_uriArrayList.add(selectedImage);
                            interior_picturePathsList.clear();
                            interior_picturePathsList.addAll(interior_uriArrayList);
                            interiorAdapter.updateList(interior_picturePathsList); // add this

                            if (interiorAdapter.isImageFile(selectedImage)) {
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

                            if (interiorAdapter.isVideoFile(selectedImage)) {
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
                                InteriorAddImageRequest interiorAddImageRequest = new InteriorAddImageRequest();
                                interiorAddImageRequest.setImageType(Image_Type);
                                interiorAddImageRequest.setIamgeType(Iamge_Type);
                                interiorAddImageRequest.setCarId(carId + "");
                                interiorAddImageRequest.setOthers("1");
                                interiorAddImageRequest.setPage("9");

                                ArrayList<InteriorAddImageData> interiorAddImageRequestArrayList = new ArrayList<>();
                                InteriorAddImageData interiorAddImageData = new InteriorAddImageData();
                                interiorAddImageData.setName(fileName);
                                if (interiorAdapter.isImageFile(selectedImage))
                                    interiorAddImageData.setType(Type);
                                else
                                    interiorAddImageData.setType("video/mp4");

                                interiorAddImageData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));

                                interiorAddImageData.setBase64(Base64_Prefix + addMultipleImage);
                                interiorAddImageRequestArrayList.add(interiorAddImageData);

                                interiorAddImageRequest.setImage(interiorAddImageRequestArrayList);
                                inspectionRequestViewModel.interiorAddImageApi(interiorAddImageRequest);
                                binding.progressBarInspectionInterior.setVisibility(View.VISIBLE);
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
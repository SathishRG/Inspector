package com.folkus.ui.login.fragments.inspection;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.generalInfo;
import static com.folkus.ui.login.InspectionRequestViewModel.powerTrain_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.tiresWheelFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.tiresWheel_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.tiresWheel_uriArrayList;
import static com.folkus.ui.login.fragments.inspection.InspectionPowerTrainFragment.getImageUri;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.engineUpperEnd_;
import static com.folkus.ui.login.fragments.inspection.TiresWheelData.anyScratchesOnWheels_;
import static com.folkus.ui.login.fragments.inspection.TiresWheelData.fourTiresCondition_;
import static com.folkus.ui.login.fragments.inspection.TiresWheelData.tiresWheelComments_;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.folkus.data.remote.request.InspectionTiresWheelImageData;
import com.folkus.data.remote.request.InspectionTiresWheelImageRequest;
import com.folkus.data.remote.request.InspectionTiresWheelsRequest;
import com.folkus.data.remote.request.PowerTrainAddImagesData;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.InspectionTiresWheelImageResponse;
import com.folkus.data.remote.response.InspectionTiresWheelResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.FragmentTiresWheelsBinding;
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

public class InspectionTiresWheelsFragment extends Fragment {
    private static final String TAG = "InspectionTiresWheelsFragment";
    private TiresWheelsAdapter tiresWheelsAdapter;
    private FragmentTiresWheelsBinding binding;
    InspectionRequestViewModel inspectionRequestViewModel;
    ArrayList<GeneralInfoData> generalInfoData = new ArrayList<>();
    int carId;
    private int sellerDealerId;
    private int inspectorId;

    private String treadDepth = "";
    private String fourTiresCondition = "";
    private String anyScratchesOnWheels = "";
    private String comments = null;
    String addMultipleImage = null;
    String imagePath;

    private final String Image_Type = "Test Drive Images";
    private final String Type = "image/jpg";
    private final String Iamge_Type = "others";
    private final String Base64_Prefix = "data:image/jpg;base64,";
    static boolean isCalledTiresWheels = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTiresWheelsBinding.inflate(inflater, container, false);
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
        if (tiresWheel_uriArrayList != null) {
            if (!tiresWheelFirstTimeCalled) {
                tiresWheel_uriArrayList.add(Uri.parse(""));
                tiresWheelFirstTimeCalled = true;
            }
        }

        if (tiresWheel_picturePathsList != null) {
            tiresWheel_picturePathsList.clear();
            tiresWheel_picturePathsList.addAll(tiresWheel_uriArrayList);
        }

        tiresWheelsAdapter = new TiresWheelsAdapter(requireActivity(), tiresWheel_picturePathsList, InspectionTiresWheelsFragment.this);
        binding.addMultipleImageVideoRecylerView.setAdapter(tiresWheelsAdapter);
        binding.addMultipleImageVideoRecylerView.hasFixedSize();

        if (tiresWheel_picturePathsList != null) {
            tiresWheelsAdapter.updateList(tiresWheel_picturePathsList);
        }

        binding.tiresWheelsContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                treadDepth = currentTabName(binding.treadDepthSwitch.getSelectedTabPosition());
                fourTiresCondition = currentTabName(binding.fourTiresConditionSwitch.getSelectedTabPosition());
                anyScratchesOnWheels = currentTabName(binding.anyScratchesWheelsSwitch.getSelectedTabPosition());
                comments = binding.comments.getText().toString().trim();
                treadDepth_ = binding.treadDepthSwitch.getSelectedTabPosition();
                fourTiresCondition_ = binding.fourTiresConditionSwitch.getSelectedTabPosition();
                anyScratchesOnWheels_ = binding.anyScratchesWheelsSwitch.getSelectedTabPosition();
                tiresWheelComments_ = comments;
                Log.d("submit", treadDepth + "\n" + fourTiresCondition + "\n" + anyScratchesOnWheels + "\n" + comments);

                try {
                    InspectionTiresWheelsRequest inspectionTiresWheelsRequest = new InspectionTiresWheelsRequest();
                    inspectionTiresWheelsRequest.setCarId(carId);
                    inspectionTiresWheelsRequest.setSellerDealerId(sellerDealerId);
                    inspectionTiresWheelsRequest.setTreadDepth(treadDepth);
                    inspectionTiresWheelsRequest.setFourTiresCondition(fourTiresCondition);
                    inspectionTiresWheelsRequest.setScratches(anyScratchesOnWheels);
                    inspectionTiresWheelsRequest.setComments(comments);
                    isCalledTiresWheels = false;
                    inspectionRequestViewModel.inspectionTiresWheel(inspectionTiresWheelsRequest);
                    binding.progressBarInspectionTireWheel.setVisibility(View.VISIBLE);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });

        binding.treadDepthSwitch.addTab(binding.treadDepthSwitch.newTab().setText("Good"));
        binding.treadDepthSwitch.addTab(binding.treadDepthSwitch.newTab().setText("Bad"));
        binding.treadDepthSwitch.addTab(binding.treadDepthSwitch.newTab().setText("N/A"));
        binding.treadDepthSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.treadDepthSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.treadDepthSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.treadDepthSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.treadDepthSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.treadDepthSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.treadDepthSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.treadDepthSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.treadDepthSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.treadDepthSwitch.getTabAt(0).select();


        binding.fourTiresConditionSwitch.addTab(binding.fourTiresConditionSwitch.newTab().setText("Good"));
        binding.fourTiresConditionSwitch.addTab(binding.fourTiresConditionSwitch.newTab().setText("Bad"));
        binding.fourTiresConditionSwitch.addTab(binding.fourTiresConditionSwitch.newTab().setText("N/A"));
        binding.fourTiresConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.fourTiresConditionSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.fourTiresConditionSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.fourTiresConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.fourTiresConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.fourTiresConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.fourTiresConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.fourTiresConditionSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.fourTiresConditionSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        //binding.fourTiresConditionSwitch.getTabAt(0).select();


        binding.anyScratchesWheelsSwitch.addTab(binding.anyScratchesWheelsSwitch.newTab().setText("Good"));
        binding.anyScratchesWheelsSwitch.addTab(binding.anyScratchesWheelsSwitch.newTab().setText("Bad"));
        binding.anyScratchesWheelsSwitch.addTab(binding.anyScratchesWheelsSwitch.newTab().setText("N/A"));
        binding.anyScratchesWheelsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
        binding.anyScratchesWheelsSwitch.setTabGravity(TabLayout.GRAVITY_FILL);

        binding.anyScratchesWheelsSwitch.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        binding.anyScratchesWheelsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_left);
                        binding.anyScratchesWheelsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FF06670A"));
                        break;
                    case 1:
                        binding.anyScratchesWheelsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_middle);
                        binding.anyScratchesWheelsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFBF0202"));
                        break;
                    case 2:
                        binding.anyScratchesWheelsSwitch.setSelectedTabIndicator(R.drawable.thumb_selector_right);
                        binding.anyScratchesWheelsSwitch.setSelectedTabIndicatorColor(Color.parseColor("#FFFF9900"));
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
        // binding.anyScratchesWheelsSwitch.getTabAt(0).select();

        /* for load temp data*/
        binding.treadDepthSwitch.selectTab(binding.treadDepthSwitch.getTabAt(treadDepth_));
        binding.fourTiresConditionSwitch.selectTab(binding.fourTiresConditionSwitch.getTabAt(fourTiresCondition_));
        binding.anyScratchesWheelsSwitch.selectTab(binding.anyScratchesWheelsSwitch.getTabAt(anyScratchesOnWheels_));

        if (tiresWheelComments_ != null) {
            binding.comments.setText(tiresWheelComments_);
        }

        if (!isCalledTiresWheels) {
            inspectionRequestViewModel.getInspectionTiresWheelResponse().observe(requireActivity(), finalResult -> {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof InspectionTiresWheelResponse) {
                        InspectionTiresWheelResponse inspectionTiresWheelResponse = (InspectionTiresWheelResponse) success1;
                        boolean success = inspectionTiresWheelResponse.isSuccess();
                        try {
                            binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (success) {
                            try {
                                navController.navigate(R.id.tiresWheelsToExterior);
                                isCalledTiresWheels = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                        binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
                    }
                }
            });
        }
        inspectionRequestViewModel.getInspectionTiresWheelImageResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof InspectionTiresWheelImageResponse) {
                    InspectionTiresWheelImageResponse tiresWheelImageResponse = (InspectionTiresWheelImageResponse) success1;
                    boolean success = tiresWheelImageResponse.isSuccess();
                    if (success) {
                        binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
                    } else {
                        binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarInspectionTireWheel.setVisibility(View.GONE);
                }
            }
        });
    }

    public void addMultipleImageAndVideos() {
        final CharSequence[] options = {"Take Photo", "Record Video", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
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
                            String fileName = "";
                            String fileSize = "";

                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                imageUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "onActivityResult: " + imageUri);
                                if (imageUri != null) {
                                    if (tiresWheelsAdapter.isImageFile(imageUri)) {
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
                            tiresWheel_uriArrayList.add(selectedImage);
                            tiresWheel_picturePathsList.clear();
                            tiresWheel_picturePathsList.addAll(tiresWheel_uriArrayList);
                            tiresWheelsAdapter.updateList(tiresWheel_picturePathsList); // add this

                            if (tiresWheelsAdapter.isImageFile(selectedImage)) {
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
                            if (tiresWheelsAdapter.isVideoFile(selectedImage)) {
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
                                InspectionTiresWheelImageRequest tiresWheelImageRequest = new InspectionTiresWheelImageRequest();
                                tiresWheelImageRequest.setImageType(Image_Type);
                                tiresWheelImageRequest.setIamgeType(Iamge_Type);
                                tiresWheelImageRequest.setCarId(carId + "");
                                tiresWheelImageRequest.setOthers("1");
                                tiresWheelImageRequest.setPage("7");

                                ArrayList<InspectionTiresWheelImageData> tiresWheelImageDataArrayList = new ArrayList<>();
                                InspectionTiresWheelImageData inspectionTiresWheelImageData = new InspectionTiresWheelImageData();
                                inspectionTiresWheelImageData.setName(fileName);

                                if (tiresWheelsAdapter.isImageFile(selectedImage))
                                    inspectionTiresWheelImageData.setType(Type);
                                else
                                    inspectionTiresWheelImageData.setType("video/mp4");

                                inspectionTiresWheelImageData.setSize(bytesIntoHumanReadable(Long.parseLong(fileSize)));

                                inspectionTiresWheelImageData.setBase64(Base64_Prefix + addMultipleImage);
                                tiresWheelImageDataArrayList.add(inspectionTiresWheelImageData);

                                tiresWheelImageRequest.setImage(tiresWheelImageDataArrayList);
                                inspectionRequestViewModel.inspectionTiresWheelImageApi(tiresWheelImageRequest);
                                binding.progressBarInspectionTireWheel.setVisibility(View.VISIBLE);
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
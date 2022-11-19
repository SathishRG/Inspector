package com.folkus.ui.login.fragments.inspection;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.generalInfo;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.passengerSide;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsFrontCarpet;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsFrontDoor;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsFrontSeat;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsRearDoor;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsRearSeat;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.engineLower;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.odoMeters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.folkus.R;
import com.folkus.comman.ProgressButton;
import com.folkus.data.remote.request.AddExteriorImage;
import com.folkus.data.remote.request.AddExteriorImagesRequest;
import com.folkus.data.remote.response.AddExteriorImagesResponse;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.FragmentInspectionInteriorImagesBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InspectionInteriorImagesFragment extends Fragment {
    private static final String TAG = "InspectionInteriorImagesFragment";
    private FragmentInspectionInteriorImagesBinding binding;
    private InspectionRequestViewModel inspectionRequestViewModel;
    private ProgressButton interiorImageNextBtn;

    private final String Interior_Images = "Interior Images";
    private final String Image_Type = "image/jpg";
    private final String Base64_Prefix = "data:image/jpg;base64,";

    private String odoMeterEncode = null;
    private String dsFrontSeatEncode = null;
    private String dsFrontDoorEncode = null;
    private String dsFrontCarpetEncode = null;
    private String dsRearSeatEncode = null;
    private String dsRearDoorEncode = null;
    private String engineLowerEncode = null;
    private int carId;
    ArrayList<GeneralInfoData> generalInfoData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInspectionInteriorImagesBinding.inflate(inflater, container, false);
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.title_inspection));
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        try {
            carId = generalInfo.getCarId();
            Log.e(TAG, "onViewCreated: " + carId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        subscribeUI();
    }

    private void subscribeUI() {
        binding.inspectionInteriorNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (odoMeters != null || dsFrontSeat != null || dsFrontDoor != null || dsFrontCarpet != null || dsRearSeat != null
                            || dsRearDoor != null || engineLower != null) {
                        navController.navigate(R.id.interiorImagesToPowerTrain);
                    } else {
                        Toast.makeText(requireContext(), R.string.plase_add_image, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (odoMeters != null) {
            Glide.with(requireContext()).asBitmap().load(odoMeters)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.odometerImg);
        }
        if (dsFrontSeat != null) {
            Glide.with(requireContext()).asBitmap().load(dsRearSeat)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsFrontSeatImg);
        }
        if (dsFrontDoor != null) {
            Glide.with(requireContext()).asBitmap().load(dsFrontDoor)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsFrontDoorImg);
        }
        if (dsFrontCarpet != null) {
            Glide.with(requireContext()).asBitmap().load(dsFrontCarpet)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsFrontCarpetImg);
        }
        if (dsRearSeat != null) {
            Glide.with(requireContext()).asBitmap().load(dsRearSeat)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsRearSeatImg);
        }
        if (dsRearDoor != null) {
            Glide.with(requireContext()).asBitmap().load(dsRearDoor)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsRearDoorImg);
        }
        if (engineLower != null) {
            Glide.with(requireContext()).asBitmap().load(engineLower)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.engineLowerImg);
        }

        binding.odometer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                odoMeterSelectImage();
            }
        });

        binding.dsFrontSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsFrontSeatSelectImage();
            }
        });

        binding.dsFrontDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsFrontDoorSelectImage();
            }
        });

        binding.dsFrontCarpet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsFrontCarpetSelectImage();
            }
        });

        binding.dsRearSeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsRearSeatSelectImage();
            }
        });

        binding.dsRearDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsRearDoorSelectImage();
            }
        });

        binding.engineLower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                engineLowerSelectImage();
            }
        });

        inspectionRequestViewModel.getAddExteriorImagesResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarAddInteriorImages.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarAddInteriorImages.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof AddExteriorImagesResponse) {
                    AddExteriorImagesResponse exteriorImagesResponse = (AddExteriorImagesResponse) success1;
                    boolean success = exteriorImagesResponse.isSuccess();
                    if (success) {
                        binding.progressBarAddInteriorImages.setVisibility(View.GONE);
                    } else {
                        binding.progressBarAddInteriorImages.setVisibility(View.GONE);
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarAddInteriorImages.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void odoMeterSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    odoMeterActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    odoMeterActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void dsFrontSeatSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    dsFrontSeatActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    dsFrontSeatActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void dsFrontDoorSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    dsFrontDoorActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    dsFrontDoorActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void dsFrontCarpetSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    dsFrontCarpetActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    dsFrontCarpetActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void dsRearSeatSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    dsRearSeatActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    dsRearSeatActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void dsRearDoorSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    dsRearDoorActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    dsRearDoorActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void engineLowerSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    engineLowerActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    engineLowerActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, formatImageFileName(), null);
        return Uri.parse(path);
    }

    private String formatImageFileName() {
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

    ActivityResultLauncher<Intent> odoMeterActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                Glide.with(requireContext()).asBitmap().load(photo)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.odometerImg);
                                odoMeters = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                odoMeterEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (odoMeterEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(Interior_Images);
                                    addExteriorImagesRequest.setIamgeType("Odometer");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + odoMeterEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("3");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> odoMeterActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();

                            Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            odoMeterEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (odoMeterEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(Interior_Images);
                                addExteriorImagesRequest.setIamgeType("Odometer");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + odoMeterEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("3");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.odometerImg);
                            odoMeters = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsFrontDoorActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                Glide.with(requireContext()).asBitmap().load(photo)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.dsFrontDoorImg);
                                dsFrontDoor = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                dsFrontDoorEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (dsFrontDoorEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(Interior_Images);
                                    addExteriorImagesRequest.setIamgeType("DsFrontDoor");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsFrontDoorEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("3");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsFrontDoorActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();

                            Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            dsFrontDoorEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (dsFrontDoorEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(Interior_Images);
                                addExteriorImagesRequest.setIamgeType("DsFrontDoor");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsFrontDoorEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("3");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.dsFrontDoorImg);
                            dsFrontDoor = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsFrontSeatActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                Glide.with(requireContext()).asBitmap().load(photo)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.dsFrontSeatImg);
                                dsFrontSeat = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                dsFrontSeatEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (dsFrontSeatEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(Interior_Images);
                                    addExteriorImagesRequest.setIamgeType("Ds Front Seat");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsFrontSeatEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("3");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsFrontSeatActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();

                            Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            dsFrontSeatEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (dsFrontSeatEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(Interior_Images);
                                addExteriorImagesRequest.setIamgeType("Ds Front Seat");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsFrontSeatEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("3");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.dsFrontSeatImg);
                            dsFrontSeat = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsFrontCarpetActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                Glide.with(requireContext()).asBitmap().load(photo)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.dsFrontCarpetImg);
                                dsFrontCarpet = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                dsFrontCarpetEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (dsFrontCarpetEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(Interior_Images);
                                    addExteriorImagesRequest.setIamgeType("Ds front Carpet");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsFrontCarpetEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("3");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsFrontCarpetActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();

                            Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            dsFrontCarpetEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (dsFrontCarpetEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(Interior_Images);
                                addExteriorImagesRequest.setIamgeType("Ds front Carpet");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsFrontCarpetEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("3");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.dsFrontCarpetImg);
                            dsFrontCarpet = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsRearSeatActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                Glide.with(requireContext()).asBitmap().load(photo)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.dsRearSeatImg);
                                dsRearSeat = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                dsRearSeatEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (dsRearSeatEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(Interior_Images);
                                    addExteriorImagesRequest.setIamgeType("Ds Rear Seat");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsRearSeatEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("3");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsRearSeatActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();

                            Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            dsRearSeatEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (dsRearSeatEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(Interior_Images);
                                addExteriorImagesRequest.setIamgeType("Ds Rear Seat");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsRearSeatEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("3");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.dsRearSeatImg);
                            dsRearSeat = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsRearDoorActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                Glide.with(requireContext()).asBitmap().load(photo)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.dsRearDoorImg);
                                dsRearDoor = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                dsRearDoorEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (dsRearDoorEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(Interior_Images);
                                    addExteriorImagesRequest.setIamgeType("DsRearDoor");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsRearDoorEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("3");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsRearDoorActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();

                            Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            dsRearDoorEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (dsRearDoorEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(Interior_Images);
                                addExteriorImagesRequest.setIamgeType("DsRearDoor");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsRearDoorEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("3");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.dsRearDoorImg);
                            dsRearDoor = bitmap;
                        }
                    }
                }
            });


    ActivityResultLauncher<Intent> engineLowerActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            try {
                                Bitmap photo = (Bitmap) data.getExtras().get("data");
                                Glide.with(requireContext()).asBitmap().load(photo)
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(binding.engineLowerImg);
                                engineLower = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                engineLowerEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (engineLowerEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(Interior_Images);
                                    addExteriorImagesRequest.setIamgeType("Engine Lower");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + engineLowerEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("3");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> engineLowerActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String[] filePath = {MediaStore.Images.Media.DATA};
                            Cursor c = requireActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                            c.moveToFirst();
                            int columnIndex = c.getColumnIndex(filePath[0]);
                            String picturePath = c.getString(columnIndex);
                            c.close();

                            Bitmap bitmap = (BitmapFactory.decodeFile(picturePath));
                            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            engineLowerEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (engineLowerEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(Interior_Images);
                                addExteriorImagesRequest.setIamgeType("Engine Lower");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + engineLowerEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("3");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddInteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.engineLowerImg);
                            engineLower = bitmap;
                        }
                    }
                }
            });

}
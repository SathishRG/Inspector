package com.folkus.ui.login.fragments.inspection;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.generalInfo;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.carFront;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.driverSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.dsFrontDoor;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.dsFrontPanel;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.dsRearDoor;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.dsRearPanel;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.passengerSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.rearDriverSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.rearPassengerSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.rearSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriage;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriageBack;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriageFront;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriageLeftSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriageRightSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.upperSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.vin;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.folkus.R;
import com.folkus.data.remote.request.AddExteriorImage;
import com.folkus.data.remote.request.AddExteriorImagesRequest;
import com.folkus.data.remote.response.AddExteriorImagesResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.FragmentInspectionExteriorImagesBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class InspectionExteriorImagesFragment extends Fragment {
    private static final String TAG = "InspectionExteriorImagesFragment";
    private FragmentInspectionExteriorImagesBinding binding;
    private InspectionRequestViewModel inspectionRequestViewModel;

    private String carFrontImageEncode = null;
    private String carPassengerSideEncode = null;
    private String driverSideEncode = null;
    private String rearSideEncode = null;
    private String upperSideEncode = null;
    private String rearDriverSideEncode = null;
    private String rearPassengerSideEncode = null;
    private String underCarriageEncode = null;
    private String underCarriageLeftSideEncode = null;
    private String underCarriageRightSideEncode = null;
    private String underCarriageFrontEncode = null;
    private String underCarriageBackEncode = null;
    private String dsFrontPanelEncode = null;
    private String vinEncode = null;
    private String dsFrontDoorEncode = null;
    private String dsRearDoorEncode = null;
    private String dsRearPanelEncode = null;
    private int carId;

    private final String ExteriorImage = "Exterior Images";
    private final String ExteriorPanelImage = "Exterior Panel Images";
    private final String Image_Type = "image/jpg";
    private final String Base64_Prefix = "data:image/jpg;base64,";

    final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInspectionExteriorImagesBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.title_inspection));
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onViewCreated(view, savedInstanceState);
        try {
            carId = generalInfo.getCarId();
            Log.e(TAG, "onViewCreated: " + carId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.progressBarAddExteriorImages.setVisibility(View.GONE);

        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, isGranted -> {
            Log.d("PERMISSIONS", "Launcher result: " + isGranted.toString());
            if (isGranted.containsValue(false)) {
                Log.d("PERMISSIONS", "At least one of the permissions was not granted, launching again...");
                multiplePermissionLauncher.launch(PERMISSIONS);
            }
        });
        askPermissions(multiplePermissionLauncher);

        binding.exteriorPanelImageNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    navController.navigate(R.id.exteriorImagesToInteriorImages);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        /* save data Temporary*/
        if (carFront != null) {
            Glide.with(requireContext()).asBitmap().load(carFront)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.carFrontImageView);
        }
        if (passengerSide != null) {
            Glide.with(requireContext()).asBitmap().load(passengerSide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.carPassengerSideImg);
        }
        if (driverSide != null) {
            Glide.with(requireContext()).asBitmap().load(driverSide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.driverSideImg);
        }
        if (rearSide != null) {
            Glide.with(requireContext()).asBitmap().load(rearSide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.rearSideImg);
            binding.rearSideImg.setImageBitmap(rearSide);
        }
        if (upperSide != null) {
            Glide.with(requireContext()).asBitmap().load(upperSide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.upperSideImg);
        }
        if (rearDriverSide != null) {
            Glide.with(requireContext()).asBitmap().load(rearDriverSide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.rearDriverSideImg);
        }
        if (rearPassengerSide != null) {
            Glide.with(requireContext()).asBitmap().load(rearPassengerSide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.rearPassengerSideImg);
        }
        if (underCarriage != null) {
            Glide.with(requireContext()).asBitmap().load(underCarriage)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.underCarriageImg);
        }
        if (underCarriageLeftSide != null) {
            Glide.with(requireContext()).asBitmap().load(underCarriageLeftSide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.underCarriageLeftSideImg);
        }
        if (underCarriageRightSide != null) {
            Glide.with(requireContext()).asBitmap().load(underCarriageRightSide)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.underCarriageRightSideImg);
        }
        if (underCarriageFront != null) {
            Glide.with(requireContext()).asBitmap().load(underCarriageFront)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.underCarriageFrontImg);
        }
        if (underCarriageBack != null) {
            Glide.with(requireContext()).asBitmap().load(underCarriageBack)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.underCarriageBackImg);
        }
        if (dsFrontPanel != null) {
            Glide.with(requireContext()).asBitmap().load(dsFrontPanel)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsFrontPanelImg);
        }
        if (vin != null) {
            Glide.with(requireContext()).asBitmap().load(vin)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.vinImg);
        }
        if (dsFrontDoor != null) {
            Glide.with(requireContext()).asBitmap().load(dsFrontDoor)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsFrontDoorImg);
        }
        if (dsRearDoor != null) {
            Glide.with(requireContext()).asBitmap().load(dsRearDoor)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsRearDoorImg);
        }
        if (dsRearPanel != null) {
            Glide.with(requireContext()).asBitmap().load(dsRearPanel)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(binding.dsRearPanelImg);
        }
        /* end if navigate next page and again back to this screen hold data*/

        binding.carFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        binding.carPassengerSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passengerSideselectImage();
            }
        });

        binding.driverSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                driverSideSelectImage();
            }
        });

        binding.rearSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rearSideSelectImage();
            }
        });

        binding.upperSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upperSideSelectImage();
            }
        });

        binding.rearDriverSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rearDriverSideSelectImage();
            }
        });

        binding.rearPassengerSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rearPassengerSideSelectImage();
            }
        });

        binding.underCarriage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                underCarriageSelectImage();
            }
        });

        binding.underCarriageLeftSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                underCarriageLeftSelectImage();
            }
        });

        binding.underCarriageRightSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                underCarriageRightSideSelectImage();
            }
        });

        binding.underCarriageFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                underCarriageFrontSelectImage();
            }
        });

        binding.underCarriageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                underCarriageBackSelectImage();
            }
        });

        binding.dsFrontPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsFrontPanelSelectImage();
            }
        });

        binding.vin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vinSelectImage();
            }
        });

        binding.dsFrontDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsFrontDoorSelectImage();
            }
        });

        binding.dsRearDoor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsRearDoorSelectImage();
            }
        });

        binding.dsRearPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dsRearPanelSelectImage();
            }
        });

        inspectionRequestViewModel.getAddExteriorImagesResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarAddExteriorImages.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarAddExteriorImages.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof AddExteriorImagesResponse) {
                    AddExteriorImagesResponse exteriorImagesResponse = (AddExteriorImagesResponse) success1;
                    boolean success = exteriorImagesResponse.isSuccess();
                    if (success) {
                        binding.progressBarAddExteriorImages.setVisibility(View.GONE);
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarAddExteriorImages.setVisibility(View.GONE);
                }
            }
        });
    }

    private void askPermissions(ActivityResultLauncher<String[]> multiplePermissionLauncher) {
        if (!hasPermissions(PERMISSIONS)) {
            Log.d("PERMISSIONS", "Launching multiple contract permission launcher for ALL required permissions");
            multiplePermissionLauncher.launch(PERMISSIONS);
        } else {
            Log.d("PERMISSIONS", "All permissions are already granted");
        }
    }

    private boolean hasPermissions(String[] permissions) {
        if (permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    Log.d("PERMISSIONS", "Permission is not granted: " + permission);
                    return false;
                }
                Log.d("PERMISSIONS", "Permission already granted: " + permission);
            }
            return true;
        }
        return false;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, formatImageFileName(), null);
        return Uri.parse(path);
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

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    carFrontActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    carFrontActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void passengerSideselectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    passengerSideActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    passengerSideActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void driverSideSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    driverSideActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    driverSideActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void rearSideSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    rearSideActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    rearSideActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void upperSideSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    upperSideActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    upperSideActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void rearDriverSideSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    rearDriverSideActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    rearDriverSideActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void rearPassengerSideSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    rearPassengerSideActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    rearPassengerSideActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void underCarriageSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    underCarriageActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    underCarriageActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void underCarriageLeftSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    underCarriageLeftSideActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    underCarriageLeftSideActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void underCarriageRightSideSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    underCarriageRightSideActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    underCarriageRightSideActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void underCarriageFrontSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    underCarriageFrontActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    underCarriageFrontActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void underCarriageBackSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    underCarriageBackActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    underCarriageBackActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void dsFrontPanelSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    dsFrontPanelActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    dsFrontPanelActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void vinSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    vinActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    vinActivityResultLauncher1.launch(intent);
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

    private void dsRearPanelSelectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    dsRearPanelActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    dsRearPanelActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> carFrontActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.carFrontImageView);
                                carFront = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                carFrontImageEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (carFrontImageEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Front");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage1.setBase64(Base64_Prefix + carFrontImageEncode);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> carFrontActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedImage = data.getData();
                            String fileName = "default_file_name";
                            Cursor returnCursor = requireContext().getContentResolver().query(selectedImage, null, null, null, null);
                            try {
                                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                int size = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                                returnCursor.moveToFirst();
                                fileName = returnCursor.getString(nameIndex);
                                String ss = returnCursor.getString(size);
                                Log.e(TAG, "file name1 : " + bytesIntoHumanReadable(Long.parseLong(ss)) + "/" + fileName);
                            } catch (Exception e) {
                                //handle the failure cases here
                            } finally {
                                returnCursor.close();
                            }

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
                            carFrontImageEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (carFrontImageEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Front");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + carFrontImageEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.carFrontImageView);
                            carFront = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> passengerSideActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.carPassengerSideImg);
                                passengerSide = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                carPassengerSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (carPassengerSideEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Passenger Side");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + carPassengerSideEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> passengerSideActivityResultLauncher1 = registerForActivityResult(
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
                            carPassengerSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (carPassengerSideEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Passenger Side");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + carPassengerSideEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.carPassengerSideImg);
                            passengerSide = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> driverSideActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.driverSideImg);
                                driverSide = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                driverSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (driverSideEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Driver Side");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + driverSideEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> driverSideActivityResultLauncher1 = registerForActivityResult(
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
                            driverSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (driverSideEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Driver Side");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + driverSideEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.driverSideImg);
                            driverSide = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> rearSideActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.rearSideImg);
                                rearSide = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                rearSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (rearSideEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Rear Side");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + rearSideEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> rearSideActivityResultLauncher1 = registerForActivityResult(
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
                            rearSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (rearSideEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Rear Side");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + rearSideEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.rearSideImg);
                            rearSide = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> upperSideActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.upperSideImg);
                                upperSide = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                upperSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (upperSideEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Upper Side");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + upperSideEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> upperSideActivityResultLauncher1 = registerForActivityResult(
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
                            upperSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (upperSideEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Upper Side");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + upperSideEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.upperSideImg);
                            upperSide = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> rearDriverSideActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.rearDriverSideImg);
                                rearDriverSide = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                rearDriverSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (rearDriverSideEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Rear Driver Side");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + rearDriverSideEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> rearDriverSideActivityResultLauncher1 = registerForActivityResult(
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
                            rearDriverSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (rearDriverSideEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Rear Driver Side");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + rearDriverSideEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.rearDriverSideImg);
                            rearDriverSide = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> rearPassengerSideActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.rearPassengerSideImg);
                                rearPassengerSide = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                rearPassengerSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (rearPassengerSideEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Rear Passenger Side");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + rearPassengerSideEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> rearPassengerSideActivityResultLauncher1 = registerForActivityResult(
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
                            rearPassengerSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (rearPassengerSideEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Rear Passenger Side");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + rearPassengerSideEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.rearPassengerSideImg);
                            rearPassengerSide = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.underCarriageImg);
                                underCarriage = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                underCarriageEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (underCarriageEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Undercarrige");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + underCarriageEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageActivityResultLauncher1 = registerForActivityResult(
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
                            underCarriageEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (underCarriageEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Undercarrige");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + underCarriageEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.underCarriageImg);
                            underCarriage = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageLeftSideActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.underCarriageLeftSideImg);
                                underCarriageLeftSide = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);
                              /*  // CALL THIS METHOD TO GET THE ACTUAL PATH
                                File finalFile = new File(getRealPathFromURI(tempUri));
                                Log.e(TAG, "finalFile: " + finalFile);*/

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                underCarriageLeftSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (underCarriageLeftSideEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Undercarriage Left Side");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + underCarriageLeftSideEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageLeftSideActivityResultLauncher1 = registerForActivityResult(
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
                            underCarriageLeftSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (underCarriageLeftSideEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Undercarriage Left Side");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + underCarriageLeftSideEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.underCarriageLeftSideImg);
                            underCarriageLeftSide = bitmap;
                        }
                    }
                }
            });
    ActivityResultLauncher<Intent> underCarriageRightSideActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.underCarriageRightSideImg);
                                underCarriageRightSide = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                underCarriageRightSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (underCarriageRightSideEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Undercarriage Right Side");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + underCarriageRightSideEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageRightSideActivityResultLauncher1 = registerForActivityResult(
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
                            underCarriageRightSideEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (underCarriageRightSideEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Undercarriage Right Side");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + underCarriageRightSideEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.underCarriageRightSideImg);
                            underCarriageRightSide = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageFrontActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.underCarriageFrontImg);
                                underCarriageFront = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                underCarriageFrontEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (underCarriageFrontEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Undercarriage Front");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + underCarriageFrontEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageFrontActivityResultLauncher1 = registerForActivityResult(
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
                            underCarriageFrontEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (underCarriageFrontEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Undercarriage Front");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + underCarriageFrontEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.underCarriageFrontImg);
                            underCarriageFront = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageBackActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.underCarriageBackImg);
                                underCarriageBack = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                underCarriageBackEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (underCarriageBackEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorImage);
                                    addExteriorImagesRequest.setIamgeType("Undercarriage Back");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + underCarriageBackEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> underCarriageBackActivityResultLauncher1 = registerForActivityResult(
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
                            underCarriageBackEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (underCarriageBackEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorImage);
                                addExteriorImagesRequest.setIamgeType("Undercarriage Back");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + underCarriageBackEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.underCarriageBackImg);
                            underCarriageBack = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsFrontPanelActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.dsFrontPanelImg);
                                dsFrontPanel = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                dsFrontPanelEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (dsFrontPanelEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                    addExteriorImagesRequest.setIamgeType("Ds Front Panel");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsFrontPanelEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsFrontPanelActivityResultLauncher1 = registerForActivityResult(
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
                            dsFrontPanelEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (dsFrontPanelEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                addExteriorImagesRequest.setIamgeType("Ds Front Panel");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsFrontPanelEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.dsFrontPanelImg);
                            dsFrontPanel = bitmap;
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> vinActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.vinImg);
                                vin = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                vinEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (vinEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                    addExteriorImagesRequest.setIamgeType("VIN");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + vinEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> vinActivityResultLauncher1 = registerForActivityResult(
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
                            vinEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (vinEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                addExteriorImagesRequest.setIamgeType("VIN");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + vinEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.vinImg);
                            vin = bitmap;
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
                                    addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                    addExteriorImagesRequest.setIamgeType("Ds Front Door");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsFrontDoorEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
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
                                addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                addExteriorImagesRequest.setIamgeType("Ds Front Door");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsFrontDoorEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
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
                                    addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                    addExteriorImagesRequest.setIamgeType("Ds Rear Door");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsRearDoorEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
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
                                addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                addExteriorImagesRequest.setIamgeType("Ds Rear Door");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsRearDoorEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
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

    ActivityResultLauncher<Intent> dsRearPanelActivityResultLauncher = registerForActivityResult(
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
                                        .into(binding.dsRearPanelImg);
                                dsRearPanel = photo;
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                dsRearPanelEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (dsRearPanelEncode != null) {
                                    AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                    addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                    addExteriorImagesRequest.setIamgeType("Ds Rear Panel");
                                    addExteriorImagesRequest.setCarId(carId);
                                    addExteriorImagesRequest.setOthers("");

                                    ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                    AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                    addExteriorImage1.setBase64(Base64_Prefix + dsRearPanelEncode);
                                    addExteriorImage1.setType(Image_Type);
                                    addExteriorImage.add(addExteriorImage1);

                                    addExteriorImagesRequest.setImage(addExteriorImage);
                                    addExteriorImagesRequest.setPage("2");

                                    inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                    binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> dsRearPanelActivityResultLauncher1 = registerForActivityResult(
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
                            dsRearPanelEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (dsRearPanelEncode != null) {
                                AddExteriorImagesRequest addExteriorImagesRequest = new AddExteriorImagesRequest();
                                addExteriorImagesRequest.setImageType(ExteriorPanelImage);
                                addExteriorImagesRequest.setIamgeType("Ds Rear Panel");
                                addExteriorImagesRequest.setCarId(carId);
                                addExteriorImagesRequest.setOthers("");

                                ArrayList<AddExteriorImage> addExteriorImage = new ArrayList<>();
                                AddExteriorImage addExteriorImage1 = new AddExteriorImage();
                                addExteriorImage1.setBase64(Base64_Prefix + dsRearPanelEncode);
                                addExteriorImage1.setType(Image_Type);
                                addExteriorImage.add(addExteriorImage1);

                                addExteriorImagesRequest.setImage(addExteriorImage);
                                addExteriorImagesRequest.setPage("2");

                                inspectionRequestViewModel.addExteriorImages(addExteriorImagesRequest);
                                binding.progressBarAddExteriorImages.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(binding.dsRearPanelImg);
                            dsRearPanel = bitmap;
                        }
                    }
                }
            });

    /*  private String createImageFile() throws IOException {
          String timeStamp =
                  new SimpleDateFormat("yyyyMMdd_HHmmss",
                          Locale.getDefault()).format(new Date());
          String imageFileName = "IMG_" + timeStamp + "_";
          File storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
          File image = File.createTempFile(
                  imageFileName,  *//* prefix *//*
                ".jpg",         *//* suffix *//*
                storageDir      *//* directory *//*
        );

        imageFilePath = image.getAbsolutePath();
        return imageFileName;
    }
*/
    private String formatImageFileName() {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_.jpg";
        return imageFileName;
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
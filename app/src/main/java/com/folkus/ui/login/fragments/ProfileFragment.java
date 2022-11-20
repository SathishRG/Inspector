package com.folkus.ui.login.fragments;

import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.carFront;
import static com.folkus.ui.login.fragments.inspection.InspectionPowerTrainFragment.getImageUri;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.folkus.R;
import com.folkus.data.remote.request.ProfileEditRequest;
import com.folkus.data.remote.request.ProfileImageData;
import com.folkus.data.remote.response.AddExteriorImagesResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.ProfileEditResponse;
import com.folkus.databinding.FragmentProfileBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;
import com.folkus.ui.login.fragments.dialog.DialogChangePassword;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private FragmentProfileBinding binding;
    private UserViewModel userViewModel;
    private final String Image_Type = "image/jpg";
    private final String Base64_Prefix = "data:image/jpg;base64,";
    private String profileImageEncode = null;
    public static boolean isEditable = false;

    private String dateOfBirth = "";
    private String emailId = "";
    private String contact = "";
    private String address = "";
    private InspectionRequestViewModel inspectionRequestViewModel;
    private Menu globalMenuItem;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(requireContext().getApplicationContext())).get(UserViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        if (!isEditable) {
            textInputFieldDisable();
        } else {
            textInputFieldEnable();
        }

        binding.ivProfileImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isEditable) {
                    selectImage();
                }
                return false;
            }
        });

        userViewModel.getProfileDetails();
        userViewModel.profileData.observe(requireActivity(), loginData -> {
            binding.etDateOfBirthValue.setText(loginData.getDob());
            binding.etProfileEmailIdValue.setText(loginData.getEmail());
            binding.etContactValue.setText(loginData.getPhone_no());
            binding.etAddressValue.setText(loginData.getAddress());
            binding.tvName.setText(loginData.getName());
            binding.tvEmailId.setText(loginData.getEmail());

            dateOfBirth = loginData.getDob();
            emailId = loginData.getEmail();
            contact = loginData.getPhone_no();
            address = loginData.getAddress();
        });

        binding.tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutDialog();
            }
        });

        SpannableString mSpannableString = new SpannableString(getString(R.string.change_password));
        mSpannableString.setSpan(new UnderlineSpan(), 0, mSpannableString.length(), 0);
        binding.tvChangePassword.setText(mSpannableString);
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.menu_my_profile));


        inspectionRequestViewModel.getProfileEditResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                binding.progressBarProfileEdit.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarProfileEdit.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof ProfileEditResponse) {
                    ProfileEditResponse profileEditResponse = (ProfileEditResponse) success1;
                    binding.progressBarProfileEdit.setVisibility(View.GONE);
                    if (profileEditResponse.isSuccess()) {
                        Toast.makeText(requireContext(), profileEditResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        isEditable = false;
                    } else {
                        Toast.makeText(requireContext(), profileEditResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    binding.progressBarProfileEdit.setVisibility(View.GONE);
                }
            }
        });

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(R.string.add_profile_photo);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    profileImageActivityResultLauncher.launch(intent);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    profileImageActivityResultLauncher1.launch(intent);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> profileImageActivityResultLauncher = registerForActivityResult(
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
                                        .circleCrop()
                                        .into(binding.ivProfileImageView);
                                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                                Uri tempUri = getImageUri(requireActivity().getApplicationContext(), photo);
                                Log.e(TAG, "tempUri: " + tempUri);

                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                photo.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                                byte[] byteArray = byteArrayOutputStream.toByteArray();
                                profileImageEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                                if (profileImageEncode != null) {
                                    ProfileEditRequest profileEditRequest = new ProfileEditRequest();
                                    profileEditRequest.setDob(dateOfBirth);
                                    profileEditRequest.setPhoneNo(contact);
                                    profileEditRequest.setAddress(address);
                                    profileEditRequest.setInspectorId(inspectionRequestViewModel.getInspectorId());

                                    ArrayList<ProfileImageData> profileImageDataList = new ArrayList<>();
                                    ProfileImageData profileImageData = new ProfileImageData();
                                    profileImageData.setType(Image_Type);
                                    profileImageData.setBase64(Base64_Prefix + profileImageEncode);
                                    profileImageDataList.add(profileImageData);

                                    profileEditRequest.setImage(profileImageDataList);
                                    // inspectionRequestViewModel.profileEditApi(profileEditRequest);
                                    //  binding.progressBarProfileEdit.setVisibility(View.VISIBLE);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> profileImageActivityResultLauncher1 = registerForActivityResult(
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
                            profileImageEncode = Base64.encodeToString(byteArray, Base64.DEFAULT);
                            if (profileImageEncode != null) {
                                ProfileEditRequest profileEditRequest = new ProfileEditRequest();
                                profileEditRequest.setDob(dateOfBirth);
                                profileEditRequest.setPhoneNo(contact);
                                profileEditRequest.setAddress(address);
                                profileEditRequest.setInspectorId(inspectionRequestViewModel.getInspectorId());

                                ArrayList<ProfileImageData> profileImageDataList = new ArrayList<>();
                                ProfileImageData profileImageData = new ProfileImageData();
                                profileImageData.setType(Image_Type);
                                profileImageData.setBase64(Base64_Prefix + profileImageEncode);
                                profileImageDataList.add(profileImageData);

                                profileEditRequest.setImage(profileImageDataList);
                                //     inspectionRequestViewModel.profileEditApi(profileEditRequest);
                                // binding.progressBarProfileEdit.setVisibility(View.VISIBLE);
                            }
                            Glide.with(requireContext()).asBitmap().load(bitmap)
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .circleCrop()
                                    .into(binding.ivProfileImageView);
                            carFront = bitmap;
                        }
                    }
                }
            });

    private void showLogoutDialog() {
        ActivityHome activity = (ActivityHome) getActivity();
        if (activity != null) {
            FragmentManager fm = activity.getSupportFragmentManager();
            DialogChangePassword dialogChangePassword = DialogChangePassword.newInstance();
            dialogChangePassword.show(fm, "fragment_change_pwd");
            dialogChangePassword.setCancelable(false);
        }
    }

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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile_edit_menu, menu);
        globalMenuItem = menu;
        if (!isEditable) {
            globalMenuItem.findItem(R.id.profile_edit).setVisible(true);
            globalMenuItem.findItem(R.id.profile_save).setVisible(false);
        } else {
            globalMenuItem.findItem(R.id.profile_edit).setVisible(false);
            globalMenuItem.findItem(R.id.profile_save).setVisible(true);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile_edit:
                isEditable = true;
                globalMenuItem.findItem(R.id.profile_edit).setVisible(false);
                globalMenuItem.findItem(R.id.profile_save).setVisible(true);
                textInputFieldEnable();
                break;

            case R.id.profile_save:
                globalMenuItem.findItem(R.id.profile_edit).setVisible(true);
                globalMenuItem.findItem(R.id.profile_save).setVisible(false);
                ProfileEditRequest profileEditRequest = new ProfileEditRequest();
                profileEditRequest.setDob(dateOfBirth);
                profileEditRequest.setPhoneNo(contact);
                profileEditRequest.setAddress(address);
                profileEditRequest.setInspectorId(inspectionRequestViewModel.getInspectorId());

                ArrayList<ProfileImageData> profileImageDataList = new ArrayList<>();
                ProfileImageData profileImageData = new ProfileImageData();
                profileImageData.setType(Image_Type);
                if (profileImageEncode != null) {
                    profileImageData.setBase64(Base64_Prefix + profileImageEncode);
                } else {
                    profileImageData.setBase64(Base64_Prefix);
                }
                profileImageDataList.add(profileImageData);
                profileEditRequest.setImage(profileImageDataList);
                inspectionRequestViewModel.profileEditApi(profileEditRequest);
                binding.progressBarProfileEdit.setVisibility(View.VISIBLE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void textInputFieldDisable() {
        binding.etDateOfBirthValue.setEnabled(false);
        binding.etProfileEmailIdValue.setEnabled(false);
        binding.etContactValue.setEnabled(false);
        binding.etAddressValue.setEnabled(false);
        binding.tvName.setEnabled(false);
        binding.tvEmailId.setEnabled(false);
    }

    private void textInputFieldEnable() {
        binding.etDateOfBirthValue.setEnabled(true);
        binding.etProfileEmailIdValue.setEnabled(true);
        binding.etContactValue.setEnabled(true);
        binding.etAddressValue.setEnabled(true);
        binding.tvName.setEnabled(true);
        binding.tvEmailId.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

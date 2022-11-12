package com.folkus.ui.login.fragments.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.comman.ProgressButton;
import com.folkus.data.remote.response.ChangePasswordResponse;
import com.folkus.databinding.DialogChangePasswordBinding;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;

public class DialogChangePassword extends DialogFragment {
    private DialogChangePasswordBinding binding;
    private EditText dtfOldPassword, dtfNewPassword, dtfConfirmPassword;
    private UserViewModel userViewModel;

    public DialogChangePassword() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogChangePassword newInstance() {
        DialogChangePassword frag = new DialogChangePassword();
        return frag;
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(requireContext().getApplicationContext())).get(UserViewModel.class);
        return inflater.inflate(R.layout.dialog_change_password, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dtfOldPassword = (EditText) view.findViewById(R.id.edOldPwd);
        dtfNewPassword = (EditText) view.findViewById(R.id.edNewPwd);
        dtfConfirmPassword = (EditText) view.findViewById(R.id.edConfirmNewPwd);
        ConstraintLayout btnChangePassword = (ConstraintLayout) view.findViewById(R.id.constraint_layout);
        AppCompatButton btnCancelChangePwd = (AppCompatButton) view.findViewById(R.id.btnCancelChangePwd);


        ProgressButton progressButton = new ProgressButton(requireContext(), btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPwd = dtfOldPassword.getText().toString();
                String newPwd = dtfNewPassword.getText().toString();
                String cnfNewPwd = dtfConfirmPassword.getText().toString();
                if (oldPwd.isEmpty()) {
                    dtfOldPassword.setError(getString((R.string.old_pwd_field_empty)));
                } else if (newPwd.isEmpty()) {
                    dtfNewPassword.setError(getString((R.string.new_pwd_field_empty)));
                } else if (cnfNewPwd.isEmpty()) {
                    dtfConfirmPassword.setError(getString((R.string.confirm_pwd_field_empty)));
                } else if (newPwd.contentEquals(cnfNewPwd)) {
                    progressButton.buttonActivated();
                    userViewModel.doChangePassword(userViewModel.getProfileDetails().getInspector_id(), oldPwd, newPwd);
                } else {
                    dtfConfirmPassword.setError(getString((R.string._new_confirm_field_empty)));
                }
            }
        });


        btnCancelChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        userViewModel.observeChangePassword.observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                return;
            }
            if (finalResult.getSuccess() != null) {
                ChangePasswordResponse changePasswordResponse = (ChangePasswordResponse) finalResult.getSuccess();
                if(changePasswordResponse.isSuccess()) {
                    hideProgress(progressButton);
                    userViewModel.doLogout();
                    Toast.makeText(requireActivity(), "Password has been changed", Toast.LENGTH_SHORT).show();
                }else {
                    String passwordError = changePasswordResponse.getChangePasswordError().getPasswordError();
                    hideProgress(progressButton);
                    Toast.makeText(requireActivity(), passwordError, Toast.LENGTH_SHORT).show();
                }
            } else if (finalResult.getError() != null) {
                hideProgress(progressButton);
                Toast.makeText(requireActivity(), "Change Password failed", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void hideProgress(ProgressButton progressButton) {
        progressButton.buttonReverted();
        progressButton.buttonFinished();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        userViewModel.observeChangePassword.removeObservers(requireActivity());
    }
}

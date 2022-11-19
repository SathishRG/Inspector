package com.folkus.ui.login.fragments.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.comman.ProgressButton;
import com.folkus.data.remote.response.ForgetPasswordError;
import com.folkus.data.remote.response.ForgetPasswordResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.LoginResponse;
import com.folkus.databinding.DialogForgotPasswordBinding;
import com.folkus.ui.login.ActivityLogin;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;
import com.folkus.ui.login.model.FinalResult;

public class DialogForgetPassword extends DialogFragment {
    private DialogForgotPasswordBinding binding;
    private EditText dtfForgetPasswordEmail;
    private UserViewModel userViewModel;
    private static DialogForgetPassword dialogForgetPassword;

    public DialogForgetPassword() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogForgetPassword newInstance() {
        if (dialogForgetPassword == null) {
            dialogForgetPassword = new DialogForgetPassword();
        }
        return dialogForgetPassword;
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
        binding = DialogForgotPasswordBinding.inflate(inflater, container, false);
        return inflater.inflate(R.layout.dialog_forgot_password, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dtfForgetPasswordEmail = (EditText) view.findViewById(R.id.dtfForgetPasswordEmail);

        ConstraintLayout btnChangePassword = (ConstraintLayout) view.findViewById(R.id.constraint_layout);
        TextView textView2 = (TextView) view.findViewById(R.id.textView2);
        textView2.setText("Ok");
        AppCompatButton btnCancel = (AppCompatButton) view.findViewById(R.id.btnCancel);


        ProgressButton progressButton = new ProgressButton(requireContext(), btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailId = dtfForgetPasswordEmail.getText().toString().trim();
                if (emailId.isEmpty()) {
                    dtfForgetPasswordEmail.setError(getString((R.string.email_field_empty)));
                } else {
                    if (isValidEmail(emailId)) {
                        progressButton.buttonActivated();
                        userViewModel.doForgetPassword(emailId);
                        dtfForgetPasswordEmail.setError(null);
                        dtfForgetPasswordEmail.setText("");
                    } else {
                        dtfForgetPasswordEmail.setError("Invalid email");
                    }
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                dtfForgetPasswordEmail.setError(null);
                dtfForgetPasswordEmail.setText("");
            }
        });


/*        userViewModel.observeForgetPassword.observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                return;
            }
            if (finalResult.getSuccess() != null) {
                ForgetPasswordResponse forgetPasswordResponse = (ForgetPasswordResponse) finalResult.getSuccess();
                if (((ForgetPasswordResponse) finalResult.getSuccess()).isSuccess()) {
                    Log.e("TAG", "onViewCreated: success");
                } else {
                    Log.e("TAG", "onViewCreated: error");
                }

                if (forgetPasswordResponse.isSuccess()) {
                    hideProgress(progressButton);
                    forgetPasswordResponse.setForgetPasswordError(null);
                    userViewModel.doLogout();
                    Toast.makeText(requireActivity(), "Email sent successfully", Toast.LENGTH_SHORT).show();
                } else {
                    hideProgress(progressButton);
                    Toast.makeText(requireActivity(), forgetPasswordResponse.getForgetPasswordError().getMailError(), Toast.LENGTH_SHORT).show();
                }
            } else if (finalResult.getError() != null) {
                hideProgress(progressButton);
                Toast.makeText(requireActivity(), "Forget Password failed", Toast.LENGTH_SHORT).show();
            }
            dismiss();
        });*/

        if (!UserViewModel.isCalledForgotPwdOnce) {
            userViewModel.getForgotPasswordResult().observe(this, new Observer<FinalResult>() {
                @Override
                public void onChanged(@Nullable FinalResult loginResult) {
                    if (loginResult == null) {
                        Log.e("TAG", "onChanged: null");
                        return;
                    }
                    Object success1 = loginResult.getSuccess();
                    if (success1 != null) {
                        Log.e("TAG", "onChanged: sucess1");
                        if (success1 instanceof ForgetPasswordResponse) {
                            ForgetPasswordResponse loginResponse = (ForgetPasswordResponse) success1;
                            boolean success = loginResponse.isSuccess();
                            if (success) {
                                hideProgress(progressButton);
                                userViewModel.doLogout();
                                dtfForgetPasswordEmail.setError(null);
                                Toast.makeText(requireActivity(), "Email sent successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                hideProgress(progressButton);
                                Toast.makeText(requireActivity(), loginResponse.forgetPasswordError.getMailError(), Toast.LENGTH_SHORT).show();
                            }
                        } else if (success1 instanceof ForgetPasswordError) {
                            Log.e("TAG", "onChanged:"+ ((ForgetPasswordError) success1).getMailError());
                            hideProgress(progressButton);
                            Toast.makeText(requireActivity(), ((ForgetPasswordError) success1).getMailError(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    dismiss();
                }
            });
        }
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private boolean isValidEmail(String username) {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    private void hideProgress(ProgressButton progressButton) {
        progressButton.buttonReverted();
        progressButton.buttonFinished();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        userViewModel.observeForgetPassword.removeObservers(requireActivity());
        dtfForgetPasswordEmail.setError(null);
    }
}
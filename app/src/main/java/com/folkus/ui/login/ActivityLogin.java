package com.folkus.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.comman.ProgressButton;
import com.folkus.data.Result;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.ActivityLoginBinding;
import com.folkus.data.remote.response.LoginResponse;
import com.folkus.ui.login.fragments.dialog.DialogForgetPassword;
import com.folkus.ui.login.model.LoginFormState;
import com.folkus.ui.login.model.FinalResult;
import com.google.android.material.textfield.TextInputEditText;

public class ActivityLogin extends AppCompatActivity {

    private UserViewModel userViewModel;
    private ActivityLoginBinding binding;
    private ProgressButton loginButton;
    public int countFailed = 0;
    public int countSuccess = 0;

    private NetworkChangeReceiver networkChangeReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        networkChangeReceiver = new NetworkChangeReceiver();
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(getApplicationContext())).get(UserViewModel.class);


        loginButton = new ProgressButton(this, binding.btnLogin.constraintLayout);

        final EditText usernameEditText = binding.editTextName;
        final EditText passwordEditText = binding.editTextPwd;
        final TextView tvForgotPassword = binding.tvForgotPassword;

        SpannableString spanString = new SpannableString("Don't have an Account ? Become an Inspector");
        spanString.setSpan(
                new ForegroundColorSpan(
                        ContextCompat.getColor(getApplicationContext(),
                                R.color.app_red)),
                24, // start
                43, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        spanString.setSpan(new UnderlineSpan(),
                24, // start
                43, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        binding.tvInspector.setText(spanString);

        SpannableString forgotPasswordSpan = new SpannableString(getString(R.string.forgot_password));
        SpannableString rememberSpan = new SpannableString(getString(R.string.remember_me));

        forgotPasswordSpan.setSpan(
                new UnderlineSpan(),
                0, // start
                15, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        rememberSpan.setSpan(
                new UnderlineSpan(),
                0, // start
                11, // end
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        );
        binding.checkedTVRemeberMe.setText(rememberSpan);
        binding.tvForgotPassword.setText(forgotPasswordSpan);
        binding.btnLogin.textView2.setText("Login");

      /*  TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                userViewModel.loginDataChanged(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    userViewModel.login(usernameEditText.getText().toString(), passwordEditText.getText().toString());
                }
                return false;
            }
        });*/

        //  binding.editTextName.setText("naveenkumar@zhagaramss.com");
        // binding.editTextPwd.setText("Shaik@1234");
/*        userViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });*/

        userViewModel.getLoginResult().observe(this, new Observer<FinalResult>() {
            @Override
            public void onChanged(@Nullable FinalResult loginResult) {
                if (loginResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (loginResult.getError() != null) {
                    Toast.makeText(getApplicationContext(), "" + loginResult.getError(), Toast.LENGTH_SHORT).show();
                    loginButton.buttonReverted();
                }
                Object success1 = loginResult.getSuccess();
                if (success1 != null) {
                    Log.e("TAG", "onChanged: sucess1");
                    if (success1 instanceof LoginResponse) {
                        if (countSuccess < 1) {
                            countSuccess++;
                            LoginResponse loginResponse = (LoginResponse) success1;
                            boolean success = loginResponse.success;
                            if (success) {
                                navigateToNextScreen();
                                loginButton.buttonReverted();
                                Toast.makeText(ActivityLogin.this, "Welcome " + loginResponse.getData().getName(), Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("TAG", "onChanged: error");
                                loginButton.buttonReverted();
                                Toast.makeText(ActivityLogin.this, loginResponse.getError().getErr(), Toast.LENGTH_SHORT).show();
                            }
                            loginButton.buttonReverted();
                        }
                    } else if (success1 instanceof LoginError) {
                        if (countFailed < 1) {
                            countFailed++;
                            LoginError loginError = (LoginError) success1;
                            loginButton.buttonReverted();
                            Log.e("TAG", "onChanged: " + loginError.err);
                            Toast.makeText(getApplicationContext(), loginError.err, Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                setResult(Activity.RESULT_OK);
            }
        });

        binding.btnLogin.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                boolean isValid = false;
                countSuccess = 0;
                countFailed = 0;
                if (userName.isEmpty()) {
                    usernameEditText.setError("Email must be required");
                } else {
                    if (isValidEmail(userName)) {
                        usernameEditText.setError(null);
                        isValid = true;
                    } else {
                        usernameEditText.setError("Invalid email");
                    }
                }

                if (isValid) {
                    if (password.isEmpty()) {
                        passwordEditText.setError("Password must be required");
                    } else {
                        if (isValidPassword(password)) {
                            passwordEditText.setError(null);
                            loginButton.buttonActivated();
                            if (countFailed >= 1 || countSuccess >= 1) {
                                countFailed = 0;
                                countSuccess = 0;
                            }
                            userViewModel.login(userName, password);
                            Log.e("onClick: ", "" + userName + "/" + password);
                        } else {
                            passwordEditText.setError("Password length should be 5 characters");
                        }
                    }
                }
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                DialogForgetPassword dialogForgetPassword = DialogForgetPassword.newInstance();
                dialogForgetPassword.show(fm, "fragment_forget_pwd");
                dialogForgetPassword.setCancelable(false);
            }
        });

        binding.tvInspector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActivityLogin.this, ActivityAddInspector.class));
            }
            // viewModel.onAddInspectorClicked()
        });
    }

    private void navigateToNextScreen() {
        Intent intent = new Intent(ActivityLogin.this, ActivityHome.class);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Internet Connection");
        builder.setMessage("App required internet connection");
        builder.setPositiveButton("Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
                        registerReceiver(receiver, filter);
                        dialog.dismiss();
                    }
                });
        AlertDialog alertDialog1 = builder.create();
        alertDialog1.setCancelable(false);
        alertDialog1.show();
    }


    private boolean isValidEmail(String username) {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    private boolean isValidPassword(String password) {
        return password.length() > 5;
    }

    private void updateUiWithUser(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, filter);
        super.onStart();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(networkChangeReceiver);
        super.onPause();
    }
}
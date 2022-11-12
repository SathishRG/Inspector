package com.folkus.ui.login;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.folkus.R;
import com.folkus.data.Result;
import com.folkus.data.remote.request.NotificationRequest;
import com.folkus.data.remote.response.ChangePasswordResponse;
import com.folkus.data.remote.response.CountResponse;
import com.folkus.data.remote.response.DeleteAllNotificationResponse;
import com.folkus.data.remote.response.DeleteNotificationResponse;
import com.folkus.data.remote.response.ForgetPasswordError;
import com.folkus.data.remote.response.ForgetPasswordResponse;
import com.folkus.data.remote.response.LoginData;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.LoginResponse;
import com.folkus.data.remote.response.NotificationResponse;
import com.folkus.data.repository.UserRepository;
import com.folkus.ui.login.model.FinalResult;
import com.folkus.ui.login.model.LoginFormState;

public class UserViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<FinalResult> loginResult = new MutableLiveData<FinalResult>();
    private MutableLiveData<FinalResult> observeCountData = new MutableLiveData<>();
    public MutableLiveData<FinalResult> observeChangePassword = new MutableLiveData<>();
    public MutableLiveData<FinalResult> observeForgetPassword = new MutableLiveData<>();
    public MutableLiveData<FinalResult> observeNotifications = new MutableLiveData<>();
    public MutableLiveData<FinalResult> observeDeleteNotifications = new MutableLiveData<>();
    public MutableLiveData<FinalResult> observeDeleteAllNotifications = new MutableLiveData<>();
    public MutableLiveData<LoginData> profileData = new MutableLiveData();
    public MutableLiveData<Boolean> logoutNavigation = new MutableLiveData();
    private MutableLiveData<LoginResponse> loginResult1 = new MutableLiveData<>();
    private MutableLiveData<Boolean> isConnectedInternet = new MutableLiveData<>();

    private UserRepository userRepository;
    public static boolean isCalledLoginApiOnce = false; /* for this avoid next response come util wait*/
    public static boolean isCalledForgotPwdOnce = false; /* for this avoid next response come util wait*/

    public UserViewModel(UserRepository loginRepository) {
        this.userRepository = loginRepository;
    }

    public MutableLiveData<Boolean> hasConnectedInternet() {
        return isConnectedInternet;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<FinalResult> getLoginResult() {
        return loginResult;
    }

    public LiveData<FinalResult> getForgotPasswordResult() {
        return observeForgetPassword;
    }

    public LiveData<LoginResponse> getLoginResult1() {
        return loginResult1;
    }

    public MutableLiveData<FinalResult> getObserveCountData() {
        return observeCountData;
    }

    public MutableLiveData<FinalResult> getObserveNotifications() {
        return observeNotifications;
    }

    public MutableLiveData<FinalResult> getObserveDeleteNotifications() {
        return observeDeleteNotifications;
    }

    public MutableLiveData<FinalResult> getObserveDeleteAllNotifications() {
        return observeDeleteAllNotifications;
    }

    public void setUserDetails() {
        profileData.postValue(getProfileDetails());
    }

    public LoginData getProfileDetails() {
        LoginData profileDetails = userRepository.getProfileDetails();
        profileData.postValue(profileDetails);
        return profileDetails;
    }

    public void getCountData() {
        LoginData user = getProfileDetails();
        if (user != null) {
            userRepository.getCountData();
            userRepository.observeCountData().observeForever(new Observer<Result<CountResponse>>() {
                @Override
                public void onChanged(Result<CountResponse> countResponseResult) {
                    if (countResponseResult instanceof Result.Success) {
                        CountResponse data = ((Result.Success<CountResponse>) countResponseResult).getData();
                        observeCountData.setValue(new FinalResult((CountResponse) data));
                    } else {
                        observeCountData.setValue(new FinalResult(R.string.login_failed));
                    }
                }
            });
        }
    }

    public void login(String username, String password) {
        isCalledLoginApiOnce = true;
        userRepository.login(username, password);
        userRepository.getLoginResponse().observeForever(new Observer<Result<LoginResponse>>() {
            @Override
            public void onChanged(Result<LoginResponse> loginResponseResult) {
                if (loginResponseResult instanceof Result.Success) {
                    if (!isCalledLoginApiOnce) {
                        LoginResponse data = ((Result.Success<LoginResponse>) loginResponseResult).getData();
                        userRepository.saveCurrentUser(data.getData());
                        userRepository.setProfileDetails(data.getData());
                        loginResult.postValue(new FinalResult((LoginResponse) ((Result.Success<?>) loginResponseResult).getData()));
                    }
                } else {
                    if (!isCalledLoginApiOnce) {
                        LoginError data = ((Result.Error<LoginError>) loginResponseResult).getError();
                        loginResult.postValue(new FinalResult((LoginError) data));
                    }
                }
            }
        });
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    public void doChangePassword(int inspectorId, String oldPassword, String newPassword) {
        userRepository.doChangePassword(inspectorId, oldPassword, newPassword);
        userRepository.observeChangePassword().observeForever(new Observer<Result<ChangePasswordResponse>>() {
            @Override
            public void onChanged(Result<ChangePasswordResponse> countResponseResult) {
                try {
                    if (countResponseResult instanceof Result.Success) {
                        ChangePasswordResponse data = ((Result.Success<ChangePasswordResponse>) countResponseResult).getData();
                        observeChangePassword.setValue(new FinalResult((ChangePasswordResponse) data));
                    } else {
                        observeChangePassword.setValue(new FinalResult(R.string.change_pwd_failed));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void doForgetPassword(String emailId) {
        isCalledForgotPwdOnce = true;
        userRepository.doForgetPassword(emailId);
        userRepository.observeForgetPassword().observeForever(new Observer<Result<ForgetPasswordResponse>>() {
            @Override
            public void onChanged(Result<ForgetPasswordResponse> forgetPasswordResponseResult) {
                try {
//                    if (forgetPasswordResponseResult instanceof Result.Success) {
//                        ForgetPasswordResponse data = ((Result.Success<ForgetPasswordResponse>) forgetPasswordResponseResult).getData();
//                        observeForgetPassword.setValue(new FinalResult((ForgetPasswordResponse) data));
//                    } else if (forgetPasswordResponseResult instanceof Result.Error) {
//                        LoginError data = ((Result.Error<LoginError>) forgetPasswordResponseResult).getError();
//                        //observeForgetPassword.setValue(new FinalResult(R.string.forget_pwd_failed));
//                        observeForgetPassword.setValue(new FinalResult(data.getErr()));
//                    }

                    if (forgetPasswordResponseResult instanceof Result.Success) {
                        if (!isCalledForgotPwdOnce) {
                            Log.e("userview_model", "success onChanged: " + ((Result.Success<?>) forgetPasswordResponseResult).getData());
                            observeForgetPassword.postValue(new FinalResult((ForgetPasswordResponse) ((Result.Success<?>) forgetPasswordResponseResult).getData()));
                        }
                    } else {
                        if (!isCalledForgotPwdOnce) {
                            ForgetPasswordError data = ((Result.Error<ForgetPasswordError>) forgetPasswordResponseResult).getError();
                            Log.e("userview_model", "failed onChanged: " + data.getMailError());
                            observeForgetPassword.postValue(new FinalResult((ForgetPasswordError) data));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void doLogout() {
        userRepository.removeCurrentUser();
        logoutNavigation.postValue(true);
    }

    public void getNotifications() {
        LoginData profileDetails = getProfileDetails();
        int inspector_id = profileDetails.getInspector_id();
        NotificationRequest notificationRequest = new NotificationRequest();
        notificationRequest.setInspector_id(inspector_id + "");

        userRepository.getNotifications(notificationRequest);
        userRepository.getObserveNotifications().observeForever(new Observer<Result<NotificationResponse>>() {
            @Override
            public void onChanged(Result<NotificationResponse> notificationResponseResult) {
                try {
                    if (notificationResponseResult instanceof Result.Success) {
                        NotificationResponse data = ((Result.Success<NotificationResponse>) notificationResponseResult).getData();
                        observeNotifications.postValue(new FinalResult((NotificationResponse) data));
                    } else {
                        observeNotifications.postValue(new FinalResult("Failed"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteNotification(String notificationId) {
        userRepository.deleteNotification(notificationId);
        userRepository.getObserveDeleteNotification().observeForever(new Observer<Result<DeleteNotificationResponse>>() {
            @Override
            public void onChanged(Result<DeleteNotificationResponse> deleteNotificationResponseResult) {
                try {
                    if (deleteNotificationResponseResult instanceof Result.Success) {
                        DeleteNotificationResponse data = ((Result.Success<DeleteNotificationResponse>) deleteNotificationResponseResult).getData();
                        observeDeleteNotifications.setValue(new FinalResult((DeleteNotificationResponse) data));
                        Log.e("TAG", "Result.Success");
                    } else {
                        observeDeleteNotifications.setValue(new FinalResult("Failed"));
                        Log.e("TAG", "Result.Failure");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void deleteAllNotification() {
        LoginData profileDetails = getProfileDetails();
        int inspector_id = profileDetails.getInspector_id();

        userRepository.deleteAllNotification(inspector_id);
        userRepository.getObserveDeleteAllNotification().observeForever(new Observer<Result<DeleteAllNotificationResponse>>() {
            @Override
            public void onChanged(Result<DeleteAllNotificationResponse> deleteAllNotificationResponseResult) {
                try {
                    if (deleteAllNotificationResponseResult instanceof Result.Success) {
                        DeleteAllNotificationResponse data = ((Result.Success<DeleteAllNotificationResponse>) deleteAllNotificationResponseResult).getData();
                        observeDeleteAllNotifications.setValue(new FinalResult((DeleteAllNotificationResponse) data));
                    } else {
                        observeDeleteAllNotifications.setValue(new FinalResult("Failed"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
package com.folkus.data.repository;

import static com.folkus.ui.login.UserViewModel.isCalledForgotPwdOnce;
import static com.folkus.ui.login.UserViewModel.isCalledLoginApiOnce;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.folkus.data.Result;
import com.folkus.data.local.prefs.UserPreferences;
import com.folkus.data.model.User;
import com.folkus.data.remote.NetworkService;
import com.folkus.data.remote.request.ChangePasswordRequest;
import com.folkus.data.remote.request.CountRequest;
import com.folkus.data.remote.request.DeleteAllNotificationRequest;
import com.folkus.data.remote.request.DeleteNotificationRequest;
import com.folkus.data.remote.request.ForgetPasswordRequest;
import com.folkus.data.remote.request.LoginRequest;
import com.folkus.data.remote.request.NotificationRequest;
import com.folkus.data.remote.response.ChangePasswordResponse;
import com.folkus.data.remote.response.CountResponse;
import com.folkus.data.remote.response.DeleteAllNotificationResponse;
import com.folkus.data.remote.response.DeleteNotificationResponse;
import com.folkus.data.remote.response.ForgetPasswordResponse;
import com.folkus.data.remote.response.LoginData;
import com.folkus.data.remote.response.LoginResponse;
import com.folkus.data.remote.response.NotificationResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class UserRepository {

    private static volatile UserRepository instance;
    private MutableLiveData<Result<LoginResponse>> loginResponse = new MutableLiveData<>();
    private MutableLiveData<Result<CountResponse>> observeCountData = new MutableLiveData<>();
    private MutableLiveData<Result<ChangePasswordResponse>> observeChangePassword = new MutableLiveData<>();
    private MutableLiveData<Result<ForgetPasswordResponse>> observeForgetPassword = new MutableLiveData<>();
    private MutableLiveData<Result<NotificationResponse>> observeNotifications = new MutableLiveData<>();
    private MutableLiveData<Result<DeleteNotificationResponse>> observeDeleteNotification = new MutableLiveData<>();
    private MutableLiveData<Result<DeleteAllNotificationResponse>> observeDeleteAllNotification = new MutableLiveData<>();
    private NetworkService networkService;
    private UserPreferences userPreferences;

    private Result user = null;

    private UserRepository(NetworkService networkService, UserPreferences userPreferences) {
        this.networkService = networkService;
        this.userPreferences = userPreferences;
    }

    public static UserRepository getInstance(NetworkService networkService, UserPreferences userPreferences) {
        if (instance == null) {
            instance = new UserRepository(networkService, userPreferences);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
    }

    private void setLoggedInUser(Result user) {
        this.user = user;
    }

    public MutableLiveData<Result<LoginResponse>> getLoginResponse() {
        return loginResponse;
    }

    public MutableLiveData<Result<CountResponse>> observeCountData() {
        return observeCountData;
    }

    public MutableLiveData<Result<ChangePasswordResponse>> observeChangePassword() {
        return observeChangePassword;
    }

    public MutableLiveData<Result<ForgetPasswordResponse>> observeForgetPassword() {
        return observeForgetPassword;
    }

    public MutableLiveData<Result<NotificationResponse>> getObserveNotifications() {
        return observeNotifications;
    }

    public MutableLiveData<Result<DeleteNotificationResponse>> getObserveDeleteNotification() {
        return observeDeleteNotification;
    }

    public MutableLiveData<Result<DeleteAllNotificationResponse>> getObserveDeleteAllNotification() {
        return observeDeleteAllNotification;
    }

    public void saveCurrentUser(LoginData loginData) {
        int inspector_id = loginData.getInspector_id();
        String name = loginData.getName();
        String email = loginData.getEmail();
        userPreferences.setUserId(inspector_id);
        userPreferences.setUserName(name);
        userPreferences.setUserEmail(email);
    }

    public void setProfileDetails(LoginData loginData) {
        userPreferences.setProfileDetails(loginData);
    }

    public LoginData getProfileDetails() {
        return userPreferences.getProfileDetails();
    }

    public void removeCurrentUser() {
        userPreferences.removeUserId();
        userPreferences.removeUserName();
        userPreferences.removeUserEmail();
        userPreferences.removeAccessToken();
        userPreferences.removeProfileDetails();
    }

    public User getCurrentUser() {
        int userId = userPreferences.getUserId();
        String userName = userPreferences.getUserName();
        String userEmail = userPreferences.getUserEmail();
        String accessToken = userPreferences.getAccessToken();

        if (userName != null && userEmail != null) {
            User user = new User();
            user.setUserId(userId);
            user.setUserName(userName);
            user.setUserEmail(userEmail);
            return user;
        } else {
            return null;
        }
    }

    public void login(String username, String password) {
        // handle login
        LoginRequest request = new LoginRequest();
        request.setEmail(username);
        request.setPassword(password);

        Result result = null;
/*
        networkService.doLoginCall(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse body = response.body();
                boolean success = body.getSuccess();
                if (success) {
                    Result result = new Result.Success<>(body);
                    setLoggedInUser(result);
                    loginResponse.postValue(result);
                } else {
                    String err = body.getError().getErr();
                    Result result = new Result.Error(body.getError());
                    setLoggedInUser(result);
                    loginResponse.postValue(result);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                LoginResponse body = new LoginResponse();
                LoginError loginError = new LoginError();
                loginError.setErr("Error logging in");
                body.setError(loginError);

                Result result = new Result.Error(new IOException("Error logging in"));
                setLoggedInUser(result);
                loginResponse.postValue(result);
                Log.e("TAG", "onResponse: " + "Error logging in");
            }
        });
*/

        networkService.doLoginCall(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().success) {
                        Result result = new Result.Success<>(response.body());
                        setLoggedInUser(result);
                        loginResponse.postValue(result);
                        isCalledLoginApiOnce = false;
                        Log.e("UserRepository", "onResponse: " + response.body().getData().getName());
                    } else {
                        Result result = new Result.Error<>(response.body().getError());
                        setLoggedInUser(result);
                        loginResponse.postValue(result);
                        isCalledLoginApiOnce = false;
                        Log.e("UserRepository", "onResponse: " + response.body().getError().getErr());
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                isCalledLoginApiOnce = false;
                Log.e("UserRepository", "onResponse: " + call.timeout());
            }
        });
    }

    public void getCountData() {
        CountRequest countRequest = new CountRequest();
        countRequest.setInspectorId(1);
        networkService.getCountData(countRequest).enqueue(new Callback<CountResponse>() {
            @Override
            public void onResponse(Call<CountResponse> call, Response<CountResponse> response) {
                CountResponse body = response.body();
                Result result = new Result.Success<>(body);
                observeCountData.postValue(result);
            }

            @Override
            public void onFailure(Call<CountResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Failed to get count"));
                observeCountData.postValue(result);
            }
        });
    }

    public void doChangePassword(int inspectorId, String oldPassword, String newPassword) {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setInspectorId(inspectorId);
        changePasswordRequest.setNewpassword(newPassword);
        changePasswordRequest.setOldpassword(oldPassword);
        networkService.doChangePasswordCall(changePasswordRequest).enqueue(new Callback<ChangePasswordResponse>() {
            @Override
            public void onResponse(Call<ChangePasswordResponse> call, Response<ChangePasswordResponse> response) {
                ChangePasswordResponse body = response.body();
                Result result = new Result.Success<>(body);
                observeChangePassword.postValue(result);
            }

            @Override
            public void onFailure(Call<ChangePasswordResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Failed to get count"));
                observeChangePassword.postValue(result);
            }
        });
    }

    public void doForgetPassword(String emailId) {
        ForgetPasswordRequest forgetPasswordRequest = new ForgetPasswordRequest();
        forgetPasswordRequest.setEmail(emailId);
        networkService.doForgotPasswordCall(forgetPasswordRequest).enqueue(new Callback<ForgetPasswordResponse>() {
            @Override
            public void onResponse(Call<ForgetPasswordResponse> call, Response<ForgetPasswordResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().success) {
                            Result result = new Result.Success<>(response.body());
                            observeForgetPassword.postValue(result);
                            isCalledForgotPwdOnce = false;
                            Log.e("UserRepository", "observeForgetPassword :onResponse " + response.body().getData());
                        } else {
                            Result result = new Result.Error<>(response.body().getForgetPasswordError());
                            observeForgetPassword.postValue(result);
                            isCalledForgotPwdOnce = false;
                            Log.e("UserRepository", "observeForgetPassword :onResponse " + response.body().forgetPasswordError.getMailError());
                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<ForgetPasswordResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Failed"));
                observeForgetPassword.postValue(result);
                isCalledForgotPwdOnce = false;
            }
        });

    }

    public void getNotifications(NotificationRequest notificationRequest) {
        networkService.getNotificationDetails(notificationRequest).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                NotificationResponse body = response.body();
                Result result = new Result.Success<>(body);
                observeNotifications.postValue(result);
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Failed"));
                observeNotifications.postValue(result);
            }
        });
    }

    public void deleteNotification(String notificationId) {
        DeleteNotificationRequest deleteNotificationRequest = new DeleteNotificationRequest();
        deleteNotificationRequest.setNotification_id(notificationId);
        networkService.deleteNotification(deleteNotificationRequest).enqueue(new Callback<DeleteNotificationResponse>() {
            @Override
            public void onResponse(Call<DeleteNotificationResponse> call, Response<DeleteNotificationResponse> response) {
                DeleteNotificationResponse body = response.body();
                Result result = new Result.Success<>(body);
                observeDeleteNotification.postValue(result);
                Log.e("TAG", "onResponse: success");
            }

            @Override
            public void onFailure(Call<DeleteNotificationResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Failed"));
                observeDeleteNotification.postValue(result);
                Log.e("TAG", "onResponse: Failed");
            }
        });
    }

    public void deleteAllNotification(int inspectorId) {
        DeleteAllNotificationRequest deleteNotificationRequest = new DeleteAllNotificationRequest();
        deleteNotificationRequest.setInspector_id(inspectorId + "");

        networkService.deleteAllNotification(deleteNotificationRequest).enqueue(new Callback<DeleteAllNotificationResponse>() {
            @Override
            public void onResponse(Call<DeleteAllNotificationResponse> call, Response<DeleteAllNotificationResponse> response) {
                DeleteAllNotificationResponse body = response.body();
                Result result = new Result.Success<>(body);
                observeDeleteAllNotification.postValue(result);
            }

            @Override
            public void onFailure(Call<DeleteAllNotificationResponse> call, Throwable t) {
                Result result = new Result.Error(new IOException("Failed"));
                observeDeleteAllNotification.postValue(result);
            }
        });
    }

}
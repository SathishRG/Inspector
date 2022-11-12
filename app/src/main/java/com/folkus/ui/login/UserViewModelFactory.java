package com.folkus.ui.login;

import android.content.Context;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.annotation.NonNull;

import com.folkus.data.local.prefs.UserPreferences;
import com.folkus.data.repository.UserRepository;
import com.folkus.data.remote.NetworkService;
import com.folkus.data.remote.Networking;

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
public class UserViewModelFactory implements ViewModelProvider.Factory {
    private NetworkService newsApi;
    private UserPreferences userPreferences;
    private  Context context;
    public UserViewModelFactory(Context applicationContext) {
        context = applicationContext;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(UserViewModel.class)) {
            newsApi = Networking.cteateService(NetworkService.class);
            userPreferences = new UserPreferences(context);
            return (T) new UserViewModel(UserRepository.getInstance(newsApi,userPreferences));
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
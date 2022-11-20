package com.folkus.ui.login;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.data.local.prefs.UserPreferences;
import com.folkus.data.remote.NetworkService;
import com.folkus.data.remote.Networking;
import com.folkus.data.repository.InspectionRepository;
import com.folkus.data.repository.UserRepository;


public class InspectionViewModelFactory implements ViewModelProvider.Factory {
    private NetworkService newsApi;
    private UserPreferences userPreferences;
    private Context context;
    public InspectionViewModelFactory(Context applicationContext) {
        context = applicationContext;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(InspectionRequestViewModel.class)) {
            newsApi = Networking.cteateService(NetworkService.class);
            userPreferences = new UserPreferences(context);
            UserRepository userRepository = UserRepository.getInstance(newsApi, userPreferences);
            InspectionRepository inspectionRepository = InspectionRepository.getInstance(newsApi, userPreferences,userRepository);

            return (T) new InspectionRequestViewModel(inspectionRepository,userRepository);
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
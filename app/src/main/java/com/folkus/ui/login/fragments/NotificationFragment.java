package com.folkus.ui.login.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.folkus.R;
import com.folkus.data.remote.response.DeleteAllNotificationResponse;
import com.folkus.data.remote.response.DeleteNotificationResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.LoginResponse;
import com.folkus.data.remote.response.NotificationData;
import com.folkus.data.remote.response.NotificationResponse;
import com.folkus.databinding.FragmentAboutUsBinding;
import com.folkus.databinding.NotificationFragmentBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;
import com.folkus.ui.login.adapter.AdapterInspectionRequest;
import com.folkus.ui.login.adapter.AdapterNotification;
import com.folkus.ui.login.model.FinalResult;

import java.util.ArrayList;

public class NotificationFragment extends Fragment {

    private NotificationFragmentBinding binding;
    private UserViewModel userViewModel;
    ArrayList<NotificationData> data = new ArrayList<>();
    private AdapterNotification mAdapter;
    public static int position;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = NotificationFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(requireActivity())).get(UserViewModel.class);
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.title_notification));
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvNotification.setLayoutManager(mLayoutManager);

        mAdapter = new AdapterNotification(data,userViewModel);
        binding.rvNotification.setAdapter(mAdapter);

        binding.notificationNoRecord.setVisibility(View.GONE);
        binding.progressBarNotification.setVisibility(View.VISIBLE);
        userViewModel.getNotifications();
        userViewModel.observeNotifications.observe(requireActivity(), new Observer<FinalResult>() {
            @Override
            public void onChanged(FinalResult finalResult) {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (finalResult.getError() != null) {
                    binding.notificationNoRecord.setVisibility(View.VISIBLE);
                    binding.progressBarNotification.setVisibility(View.GONE);
                }
                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof NotificationResponse) {
                        NotificationResponse notificationResponse = (NotificationResponse) success1;
                        boolean success = notificationResponse.isSuccess();
                        if (success) {
                            binding.notificationNoRecord.setVisibility(View.GONE);
                            binding.progressBarNotification.setVisibility(View.GONE);
                            data = notificationResponse.getData();
                            if(data.size() > 0) {
                                mAdapter.setValue(data);
                            }else {
                                binding.notificationNoRecord.setVisibility(View.VISIBLE);
                            }
                        } else {
                            binding.notificationNoRecord.setVisibility(View.VISIBLE);
                            binding.progressBarNotification.setVisibility(View.GONE);
                        }

                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        binding.notificationNoRecord.setVisibility(View.VISIBLE);
                        binding.progressBarNotification.setVisibility(View.GONE);
                    }
                }
            }
        });

        userViewModel.getObserveDeleteNotifications().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof DeleteNotificationResponse) {
                    DeleteNotificationResponse notificationResponse = (DeleteNotificationResponse) success1;
                    boolean success = notificationResponse.isSuccess();
                    if(success){
                        Log.e("TAG", "onChanged: "+position );
                        if(position < data.size()) {
                            data.remove(position);
                            mAdapter.notifyDataSetChanged();
                        }
                        Toast.makeText(requireActivity(), notificationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.txtClearNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userViewModel.deleteAllNotification();
            }
        });

        userViewModel.getObserveDeleteAllNotifications().observe(requireActivity(), new Observer<FinalResult>() {
            @Override
            public void onChanged(FinalResult finalResult) {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (finalResult.getError() != null) {
                    Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof DeleteAllNotificationResponse) {
                        DeleteAllNotificationResponse notificationResponse = (DeleteAllNotificationResponse) success1;
                        boolean success = notificationResponse.isSuccess();
                        if(success){
                            Log.e("TAG", "onChanged: "+position );
                            if(position < data.size()) {
                                data.clear();
                                mAdapter.notifyDataSetChanged();
                                binding.notificationNoRecord.setVisibility(View.VISIBLE);
                            }
                            Toast.makeText(requireActivity(), notificationResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        userViewModel.observeNotifications.removeObservers(requireActivity());
        userViewModel.getObserveDeleteNotifications().removeObservers(requireActivity());
        userViewModel.getObserveDeleteAllNotifications().removeObservers(requireActivity());
    }
}

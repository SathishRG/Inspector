package com.folkus.ui.login.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import static com.folkus.ui.login.ActivityHome.navController;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.folkus.R;
import com.folkus.data.remote.response.InspectionRequestData;
import com.folkus.data.remote.response.InspectionRequestResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.LoginResponse;
import com.folkus.databinding.InspectionRequestFragmentBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;
import com.folkus.ui.login.adapter.AdapterInspectionRequest;
import com.folkus.ui.login.fragments.dialog.DialogAddInspectorSuccess;
import com.folkus.ui.login.model.FinalResult;

import java.util.ArrayList;

public class InspectionRequestFragment extends Fragment {

    private InspectionRequestFragmentBinding binding;
    private InspectionRequestViewModel inspectionRequestViewModel;
    private ArrayList<InspectionRequestData> arrayList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = InspectionRequestFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inspectionRequestViewModel = new ViewModelProvider(requireActivity(), new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.title_inspection_request));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvInspectionRequest.setLayoutManager(mLayoutManager);

        binding.inspectionRequestNoRecord.setVisibility(View.GONE);
        binding.progressBarInspectionRequest.setVisibility(View.VISIBLE);
        inspectionRequestViewModel.inspectionRequest();
        inspectionRequestViewModel.getInspectionRequestResult().observe(requireActivity(), new Observer<FinalResult>() {
            @Override
            public void onChanged(@Nullable FinalResult loginResult) {
                if (loginResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (loginResult.getError() != null) {
                    Integer error = (Integer) loginResult.getError();
                    binding.progressBarInspectionRequest.setVisibility(View.GONE);
                    binding.inspectionRequestNoRecord.setText(error);
                    showMsg(error);
                }

                Object success1 = loginResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof InspectionRequestResponse) {
                        InspectionRequestResponse inspectionRequestResponse = (InspectionRequestResponse) success1;
                        boolean success = inspectionRequestResponse.isSuccess();
                        if (success) {
                            ArrayList<InspectionRequestData> data = inspectionRequestResponse.getData();
                            AdapterInspectionRequest mAdapter = new AdapterInspectionRequest(data, InspectionRequestFragment.this);
                            if (data.size() > 0) {
                                binding.progressBarInspectionRequest.setVisibility(View.GONE);
                                binding.inspectionRequestNoRecord.setVisibility(View.GONE);
                                binding.rvInspectionRequest.setAdapter(mAdapter);
                            } else {
                                binding.progressBarInspectionRequest.setVisibility(View.GONE);
                                binding.inspectionRequestNoRecord.setVisibility(View.VISIBLE);
                                binding.rvInspectionRequest.setAdapter(mAdapter);
                            }
                        } else {
                            String err = inspectionRequestResponse.getError().getErr();
                            showMsg(err);
                            binding.progressBarInspectionRequest.setVisibility(View.GONE);
                            binding.inspectionRequestNoRecord.setText(err);
                        }
                    } else if (success1 instanceof LoginError) {
                        binding.progressBarInspectionRequest.setVisibility(View.GONE);
                        LoginError loginError = (LoginError) success1;
                        showMsg(loginError.getErr());
                        binding.inspectionRequestNoRecord.setText(loginError.getErr() + "");
                    }
                }
            }
        });
    }

    private void showMsg(@StringRes Integer errorString) {
        Toast.makeText(requireActivity(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showMsg(String errorString) {
        Toast.makeText(requireActivity(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void navToSellerDetailsPage(InspectionRequestData inspectionRequestData) {
        inspectionRequestViewModel.setInspectionRequestData(inspectionRequestData);
        navController.navigate(R.id.action_request_to_seller_details);
//        navController.navigate(R.id.action_request_to_seller_details);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        inspectionRequestViewModel.getInspectionRequestResult().removeObservers(requireActivity());
    }
}

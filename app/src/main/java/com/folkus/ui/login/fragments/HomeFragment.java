package com.folkus.ui.login.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import static com.folkus.ui.login.Constant.position;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.data.remote.response.CountData;
import com.folkus.data.remote.response.CountResponse;
import com.folkus.databinding.FragmentHomeBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.Constant;
import com.folkus.ui.login.NetworkChangeReceiver;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    private UserViewModel userViewModel;
    private FragmentHomeBinding binding;
    private TextView txtCompletedCount,
            tvPendingCount,
            tvMonthCompletedCount,
            tvReOpenCount,
            txtInspectionRequest,
            txtInspection,
            btnAddVehicle;
    private ProgressBar progressBarCompletedCount,
            progressBarPendingCount,
            progressBarTotalCount,
            progressBarReOpenCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
//        requireActivity().registerReceiver(receiver, filter);
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(getContext().getApplicationContext())).get(UserViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        txtCompletedCount = binding.txtCompletedCount;
        tvPendingCount = binding.tvPendingCount;
        tvMonthCompletedCount = binding.tvMonthCompletedCount;
        tvReOpenCount = binding.tvReOpenCount;

        txtInspectionRequest = binding.txtInspectionRequest;
        txtInspection = binding.txtInspection;
        btnAddVehicle = binding.btnAddVehicle;

        progressBarCompletedCount = binding.progressBarCompletedCount;
        progressBarPendingCount = binding.progressBarPendingCount;
        progressBarTotalCount = binding.progressBarTotalCount;
        progressBarReOpenCount = binding.progressBarReOpenCount;

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userViewModel.getCountData();
        userViewModel.getObserveCountData().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                int error = (Integer) finalResult.getError();
                setProgressVisibilityGone();
            }
            if (finalResult.getSuccess() != null) {
                CountResponse success = (CountResponse) finalResult.getSuccess();
                ArrayList<CountData> data = success.getData();
                int totalcompleted = data.get(0).getTotalcompleted();
                int totalpending = data.get(0).getTotalpending();
                int thismonthcompleted = data.get(0).getThismonthcompleted();
                int totalreopen = data.get(0).getTotalreopen();

                txtCompletedCount.setText(totalcompleted + "");
                tvPendingCount.setText(totalpending + "");
                tvMonthCompletedCount.setText(thismonthcompleted + "");
                tvReOpenCount.setText(totalreopen + "");

                setTextVisibilityVisible();
                setProgressVisibilityGone();
            }
        });


        binding.cvCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 0;
                showInspection();
            }
        });
        binding.cvPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 1;
                showInspection();
            }
        });
        binding.cvMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 2;
                showInspection();
            }
        });

        binding.cvReOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 3;
                showInspection();
            }
        });
        txtInspectionRequest.setOnClickListener(v -> showInspectionRequest());

        txtInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = 1;
                showInspection();
            }
        });

        btnAddVehicle.setOnClickListener(v -> showAddVehiclePage());

        ((ActivityHome) requireActivity()).createMenu();

        if (view.isFocused()) {
            hiddenKeyboard(view);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.menu_home));
    }

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void setTextVisibilityVisible() {
        txtCompletedCount.setVisibility(View.VISIBLE);
        tvPendingCount.setVisibility(View.VISIBLE);
        tvMonthCompletedCount.setVisibility(View.VISIBLE);
        tvReOpenCount.setVisibility(View.VISIBLE);
    }

    private void setProgressVisibilityGone() {
        progressBarCompletedCount.setVisibility(View.GONE);
        progressBarPendingCount.setVisibility(View.GONE);
        progressBarTotalCount.setVisibility(View.GONE);
        progressBarReOpenCount.setVisibility(View.GONE);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void showInspectionRequest() {
        findNavController(HomeFragment.this).navigate(R.id.nav_inspection_request);
    }

    private void showInspection() {
        findNavController(HomeFragment.this).navigate(R.id.nav_inspection_tab);
    }

    private void showAddVehiclePage() {
        findNavController(HomeFragment.this).navigate(R.id.nav_add_vehicle);
    }
}
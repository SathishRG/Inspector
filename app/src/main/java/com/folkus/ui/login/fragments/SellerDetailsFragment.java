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
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.folkus.R;
import com.folkus.data.remote.response.AcceptResponse;
import com.folkus.data.remote.response.InspectionRequestData;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.SellerDetails;
import com.folkus.data.remote.response.SellerDetailsResponse;
import com.folkus.databinding.SellerDetailsFragmentBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.folkus.ui.login.adapter.AdapterSellerInfoDetails;
import com.folkus.ui.login.fragments.dialog.DialogAddInspectorSuccess;
import com.folkus.ui.login.fragments.dialog.DialogRejectionReason;

import java.util.ArrayList;

public class SellerDetailsFragment extends Fragment {

    private SellerDetailsFragmentBinding binding;
    private InspectionRequestViewModel inspectionRequestViewModel;
    public static boolean isCalledOnceSellerApi = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = SellerDetailsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inspectionRequestViewModel = new ViewModelProvider(requireActivity(), new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.inspection_seller_details));
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InspectionRequestData inspectionRequestData = inspectionRequestViewModel.getInspectionRequestData();

        String address = inspectionRequestData.getAddress();
        String dealer_name = inspectionRequestData.getDealer_name();
        int no_cars = inspectionRequestData.getNo_cars();
        String phone_no = inspectionRequestData.getPhone_no();
        int seller_info_id = inspectionRequestData.getSeller_info_id();

        binding.tvSellerIdValue.setText(seller_info_id + "");
        binding.tvInspectionCustomerName.setText(dealer_name);
        binding.tvInspectionPhone.setText(phone_no);
        binding.tvInspectionLocation.setText(address);
        binding.tvInspectionVehicleModel.setText(no_cars + "");

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerView.setLayoutManager(mLayoutManager);

        inspectionRequestViewModel.getInspectionRequestSellerDetail(seller_info_id);
        inspectionRequestViewModel.getSellerDetailsResponseResult().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                Log.e("TAG", "onChanged: " + error);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof SellerDetailsResponse) {
                    SellerDetailsResponse sellerDetailsResponse = (SellerDetailsResponse) success1;
                    boolean success = sellerDetailsResponse.isSuccess();
                    if (success) {
                        ArrayList<SellerDetails> data = sellerDetailsResponse.getData();
                        AdapterSellerInfoDetails mAdapter = new AdapterSellerInfoDetails(data);
                        binding.recyclerView.setAdapter(mAdapter);
                    } else {
                        Toast.makeText(requireActivity(), sellerDetailsResponse.getError().getErr(), Toast.LENGTH_LONG).show();
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    Log.e("TAG", "onChanged: " + loginError.getErr());
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCalledOnceSellerApi = false;
                inspectionRequestViewModel.acceptRequest(seller_info_id);
                // showAcceptDialog();
            }
        });

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRejectDialog(seller_info_id);
            }
        });

        if (!isCalledOnceSellerApi) {
            inspectionRequestViewModel.getAcceptResponseResult().observe(requireActivity(), finalResult -> {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    Log.e("TAG", "onChanged: " + error);
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof AcceptResponse) {
                        AcceptResponse sellerDetailsResponse = (AcceptResponse) success1;
                        boolean success = sellerDetailsResponse.isSuccess();
                        if (success) {
                            showAcceptDialog();
                        } else {
                            Toast.makeText(requireActivity(), sellerDetailsResponse.getError().getErr(), Toast.LENGTH_LONG).show();
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        Log.e("TAG", "onChanged: " + loginError.getErr());
                        Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        ((ActivityHome) requireActivity()).createMenu();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        inspectionRequestViewModel.getSellerDetailsResponseResult().removeObservers(requireActivity());
        inspectionRequestViewModel.getAcceptResponseResult().removeObservers(requireActivity());

        inspectionRequestViewModel.getRejectResponseResult().removeObservers(requireActivity());
        inspectionRequestViewModel.getRejectReasonDropDownResult().removeObservers(requireActivity());
    }

    private void showAcceptDialog() {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        DialogAddInspectorSuccess editNameDialogFragment = DialogAddInspectorSuccess.newInstance(getString(R.string.seller_accepted));
        editNameDialogFragment.show(fm, "fragment_accept");
        editNameDialogFragment.setCancelable(false);
    }

    private void showRejectDialog(int seller_info_id) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        DialogRejectionReason editNameDialogFragment = DialogRejectionReason.newInstance(seller_info_id);
        editNameDialogFragment.show(fm, "fragment_reject");
        editNameDialogFragment.setCancelable(false);
    }
}

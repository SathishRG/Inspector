package com.folkus.ui.login.fragments.dialog;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.Constant.position;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;

import com.folkus.R;
import com.folkus.comman.ProgressButton;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.RejectReasonDropDownData;
import com.folkus.data.remote.response.RejectReasonDropDownResponse;
import com.folkus.data.remote.response.RejectResponse;
import com.folkus.data.remote.response.SellerDetails;
import com.folkus.data.remote.response.SellerDetailsResponse;
import com.folkus.databinding.DialogRejectionReasonBinding;
import com.folkus.ui.login.ActivityAddInspector;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;
import com.folkus.ui.login.adapter.AdapterSellerInfoDetails;

import java.util.ArrayList;

public class DialogRejectionReason extends DialogFragment {
    private AppCompatSpinner dtfRejectReason;
    private EditText edComments;
    private InspectionRequestViewModel inspectionRequestViewModel;
    private ArrayList<RejectReasonDropDownData> data;
    private int rejectId;
    private static int sellerInfoId;

    public DialogRejectionReason() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogRejectionReason newInstance(int seller_info_id) {
        sellerInfoId = seller_info_id;
        DialogRejectionReason frag = new DialogRejectionReason();
        return frag;
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
        inspectionRequestViewModel = new ViewModelProvider(requireActivity(), new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return inflater.inflate(R.layout.dialog_rejection_reason, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dtfRejectReason = (AppCompatSpinner) view.findViewById(R.id.dtfRejectReason);
        edComments = (EditText) view.findViewById(R.id.edComments);
        ConstraintLayout btnSubmit = (ConstraintLayout) view.findViewById(R.id.constraint_layout);
        AppCompatButton btnCancelChangePwd = (AppCompatButton) view.findViewById(R.id.btnCancelChangePwd);
        ProgressButton progressButton = new ProgressButton(requireContext(), btnSubmit);

        inspectionRequestViewModel.getRejectReasonDropDown();
        inspectionRequestViewModel.getRejectReasonDropDownResult().observe(requireActivity(), finalResult -> {
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
                if (success1 instanceof RejectReasonDropDownResponse) {
                    RejectReasonDropDownResponse sellerDetailsResponse = (RejectReasonDropDownResponse) success1;
                    boolean success = sellerDetailsResponse.isSuccess();
                    if (success) {
                        data = sellerDetailsResponse.getData();
                        ArrayList<String> strings = new ArrayList<>();
                        data.forEach(stateData -> {
                            strings.add(stateData.getRejectreason());
                        });
                        ArrayAdapter<String> adapter = new ArrayAdapter(requireActivity(), R.layout.item_spinner, strings);
                        dtfRejectReason.setAdapter(adapter);

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

        inspectionRequestViewModel.getRejectResponseResult().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                Log.e("TAG", "onChanged: " + error);
                dismiss();
                Toast.makeText(requireActivity(), "Error", Toast.LENGTH_LONG).show();
                naviagetToInspectionTab();
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof RejectResponse) {
                    RejectResponse rejectResponse = (RejectResponse) success1;
                    boolean success = rejectResponse.isSuccess();
                    if (success) {
                        dismiss();
                        Toast.makeText(requireActivity(), rejectResponse.getMessage(), Toast.LENGTH_LONG).show();
                        naviagetToInspectionTab();
                    } else {
                        dismiss();
                        Toast.makeText(requireActivity(), rejectResponse.getError().getErr(), Toast.LENGTH_LONG).show();
                        naviagetToInspectionTab();
                    }
                } else if (success1 instanceof LoginError) {
                    dismiss();
                    LoginError loginError = (LoginError) success1;
                    Log.e("TAG", "onChanged: " + loginError.getErr());
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                    naviagetToInspectionTab();
                }
            }
        });


        dtfRejectReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG", "onItemSelected: " + i);
                rejectId = data.get(i).getReject_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = edComments.getText().toString();
                inspectionRequestViewModel.rejectRequest(sellerInfoId, rejectId, text);
            }
        });

        btnCancelChangePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    private void naviagetToInspectionTab() {
        position = 1;
        navController.navigate(R.id.action_sellerInfoFragment_to_nav_inspection_tab2);
//        findNavController(DialogRejectionReason.this).
//                navigate(R.id.action_sellerInfoFragment_to_nav_inspection_tab2,null, new NavOptions.Builder().
//                        setPopUpTo(findNavController(DialogRejectionReason.this)
//                                .getGraph().getStartDestinationId(), true).build());
    }

    private void hideProgress(ProgressButton progressButton) {
        progressButton.buttonReverted();
        progressButton.buttonFinished();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        inspectionRequestViewModel.getRejectResponseResult().removeObservers(requireActivity());
        inspectionRequestViewModel.getRejectReasonDropDownResult().removeObservers(requireActivity());
    }
}

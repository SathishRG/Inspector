package com.folkus.ui.login.fragments.inspection;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.completedInspection;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.carFront;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.driverSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.dsFrontDoor;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.dsFrontPanel;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.dsRearDoor;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.dsRearPanel;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.passengerSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.rearDriverSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.rearPassengerSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.rearSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriage;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriageBack;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriageFront;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriageLeftSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.underCarriageRightSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.upperSide;
import static com.folkus.ui.login.fragments.inspection.InspectionExteriorImageData.vin;
import static com.folkus.ui.login.fragments.inspection.InspectionGeneralInfoFragment.isCalled;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsFrontCarpet;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsFrontSeat;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsRearSeat;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.engineLower;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.odoMeters;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.automaticTransmission_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.differentialOperation_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.dsFrontDoor_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.engineBottomNoise_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.picturePathsList_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.powerComments_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.transferCaseOperation_;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.data.remote.request.InspectionCancelRequest;
import com.folkus.data.remote.response.CancelInspectionResponse;
import com.folkus.data.remote.response.CancelReasonDropDownData;
import com.folkus.data.remote.response.CancelReasonDropDownResponse;
import com.folkus.data.remote.response.CarModelResponse;
import com.folkus.data.remote.response.CompletedInspection;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.InspectionDetailsFragmentBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;

import java.util.ArrayList;

public class InspectionDetailsFragment extends Fragment {
    private InspectionDetailsFragmentBinding binding;
    private LifecycleOwner lifecycleOwner;
    private InspectionRequestViewModel inspectionRequestViewModel;
    private ArrayList<CancelReasonDropDownData> cancelReasonDropDownData = new ArrayList<>();

    private static boolean obdTestChecked = false;
    public CompletedInspection inspection;
    private String inspectionCancelReasonId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = InspectionDetailsFragmentBinding.inflate(inflater, container, false);
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.details));
        lifecycleOwner = this;
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        subscribeUI();
        isCalled = false;
    }

    private void subscribeUI() {
        Bundle b = getArguments();
     /*   if (b != null) {
            completedInspection = (CompletedInspection) getArguments().getSerializable("inspection_details");
            if (completedInspection != null) {
                binding.tvInspectionIdValue.setText("" + completedInspection.getInspector_id());
                binding.tvInspectionCustomerName.setText(completedInspection.getDealer_name());
                binding.tvInspectionPhone.setText(completedInspection.getPhone_no());
                binding.tvInspectionLocation.setText(completedInspection.getLocation());
                binding.tvInspectionVehicleModel.setText(completedInspection.getModel());
                binding.tvInspectionVehicleValue.setText(completedInspection.getMake());
                binding.tvInspectionScheduleValue.setText(completedInspection.getDate());
                binding.tvInspectionTimeSlotValue.setText(completedInspection.getTime());
                binding.progressBarInspectionDetails.setVisibility(View.GONE);
                inspectionRequestViewModel.setCompleteInspection(completedInspection);
            } else {
                completedInspection = inspectionRequestViewModel.getCompleteInspection();
                binding.tvInspectionIdValue.setText("" + completedInspection.getInspector_id());
                binding.tvInspectionCustomerName.setText(completedInspection.getDealer_name());
                binding.tvInspectionPhone.setText(completedInspection.getPhone_no());
                binding.tvInspectionLocation.setText(completedInspection.getLocation());
                binding.tvInspectionVehicleModel.setText(completedInspection.getModel());
                binding.tvInspectionVehicleValue.setText(completedInspection.getMake());
                binding.tvInspectionScheduleValue.setText(completedInspection.getDate());
                binding.tvInspectionTimeSlotValue.setText(completedInspection.getTime());
                binding.progressBarInspectionDetails.setVisibility(View.GONE);
            }
        }
*/
        try {
            inspection = completedInspection;
            if (inspection != null) {
                binding.tvInspectionIdValue.setText("" + inspection.getInspector_id());
                binding.tvInspectionCustomerName.setText(inspection.getDealer_name());
                binding.tvInspectionPhone.setText(inspection.getPhone_no());
                binding.tvInspectionLocation.setText(inspection.getLocation());
                binding.tvInspectionVehicleModel.setText(inspection.getModel());
                binding.tvInspectionVehicleValue.setText(inspection.getMake());
                binding.tvInspectionScheduleValue.setText(inspection.getDate());
                binding.tvInspectionTimeSlotValue.setText(inspection.getTime());
                binding.progressBarInspectionDetails.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        binding.btnInspectionStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (obdTestChecked) {
                    showDialog();
                } else {
                    Toast.makeText(requireContext(), "Enable obd test checkbox", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnInspectionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (obdTestChecked) {
                    showInspectionCancelDialog();
                } else {
                    Toast.makeText(requireContext(), "Enable obd test checkbox", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.inspectionCarCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                obdTestChecked = isChecked;
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.inspection_important_alert, null);
        dialogBuilder.setView(dialogView);

        Button button = (Button) dialogView.findViewById(R.id.start_inspection_ok_btn);
        TextView textView_title = (TextView) dialogView.findViewById(R.id.alert_title);
        TextView textView_message = (TextView) dialogView.findViewById(R.id.alert_message);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("inspection_general_info", completedInspection);
//                setArguments(bundle);
                obdTestChecked = false;
                navController.navigate(R.id.action_inspectionDetailsFragment_to_inspectionGeneralInfoFragment);
                alertDialog.dismiss();
            }
        });
    }

    private void showInspectionCancelDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.inspection_cancel_view, null);
        dialogBuilder.setView(dialogView);

        ProgressBar progressBar = (ProgressBar) dialogView.findViewById(R.id.cancel_content_progressbar);
        Button submitButton = (Button) dialogView.findViewById(R.id.inspection_cancel_submit_btn);
        EditText commentsEditText = (EditText) dialogView.findViewById(R.id.comments);
        ImageView cancel_view = (ImageView) dialogView.findViewById(R.id.cancel_icon);
        AppCompatSpinner appCompatSpinner = (AppCompatSpinner) dialogView.findViewById(R.id.cancel_reason);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        inspectionRequestViewModel.getCancelReasonDropDownData();

        cancel_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.GONE);
                alertDialog.dismiss();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comments = commentsEditText.getText().toString().trim();
                if (!comments.isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    InspectionCancelRequest cancelRequest = new InspectionCancelRequest();
                    cancelRequest.setCarId(String.valueOf(inspection.getCar_id()));
                    cancelRequest.setInsCancelReasonId(inspectionCancelReasonId);
                    cancelRequest.setComments(comments);
                    inspectionRequestViewModel.submitInspectionCancelReason(cancelRequest);
                } else {
                    Toast.makeText(requireContext(), "Please add comments", Toast.LENGTH_SHORT).show();
                }
            }
        });

        inspectionRequestViewModel.getCancelReasonDropDownResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                progressBar.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof CancelReasonDropDownResponse) {
                    CancelReasonDropDownResponse cancelReasonDropDownResponse = (CancelReasonDropDownResponse) success1;
                    boolean success = cancelReasonDropDownResponse.isSuccess();
                    if (success) {
                        cancelReasonDropDownData = cancelReasonDropDownResponse.getData();
                        ArrayList<String> cancelReasons = new ArrayList<>();
                        if (cancelReasonDropDownData.size() > 0) {
                            cancelReasonDropDownData.forEach(cancelReasonDropDownData -> {
                                String insCancelReason = cancelReasonDropDownData.getIns_cancel_reason();
                                cancelReasons.add(insCancelReason);
                            });
                            ArrayAdapter<String> adapter = new ArrayAdapter(requireActivity(), R.layout.item_spinner, cancelReasons);
                            appCompatSpinner.setAdapter(adapter);
                        }
                        progressBar.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });

        inspectionRequestViewModel.getCancelInspectionResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                progressBar.setVisibility(View.GONE);
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                progressBar.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof CancelInspectionResponse) {
                    CancelInspectionResponse cancelInspectionResponse = (CancelInspectionResponse) success1;
                    boolean success = cancelInspectionResponse.isSuccess();
                    progressBar.setVisibility(View.GONE);
                    if (success) {
                        Toast.makeText(requireActivity(), "" + cancelInspectionResponse.getMessage(), Toast.LENGTH_LONG).show();
                        alertDialog.dismiss();
                        navController.popBackStack(R.id.nav_inspection_tab, true);
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }

            appCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e("TAG", "onItemSelected: " + i);
                    inspectionCancelReasonId = cancelReasonDropDownData.get(i).getIns_cancel_reason_id() + "";
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

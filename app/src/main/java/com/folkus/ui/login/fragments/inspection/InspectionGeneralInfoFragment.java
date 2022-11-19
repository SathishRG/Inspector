package com.folkus.ui.login.fragments.inspection;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.InspectionRequestViewModel.completedInspection;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.data.remote.request.GeneralInfoRequest;
import com.folkus.data.remote.response.CarMakersResponseDropDown;
import com.folkus.data.remote.response.CarModelData;
import com.folkus.data.remote.response.CarModelResponse;
import com.folkus.data.remote.response.CatMakersData;
import com.folkus.data.remote.response.CompletedInspection;
import com.folkus.data.remote.response.GeneralInfoData;
import com.folkus.data.remote.response.GeneralInfoResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.FragmentInspectionGeneralInfoBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.folkus.ui.login.fragments.dialog.MonthYearPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class InspectionGeneralInfoFragment extends Fragment {
    private static final String TAG = "InspectionGeneralInfoFragment";
    private FragmentInspectionGeneralInfoBinding binding;
    final Calendar inspectionCalendar = Calendar.getInstance();
    private InspectionRequestViewModel inspectionRequestViewModel;
    private ArrayList<CatMakersData> catMakersData = new ArrayList<>();
    private ArrayList<CarModelData> catModelData = new ArrayList<>();

    private String sellerName = null;
    private String vehileVin = null;
    private String make = null;
    private String vehicleModel = null;
    private String odoMeter = null;
    private String comments = null;
    private String inspectionDate = null;
    private String vehicleYear = null;
    private boolean manually = false;
    private boolean engineStart = false;
    private boolean drivable = false;
    private String makerId;
    private String modelId;
    CompletedInspection inspection;
    private ArrayList<GeneralInfoData> generalInfoData = new ArrayList();
    public static boolean isCalled = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInspectionGeneralInfoBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.title_inspection));
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //  Bundle b = getArguments();
        // if (b != null) {
        try {
            //          if (!isCalled) {
//                    completedInspection = (CompletedInspection) getArguments().getSerializable("inspection_general_info");
//                    if (completedInspection != null) {
//                        binding.vehileVin.setText(completedInspection.getVin_no());
//                    } else {
//                        completedInspection = inspectionRequestViewModel.getCompleteInspection();
//                        binding.vehileVin.setText(completedInspection.getVin_no());
//                    }
            inspection = completedInspection;
            if (inspection != null) {
                binding.vehileVin.setText(inspection.getVin_no() != null ? inspection.getVin_no() : "");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onViewCreated: " + e.getMessage());
        }
        //  }


        binding.generalInfoContinueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sellerName = binding.sellerName.getText().toString().trim();
                vehileVin = binding.vehileVin.getText().toString().trim();
                make = binding.vehicleMake.getSelectedItem().toString();
                vehicleModel = binding.vehicleModel.getSelectedItem().toString();
                odoMeter = binding.odometer.getSelectedItem().toString();
                comments = binding.comments.getText().toString().trim();
                vehicleYear = binding.vehicleYear.getText().toString().trim();
                inspectionDate = binding.inspectionDate.getText().toString().trim();

                if (inspectionDate.isEmpty()) {
                    binding.inspectionDate.setError("Please select inspection date");
                } else if (sellerName.isEmpty()) {
                    binding.sellerName.setError("Please select seller name");
                } else if (vehileVin.isEmpty()) {
                    binding.vehileVin.setError("Please select vehicle vin");
                } else if (vehicleYear.isEmpty()) {
                    binding.vehicleYear.setError("Please enter vehicle year");
                } else if (make == null) {
                    Toast.makeText(requireContext(), "Select vehicle make", Toast.LENGTH_LONG).show();
                } else if (vehicleModel == null) {
                    Toast.makeText(requireContext(), "Select vehicle model", Toast.LENGTH_LONG).show();
                } else {
                    binding.inspectionDate.setError(null);
                    binding.vehicleYear.setError(null);
                    binding.sellerName.setError(null);
                    try {
                        GeneralInfoRequest generalInfoRequest = new GeneralInfoRequest();
                        generalInfoRequest.setCarId(inspection.getCar_id());
                        generalInfoRequest.setSellerDealerId(inspection.getSeller_dealer_id());
                        generalInfoRequest.setInspectorId(inspection.getInspector_id());
                        generalInfoRequest.setInspectionDate(inspectionDate);
                        generalInfoRequest.setYear(vehicleYear);
                        generalInfoRequest.setVinNO(vehileVin);
                        generalInfoRequest.setMake(make);
                        generalInfoRequest.setModel(vehicleModel);
                        generalInfoRequest.setOdometer(odoMeter);
                        generalInfoRequest.setEngineStart("" + engineStart);
                        generalInfoRequest.setDriveable("" + drivable);
                        generalInfoRequest.setComments(comments);
                        isCalled = false;
                        inspectionRequestViewModel.submitGeneralInformation(generalInfoRequest);
                        binding.progressBarGeneralInfo.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (!isCalled) {
            inspectionRequestViewModel.getGeneralInfoResponse().observe(requireActivity(), finalResult -> {
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    binding.progressBarGeneralInfo.setVisibility(View.GONE);
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    Log.e("getGeneralInfoResponse:fragment", "" + error);
                    //  Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                    binding.progressBarGeneralInfo.setVisibility(View.GONE);
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof GeneralInfoResponse) {
                        GeneralInfoResponse generalInfoResponse = (GeneralInfoResponse) success1;
                        boolean success = generalInfoResponse.isSuccess();
                        if (success) {
                            try {
                                // Toast.makeText(requireActivity(),generalInfoResponse.getMessage(), Toast.LENGTH_LONG).show();
                                generalInfoData.add(generalInfoResponse.getData());
                                inspectionRequestViewModel.setGeneralInfo(generalInfoResponse.getData());
                                navController.navigate(R.id.generalInfoToExteriorImages);
                                binding.progressBarGeneralInfo.setVisibility(View.GONE);
                                isCalled = true;
                            } catch (Exception e) {
                                binding.progressBarGeneralInfo.setVisibility(View.GONE);
                                e.printStackTrace();
                            }
                        } else {
                            //  Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                            binding.progressBarGeneralInfo.setVisibility(View.GONE);
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        binding.progressBarGeneralInfo.setVisibility(View.GONE);
                        //Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

        ArrayList<String> odoMeterValues = new ArrayList<>();
        odoMeterValues.add("No");
        odoMeterValues.add("Yes");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(requireActivity(), R.layout.item_spinner, odoMeterValues);
        binding.odometer.setAdapter(adapter);

        binding.progressBarGeneralInfo.setVisibility(View.VISIBLE);
        inspectionRequestViewModel.getCarMakersList();
        inspectionRequestViewModel.getCarMakersDropDown().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarGeneralInfo.setVisibility(View.GONE);
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof CarMakersResponseDropDown) {
                    CarMakersResponseDropDown carMakersResponseDropDown = (CarMakersResponseDropDown) success1;
                    boolean success = carMakersResponseDropDown.isSuccess();
                    if (success) {
                        try {
                            catMakersData = carMakersResponseDropDown.getData();
                            ArrayList<String> customerName = new ArrayList<>();
                            catMakersData.forEach(sellerDealerShip -> {
                                String dealer_name = sellerDealerShip.getCar_name();
                                customerName.add(dealer_name);
                            });
                            ArrayAdapter<String> vehicleMakeAdapter = new ArrayAdapter(requireContext(), R.layout.item_spinner, customerName);
                            binding.vehicleMake.setAdapter(vehicleMakeAdapter);
                            //   binding.progressBarGeneralInfo.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        binding.progressBarGeneralInfo.setVisibility(View.GONE);
                        //  Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                } else if (success1 instanceof LoginError) {
                    try {
                        LoginError loginError = (LoginError) success1;
                        binding.progressBarGeneralInfo.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.vehicleMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG", "onItemSelected: " + i);
                CatMakersData data = catMakersData.get(i);
                makerId = data.getId() + "";
                inspectionRequestViewModel.getCarModelList(makerId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        inspectionRequestViewModel.getCarModelResponse().observe(requireActivity(), finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                binding.progressBarGeneralInfo.setVisibility(View.GONE);
            }
            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof CarModelResponse) {
                    CarModelResponse carModelResponse = (CarModelResponse) success1;
                    boolean success = carModelResponse.isSuccess();
                    if (success) {
                        try {
                            catModelData = carModelResponse.getData();
                            ArrayList<String> customerName = new ArrayList<>();
                            catModelData.forEach(sellerDealerShip -> {
                                String dealer_name = sellerDealerShip.getModel_name();
                                customerName.add(dealer_name);
                            });
                            ArrayAdapter<String> vehicleModelAdapter = new ArrayAdapter(requireActivity(), R.layout.item_spinner, customerName);
                            binding.vehicleModel.setAdapter(vehicleModelAdapter);
                            binding.progressBarGeneralInfo.setVisibility(View.GONE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        //   Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                        binding.progressBarGeneralInfo.setVisibility(View.GONE);
                    }
                } else if (success1 instanceof LoginError) {
                    try {
                        LoginError loginError = (LoginError) success1;
                        binding.progressBarGeneralInfo.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //  Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });


        binding.vehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG", "onItemSelected: " + i);
                if (i > 0) {
                    CarModelData data = catModelData.get(i);
                    modelId = data.getModel_id() + "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.manualSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                manually = b;
            }
        });

        binding.engineStartSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                engineStart = b;
            }
        });

        binding.drivableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                drivable = b;
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                inspectionCalendar.set(Calendar.YEAR, year);
                inspectionCalendar.set(Calendar.MONTH, month);
                inspectionCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };
        binding.inspectionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.inspectionDate.setError(null);
                new DatePickerDialog(requireContext(), date, inspectionCalendar.get(Calendar.YEAR), inspectionCalendar.get(Calendar.MONTH), inspectionCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        binding.vehicleYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.vehicleYear.setError(null);
                showYearDialog();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        binding.inspectionDate.setText(dateFormat.format(inspectionCalendar.getTime()));
    }

    private void showYearDialog() {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        MonthYearPickerDialog editNameDialogFragment = MonthYearPickerDialog.newInstance();
        editNameDialogFragment.show(fm, "fragment_year");
        editNameDialogFragment.setCancelable(false);

        editNameDialogFragment.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int i2) {
                Log.e("TAG", "onDateSet: " + year);
                binding.vehicleYear.setText(year + "");
            }
        });
    }
}
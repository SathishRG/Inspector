package com.folkus.ui.login.fragments;

import static com.folkus.ui.login.ActivityHome.navController;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.comman.ProgressButton;
import com.folkus.data.remote.request.AddVehicleRequest;
import com.folkus.data.remote.response.AddVehicleResponse;
import com.folkus.data.remote.response.CarMakersResponseDropDown;
import com.folkus.data.remote.response.CarModelData;
import com.folkus.data.remote.response.CarModelResponse;
import com.folkus.data.remote.response.CatMakersData;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.SellerDealerShip;
import com.folkus.data.remote.response.SellerDealerShipResponse;
import com.folkus.data.remote.response.VehicleData;
import com.folkus.databinding.FragmentAddVehicleFragmentBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.folkus.ui.login.adapter.AdapterCustomerNameAutoComplete;
import com.folkus.ui.login.fragments.dialog.MonthYearPickerDialog;

import java.util.ArrayList;

public class AddVehicleFragment extends Fragment {

    private FragmentAddVehicleFragmentBinding binding;
    private DatePickerDialog picker;
    private InspectionRequestViewModel inspectionRequestViewModel;
    private AdapterCustomerNameAutoComplete adapter;
    private ArrayList<SellerDealerShip> data = new ArrayList<>();
    private ArrayList<CatMakersData> catMakersData = new ArrayList<>();
    private ArrayList<CarModelData> catModelData = new ArrayList<>();
    private boolean lock;
    private String makerId;
    private String modelId;
    private String phone_no;
    private String address;
    private String state_id;
    private String city_id;
    private String zipcode_id;
    private String createdAt;
    private String updatedAt;
    private String sellerDealerId;
    private ProgressButton submitButton;
    static boolean isCalledAddVehicle = false;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentAddVehicleFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.add_vehicle_form));
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submitButton = new ProgressButton(requireActivity(), binding.btnAddVehicleSubmit.constraintLayout);

        binding.dtfVehicleYear.setOnTouchListener((view12, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                showYearDialog();
                binding.dtfVehicleYear.setError(null);
                return true;
            }
            return false;
        });

        binding.dtfSchedule.setOnTouchListener((view13, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(requireActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view13, int year, int monthOfYear, int dayOfMonth) {
                                binding.dtfSchedule.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                                binding.dtfSchedule.setError(null);
                            }
                        }, year, month, day);
                picker.show();
                return true;
            }
            return false;
        });

        binding.dtfInspectionTime.setOnTouchListener((view1, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                // Create a new instance of TimePickerDialog and return it
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        binding.dtfInspectionTime.setText(hourOfDay + ":" + minute);
                        binding.dtfInspectionTime.setError(null);
                    }
                }, hour, minute, DateFormat.is24HourFormat(getActivity()));
                timePickerDialog.show();
                return true;
            }
            return false;
        });


        inspectionRequestViewModel.getSellerDealershipNameList("");
        inspectionRequestViewModel.getSellerDealershipNameResult().observe(requireActivity(), finalResult -> {
            lock = false;
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof SellerDealerShipResponse) {
                    SellerDealerShipResponse sellerDealerShipResponse = (SellerDealerShipResponse) success1;
                    boolean success = sellerDealerShipResponse.isSuccess();
                    if (success) {
                        data = sellerDealerShipResponse.getData();
                        ArrayList<String> customerName = new ArrayList<>();
                        data.forEach(sellerDealerShip -> {
                            String dealer_name = sellerDealerShip.getDealer_name();

                            customerName.add(dealer_name);
                        });
                        ArrayAdapter<String> adapter = new ArrayAdapter(requireActivity(), R.layout.item_spinner, customerName);
                        binding.dtfCustomerName.setAdapter(adapter);
                    } else {
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });

        inspectionRequestViewModel.getCarMakersList();
        inspectionRequestViewModel.getCarMakersDropDown().observe(requireActivity(), finalResult -> {
            lock = false;
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof CarMakersResponseDropDown) {
                    CarMakersResponseDropDown carMakersResponseDropDown = (CarMakersResponseDropDown) success1;
                    boolean success = carMakersResponseDropDown.isSuccess();
                    if (success) {
                        catMakersData = carMakersResponseDropDown.getData();
                        ArrayList<String> customerName = new ArrayList<>();
                        catMakersData.forEach(sellerDealerShip -> {
                            String dealer_name = sellerDealerShip.getCar_name();

                            customerName.add(dealer_name);
                        });
                        ArrayAdapter<String> adapter = new ArrayAdapter(requireActivity(), R.layout.item_spinner, customerName);
                        binding.dtfVehicleMake.setAdapter(adapter);
                    } else {
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });
        inspectionRequestViewModel.getCarModelResponse().observe(requireActivity(), finalResult -> {
            lock = false;
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof CarModelResponse) {
                    CarModelResponse carModelResponse = (CarModelResponse) success1;
                    boolean success = carModelResponse.isSuccess();
                    if (success) {
                        catModelData = carModelResponse.getData();
                        ArrayList<String> customerName = new ArrayList<>();
                        catModelData.forEach(sellerDealerShip -> {
                            String dealer_name = sellerDealerShip.getModel_name();
                            customerName.add(dealer_name);
                        });
                        ArrayAdapter<String> adapter = new ArrayAdapter(requireActivity(), R.layout.item_spinner, customerName);
                        binding.dtfVehicleModel.setAdapter(adapter);
                    } else {
                        Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                    }
                } else if (success1 instanceof LoginError) {
                    LoginError loginError = (LoginError) success1;
                    Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });

        if (!isCalledAddVehicle) {
            inspectionRequestViewModel.getAddVehicleResponse().observe(requireActivity(), finalResult -> {
                lock = false;
                if (finalResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (finalResult.getError() != null) {
                    Integer error = (Integer) finalResult.getError();
                    submitButton.buttonFinished();
                }

                Object success1 = finalResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof AddVehicleResponse) {
                        AddVehicleResponse addVehicleResponse = (AddVehicleResponse) success1;
                        boolean success = addVehicleResponse.isSuccess();
                        if (success) {
                            VehicleData vehicleData = addVehicleResponse.getData();
                            Toast.makeText(requireActivity(), addVehicleResponse.getMessage(), Toast.LENGTH_LONG).show();
                            submitButton.buttonFinished();
                            navController.navigate(R.id.addVehicleToInspectionTabFragment);
                        } else {
                            Toast.makeText(requireActivity(), "Failed", Toast.LENGTH_LONG).show();
                            submitButton.buttonFinished();
                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                        Toast.makeText(requireActivity(), loginError.getErr(), Toast.LENGTH_LONG).show();
                        submitButton.buttonFinished();
                    }
                }
            });
        }

        binding.dtfCustomerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG", "onItemSelected: " + i);
                SellerDealerShip sellerDealerShip = data.get(i);

                phone_no = sellerDealerShip.getPhone_no();
                address = sellerDealerShip.getAddress();
                state_id = String.valueOf(sellerDealerShip.getState_id());
                city_id = String.valueOf(sellerDealerShip.getCity_id());
                zipcode_id = String.valueOf(sellerDealerShip.getZipcode_id());
                createdAt = sellerDealerShip.getCreatedAt();
                updatedAt = sellerDealerShip.getUpdatedAt();
                sellerDealerId = String.valueOf(sellerDealerShip.getSeller_dealer_id());

                binding.dtfCustomerPhone.setText(phone_no);
                binding.dtfCustomerAddress.setText(address);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.dtfVehicleMake.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        binding.dtfVehicleModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG", "onItemSelected: " + i);
                CarModelData data = catModelData.get(i);
                modelId = data.getModel_id() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnAddVehicleSubmit.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = binding.dtfInspectionTime.getText().toString();
                String year = binding.dtfVehicleYear.getText().toString();
                String date = binding.dtfSchedule.getText().toString();
                String vehicleVin = binding.dtfCustomerVehicle.getText().toString();
                String comments = binding.dtfVehicleComments.getText().toString();

                if (time.isEmpty()) {
                    binding.dtfInspectionTime.setError("Please select inspection time");
                } else if (year.isEmpty()) {
                    binding.dtfVehicleYear.setError("Please select vehicle year");
                } else if (date.isEmpty()) {
                    binding.dtfSchedule.setError("Please select schedule date");
                } else if (vehicleVin.isEmpty()) {
                    binding.dtfCustomerVehicle.setError("Please enter vehicle VIN");
                } else {
                    if (isValidMobileNumber(phone_no)) {
                        submitButton.buttonActivated();
                        isCalledAddVehicle = false;
                        AddVehicleRequest addVehicleRequest = new AddVehicleRequest();
                        addVehicleRequest.setSeller_dealer_id(sellerDealerId);
                        addVehicleRequest.setAddress(address);
                        addVehicleRequest.setCreatedBy(createdAt);
                        addVehicleRequest.setUpdatedBy(updatedAt);
                        addVehicleRequest.setState_id(state_id);
                        addVehicleRequest.setCity_id(city_id);
                        addVehicleRequest.setZipcode_id(zipcode_id);
                        addVehicleRequest.setVin_no(vehicleVin);
                        addVehicleRequest.setYear(year);
                        addVehicleRequest.setMake(makerId);
                        addVehicleRequest.setModel(modelId);
                        addVehicleRequest.setComments(comments);
                        addVehicleRequest.setDate(date);
                        addVehicleRequest.setTime(time);
                        addVehicleRequest.setInspector_id(inspectionRequestViewModel.getInspectorId());
                        inspectionRequestViewModel.addVehicle(addVehicleRequest);
                        binding.dtfCustomerPhone.setError(null);
                    } else {
                        binding.dtfCustomerPhone.setError("Invalid MobileNumber");
                    }
                }
            }
        });
    }

    private boolean isValidMobileNumber(String mobieNumber) {
        return mobieNumber.length() == 10 && mobieNumber.length() >= 10;
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
                binding.dtfVehicleYear.setText(year + "");
            }
        });
    }


    @Override
    public void onDetach() {
        super.onDetach();
        inspectionRequestViewModel.getSellerDealershipNameResult().removeObservers(requireActivity());
        inspectionRequestViewModel.getCarModelResponse().removeObservers(requireActivity());
        inspectionRequestViewModel.getCarMakersDropDown().removeObservers(requireActivity());
    }
}

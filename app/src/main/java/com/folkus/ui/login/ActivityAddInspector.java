package com.folkus.ui.login;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.comman.ProgressButton;
import com.folkus.data.remote.request.AddInspectorRequest;
import com.folkus.data.remote.response.City;
import com.folkus.data.remote.response.CityResponse;
import com.folkus.data.remote.response.InspectorPosition;
import com.folkus.data.remote.response.InspectorPositionResponse;
import com.folkus.data.remote.response.InspectorResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.data.remote.response.LoginResponse;
import com.folkus.data.remote.response.StateData;
import com.folkus.data.remote.response.StateResponse;
import com.folkus.data.remote.response.ZipCode;
import com.folkus.data.remote.response.ZipCodeResponse;
import com.folkus.databinding.ActivityAddInspectorBinding;
import com.folkus.ui.login.fragments.dialog.DialogAddInspectorSuccess;
import com.folkus.ui.login.fragments.dialog.DialogLogout;
import com.folkus.ui.login.model.FinalResult;

import java.util.ArrayList;

public class ActivityAddInspector extends AppCompatActivity {
    private ActivityAddInspectorBinding binding;
    private InspectionRequestViewModel inspectionRequestViewModel;
    private ArrayList<StateData> stateData = new ArrayList<>();
    private ArrayList<City> cityData = new ArrayList<>();
    private ArrayList<ZipCode> zipData = new ArrayList<>();
    private ArrayList<InspectorPosition> inspectorPosition = new ArrayList<>();
    private int state_id;
    private int city_id;
    private int zip_id;
    private int position_id;
    private ProgressButton submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddInspectorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.appBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submitButton = new ProgressButton(this, binding.btnCancelInspectionSubmit.constraintLayout);

        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(ActivityAddInspector.this)).get(InspectionRequestViewModel.class);
        inspectionRequestViewModel.getInspectorPositionList();
        inspectionRequestViewModel.getStates();
        inspectionRequestViewModel.getStateResult().observe(this, new Observer<FinalResult>() {
            @Override
            public void onChanged(@Nullable FinalResult loginResult) {
                if (loginResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (loginResult.getError() != null) {
                    Integer error = (Integer) loginResult.getError();
                }

                Object success1 = loginResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof StateResponse) {
                        StateResponse stateResponse = (StateResponse) success1;
                        boolean success = stateResponse.isSuccess();
                        if (success) {
                            stateData = stateResponse.getData();
                            ArrayList<String> strings = new ArrayList<>();

                            stateData.forEach(stateData -> {
                                strings.add(stateData.getState_name());
                            });

                            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityAddInspector.this, R.layout.item_spinner, strings);
                            binding.ddState.setAdapter(adapter);

                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                    }
                }
            }
        });


        inspectionRequestViewModel.getCityResult().observe(this, new Observer<FinalResult>() {
            @Override
            public void onChanged(@Nullable FinalResult loginResult) {
                if (loginResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (loginResult.getError() != null) {
                    Integer error = (Integer) loginResult.getError();
                }

                Object success1 = loginResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof CityResponse) {
                        CityResponse cityResponse = (CityResponse) success1;
                        boolean success = cityResponse.isSuccess();
                        if (success) {
                            cityData = cityResponse.getData();
                            ArrayList<String> strings = new ArrayList<>();
                            cityData.forEach(stateData -> {
                                strings.add(stateData.getCity_name());
                            });

                            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityAddInspector.this, R.layout.item_spinner, strings);
                            binding.ddCity.setAdapter(adapter);

                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                    }
                }
            }
        });

        inspectionRequestViewModel.getZipCodeResult().observe(this, new Observer<FinalResult>() {
            @Override
            public void onChanged(@Nullable FinalResult loginResult) {
                if (loginResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (loginResult.getError() != null) {
                    Integer error = (Integer) loginResult.getError();
                }

                Object success1 = loginResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof ZipCodeResponse) {
                        ZipCodeResponse zipCodeResponse = (ZipCodeResponse) success1;
                        boolean success = zipCodeResponse.isSuccess();
                        if (success) {
                            zipData = zipCodeResponse.getData();
                            ArrayList<String> strings = new ArrayList<>();
                            zipData.forEach(stateData -> {
                                strings.add(stateData.getZipcode() + "");
                            });

                            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityAddInspector.this, R.layout.item_spinner, strings);
                            binding.ddZip.setAdapter(adapter);

                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                    }
                }
            }
        });

        inspectionRequestViewModel.getInspectorPositionResult().observe(this, new Observer<FinalResult>() {
            @Override
            public void onChanged(@Nullable FinalResult loginResult) {
                if (loginResult == null) {
                    Log.e("TAG", "onChanged: null");
                    return;
                }
                if (loginResult.getError() != null) {
                    Integer error = (Integer) loginResult.getError();
                }

                Object success1 = loginResult.getSuccess();
                if (success1 != null) {
                    if (success1 instanceof InspectorPositionResponse) {
                        InspectorPositionResponse inspectorPositionResponse = (InspectorPositionResponse) success1;
                        boolean success = inspectorPositionResponse.isSuccess();
                        if (success) {
                            inspectorPosition = inspectorPositionResponse.getData();
                            ArrayList<String> strings = new ArrayList<>();
                            inspectorPosition.forEach(stateData -> {
                                strings.add(stateData.getPosition());
                            });

                            ArrayAdapter<String> adapter = new ArrayAdapter(ActivityAddInspector.this, R.layout.item_spinner, strings);
                            binding.dtfPosition.setAdapter(adapter);

                        }
                    } else if (success1 instanceof LoginError) {
                        LoginError loginError = (LoginError) success1;
                    }
                }
            }
        });
        binding.ddState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG", "onItemSelected: " + i);
                state_id = stateData.get(i).getState_id();
                inspectionRequestViewModel.getCityList(state_id + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.ddCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("TAG", "onItemSelected: " + i);
                city_id = cityData.get(i).getCity_id();
                inspectionRequestViewModel.getZipCodeList(city_id + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.ddZip.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                zip_id = zipData.get(i).getZipcode_id();
                Log.e("TAG", "onItemSelected: " + zip_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.dtfPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                position_id = inspectorPosition.get(i).getPosition_id();
                Log.e("TAG", "onItemSelected: " + position_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.btnCancelInspectionSubmit.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = binding.dtfInspectorName.getText().toString();
                String address = binding.dtfAddress.getText().toString();
                String phone = binding.dtfPhone.getText().toString();
                String email = binding.dtfEmail.getText().toString();
                String experience = binding.dtfVehicleComments.getText().toString();
                if (name.isEmpty()) {
                    binding.dtfInspectorName.setError("Please enter name");
                } else if (address.isEmpty()) {
                    binding.dtfAddress.setError("Please enter address");
                } else if (phone.isEmpty()) {
                    binding.dtfPhone.setError("Please enter phone number");
                } else if (email.isEmpty()) {
                    binding.dtfEmail.setError("Please enter email id");
                } else if (experience.isEmpty()) {
                    binding.dtfVehicleComments.setError("Please enter experience");
                } else {
                    if (isValidMobileNumber(phone)) {
                        if (isValidEmail(email)) {
                            submitButton.buttonActivated();
                            AddInspectorRequest addInspectorRequest = new AddInspectorRequest();
                            addInspectorRequest.setName(name);
                            addInspectorRequest.setEmail(email);
                            addInspectorRequest.setPassword("");
                            addInspectorRequest.setPhone_no(phone);
                            addInspectorRequest.setAddress(address);
                            addInspectorRequest.setState_id(state_id);
                            addInspectorRequest.setCity_id(city_id);
                            addInspectorRequest.setZipcode_id(zip_id);
                            addInspectorRequest.setPosition_id(position_id);
                            addInspectorRequest.setImage("");
                            addInspectorRequest.setExperience(Integer.parseInt(experience));
                            inspectionRequestViewModel.addInspector(addInspectorRequest);
                        } else {
                            binding.dtfEmail.setError("Invalid email");
                        }
                    } else {
                        binding.dtfPhone.setError("Invalid Mobile number");
                    }
                }
            }
        });

        inspectionRequestViewModel.getInspectorResponse().observe(this, finalResult -> {
            if (finalResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (finalResult.getError() != null) {
                Integer error = (Integer) finalResult.getError();
                Log.e("TAG", "onChanged: " + error);
                submitButton.buttonReverted();
                submitButton.buttonFinished();
            }

            Object success1 = finalResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof InspectorResponse) {
                    InspectorResponse loginResponse = (InspectorResponse) success1;
                    boolean success = loginResponse.getSuccess();
                    if (success) {
                        submitButton.buttonReverted();
                        submitButton.buttonFinished();
                        showLogoutDialog();
                        Toast.makeText(ActivityAddInspector.this, "Inspector created successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        submitButton.buttonReverted();
                        submitButton.buttonFinished();
                        Toast.makeText(ActivityAddInspector.this, loginResponse.getError().getErr(), Toast.LENGTH_LONG).show();
                    }
                } else if (success1 instanceof LoginError) {
                    submitButton.buttonReverted();
                    submitButton.buttonFinished();
                    LoginError loginError = (LoginError) success1;
                    Log.e("TAG", "onChanged: " + loginError.getErr());
                    Toast.makeText(ActivityAddInspector.this, loginError.getErr(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean isValidEmail(String username) {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches();
    }

    private boolean isValidMobileNumber(String mobieNumber) {
        return mobieNumber.length() == 10 && mobieNumber.length() >= 10;
    }

    private void showLogoutDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DialogAddInspectorSuccess editNameDialogFragment = DialogAddInspectorSuccess.newInstance(getString(R.string.inspector_created));
        editNameDialogFragment.show(fm, "fragment_success");
        editNameDialogFragment.setCancelable(false);
    }
}

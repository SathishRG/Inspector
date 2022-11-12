package com.folkus.ui.login.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.folkus.R;
import com.folkus.data.remote.request.CompleteSearchRequest;
import com.folkus.data.remote.request.PendingSearchRequest;
import com.folkus.data.remote.response.CompletedInspection;
import com.folkus.data.remote.response.CompletedInspectionResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.CompletedInspectionsFragmentBinding;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.folkus.ui.login.adapter.AdapterCompletedInspection;
import com.folkus.ui.login.adapter.AdapterPendingInspection;

import java.util.ArrayList;

public class CompletedInspectionsFragment extends Fragment {
    private CompletedInspectionsFragmentBinding binding;
    private InspectionRequestViewModel inspectionRequestViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = CompletedInspectionsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume: called");
        //inspectionRequestViewModel.completedInspection(0, "CompletedInspectionsFragment");
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvCompletedInspection.setLayoutManager(mLayoutManager);
        binding.progressBar.setVisibility(View.VISIBLE);

        inspectionRequestViewModel.completedInspection(0, "CompletedInspectionsFragment");

        inspectionRequestViewModel.getCompleteSearchResponse().observe(requireActivity(), loginResult -> {
            if (loginResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (loginResult.getError() != null) {
                Integer error = (Integer) loginResult.getError();
                binding.progressBar.setVisibility(View.GONE);
                showMsg(error);
            }

            Object success1 = loginResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof CompletedInspectionResponse) {
                    CompletedInspectionResponse completedInspectionResponse = (CompletedInspectionResponse) success1;
                    boolean success = completedInspectionResponse.isSuccess();
                    if (success) {
                        try {
                            ArrayList<CompletedInspection> data = completedInspectionResponse.getData();
                            AdapterCompletedInspection mAdapter = new AdapterCompletedInspection(data);
                            binding.progressBar.setVisibility(View.GONE);
                            if (data.size() > 0) {
                                binding.rvCompletedInspection.setAdapter(mAdapter);
                            } else {
                                binding.rvCompletedInspection.setAdapter(mAdapter);
                                Toast.makeText(requireActivity(), "Record not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                } else if (success1 instanceof LoginError) {
                    binding.progressBar.setVisibility(View.GONE);
                    LoginError loginError = (LoginError) success1;
                    showMsg(loginError.getErr());
                }
            }
        });

        inspectionRequestViewModel.getCompletedInspectionResult().observe(requireActivity(), loginResult -> {
            if (loginResult == null) {
                Log.e("TAG", "onChanged: null");
                return;
            }
            if (loginResult.getError() != null) {
                Integer error = (Integer) loginResult.getError();
                binding.progressBar.setVisibility(View.GONE);
                showMsg(error);
            }

            Object success1 = loginResult.getSuccess();
            if (success1 != null) {
                if (success1 instanceof CompletedInspectionResponse) {
                    CompletedInspectionResponse completedInspectionResponse = (CompletedInspectionResponse) success1;
                    boolean success = completedInspectionResponse.isSuccess();
                    if (success) {
                        ArrayList<CompletedInspection> data = completedInspectionResponse.getData();
                        binding.progressBar.setVisibility(View.GONE);

                        AdapterCompletedInspection mAdapter = new AdapterCompletedInspection(data);
                        binding.rvCompletedInspection.setAdapter(mAdapter);
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                    }
                } else if (success1 instanceof LoginError) {
                    binding.progressBar.setVisibility(View.GONE);
                    LoginError loginError = (LoginError) success1;
                    showMsg(loginError.getErr());
                }
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_search_view, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem mSearchMenuItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) mSearchMenuItem.getActionView();

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                binding.progressBar.setVisibility(View.GONE);
                inspectionRequestViewModel.completedInspection(0, "PendingInspectionsFragment");
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                CompleteSearchRequest completeSearchRequest = new CompleteSearchRequest();
                completeSearchRequest.setInspectorId("1");
                completeSearchRequest.setSearch(query);
                inspectionRequestViewModel.completeSearchApi(completeSearchRequest);
                binding.progressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void showMsg(@StringRes Integer errorString) {
        Toast.makeText(requireActivity(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showMsg(String errorString) {
        Toast.makeText(requireActivity(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //inspectionRequestViewModel.getCompletedInspectionResult().removeObservers(requireActivity());
    }
}

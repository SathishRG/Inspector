package com.folkus.ui.login.fragments;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.Constant.position;

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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.folkus.R;
import com.folkus.comman.EventObserver;
import com.folkus.data.remote.request.CompleteSearchRequest;
import com.folkus.data.remote.request.MonthSearchRequest;
import com.folkus.data.remote.request.PendingSearchRequest;
import com.folkus.data.remote.request.ReopenSearchRequest;
import com.folkus.data.remote.response.CompletedInspection;
import com.folkus.data.remote.response.CompletedInspectionResponse;
import com.folkus.data.remote.response.LoginError;
import com.folkus.databinding.PendingInspectionsFragmentBinding;
import com.folkus.ui.login.InspectionRequestViewModel;
import com.folkus.ui.login.InspectionViewModelFactory;
import com.folkus.ui.login.adapter.AdapterPendingInspection;

import java.util.ArrayList;

public class PendingInspectionsFragment extends Fragment {
    private PendingInspectionsFragmentBinding binding;
    private InspectionRequestViewModel inspectionRequestViewModel;
    private LifecycleOwner lifecycleOwner;
    AdapterPendingInspection mAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = PendingInspectionsFragmentBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);
        View root = binding.getRoot();
        lifecycleOwner = this;
        inspectionRequestViewModel = new ViewModelProvider(this, new InspectionViewModelFactory(requireActivity())).get(InspectionRequestViewModel.class);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (position == 2) {
//            inspectionRequestViewModel.completedInspection(2, "PendingInspectionsFragment");
//        } else if (position == 3) {
//            inspectionRequestViewModel.completedInspection(3, "PendingInspectionsFragment");
//        } else {
//            inspectionRequestViewModel.completedInspection(1, "PendingInspectionsFragment");
//        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        Log.e("onViewCreated: ", "" + position);
        if (position == 2) {
            inspectionRequestViewModel.completedInspection(2, "PendingInspectionsFragment");
        } else if (position == 3) {
            inspectionRequestViewModel.completedInspection(3, "PendingInspectionsFragment");
        } else if (position == 1) {
            inspectionRequestViewModel.completedInspection(1, "PendingInspectionsFragment");
        } else if (position == 0) {
            inspectionRequestViewModel.completedInspection(0, "PendingInspectionsFragment");
        }


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        binding.rvInspectionRequest.setLayoutManager(mLayoutManager);

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }

        inspectionRequestViewModel.isSelectInspectionDetail.observe(lifecycleOwner, new EventObserver<CompletedInspection>(completedInspection ->
        {
            if (completedInspection != null) {
                inspectionRequestViewModel.setCompletedInspection(completedInspection);
                navController.navigate(R.id.navigation);
            }
        }));

        inspectionRequestViewModel.getPendingInspectionResult().observe(requireActivity(), loginResult -> {
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
                    CompletedInspectionResponse pendingInspectionResponse = (CompletedInspectionResponse) success1;
                    boolean success = pendingInspectionResponse.isSuccess();
                    if (success) {
                        ArrayList<CompletedInspection> data = pendingInspectionResponse.getData();
                        binding.progressBar.setVisibility(View.GONE);

                        mAdapter = new AdapterPendingInspection(data, PendingInspectionsFragment.this, inspectionRequestViewModel);
                        binding.rvInspectionRequest.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
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

        inspectionRequestViewModel.getReopenedInspectionResult().observe(requireActivity(), loginResult -> {
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
                    CompletedInspectionResponse pendingInspectionResponse = (CompletedInspectionResponse) success1;
                    boolean success = pendingInspectionResponse.isSuccess();
                    if (success) {
                        ArrayList<CompletedInspection> data = pendingInspectionResponse.getData();
                        binding.progressBar.setVisibility(View.GONE);

                        AdapterPendingInspection mAdapter = new AdapterPendingInspection(data, PendingInspectionsFragment.this, inspectionRequestViewModel);
                        binding.rvInspectionRequest.setAdapter(mAdapter);
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

        inspectionRequestViewModel.getTotalCompletedInspectionResult().observe(requireActivity(), loginResult -> {
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
                    CompletedInspectionResponse pendingInspectionResponse = (CompletedInspectionResponse) success1;
                    boolean success = pendingInspectionResponse.isSuccess();
                    if (success) {
                        try {
                            ArrayList<CompletedInspection> data = pendingInspectionResponse.getData();
                            binding.progressBar.setVisibility(View.GONE);

                            AdapterPendingInspection mAdapter = new AdapterPendingInspection(data, PendingInspectionsFragment.this, inspectionRequestViewModel);
                            binding.rvInspectionRequest.setAdapter(mAdapter);
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

        inspectionRequestViewModel.getPendingSearchResponse().observe(requireActivity(), loginResult -> {
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
                            AdapterPendingInspection mAdapter = new AdapterPendingInspection(data, PendingInspectionsFragment.this, inspectionRequestViewModel);
                            binding.progressBar.setVisibility(View.GONE);

                            if (data.size() > 0) {
                                binding.rvInspectionRequest.setAdapter(mAdapter);
                            } else {
                                binding.rvInspectionRequest.setAdapter(mAdapter);
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

        inspectionRequestViewModel.getMonthSearchResponse().observe(requireActivity(), loginResult -> {
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
                            AdapterPendingInspection mAdapter = new AdapterPendingInspection(data, PendingInspectionsFragment.this, inspectionRequestViewModel);
                            binding.progressBar.setVisibility(View.GONE);
                            if (data.size() > 0) {
                                binding.rvInspectionRequest.setAdapter(mAdapter);
                            } else {
                                binding.rvInspectionRequest.setAdapter(mAdapter);
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

        inspectionRequestViewModel.getReOpenSearchResponse().observe(requireActivity(), loginResult -> {
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
                            AdapterPendingInspection mAdapter = new AdapterPendingInspection(data, PendingInspectionsFragment.this, inspectionRequestViewModel);
                            binding.progressBar.setVisibility(View.GONE);
                            if (data.size() > 0) {
                                binding.rvInspectionRequest.setAdapter(mAdapter);
                            } else {
                                binding.rvInspectionRequest.setAdapter(mAdapter);
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
//
//        if (position == 2) {
//            inspectionRequestViewModel.completedInspection(2, "PendingInspectionsFragment");
//        } else if (position == 3) {
//            inspectionRequestViewModel.completedInspection(3, "PendingInspectionsFragment");
//        } else {
//            inspectionRequestViewModel.completedInspection(1, "PendingInspectionsFragment");
//        }
    }

    private void showMsg(@StringRes Integer errorString) {
        Toast.makeText(requireActivity(), errorString, Toast.LENGTH_SHORT).show();
    }

    private void showMsg(String errorString) {
        Toast.makeText(requireActivity(), errorString, Toast.LENGTH_SHORT).show();
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
                if (position == 2) {
                    inspectionRequestViewModel.completedInspection(2, "PendingInspectionsFragment");
                } else if (position == 3) {
                    inspectionRequestViewModel.completedInspection(3, "PendingInspectionsFragment");
                } else if (position == 1) {
                    inspectionRequestViewModel.completedInspection(1, "PendingInspectionsFragment");
                } else if (position == 0) {
                    inspectionRequestViewModel.completedInspection(0, "PendingInspectionsFragment");
                }
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (position == 2) {
                    MonthSearchRequest monthSearchRequest = new MonthSearchRequest();
                    monthSearchRequest.setInspectorId(inspectionRequestViewModel.getInspectorId());
                    monthSearchRequest.setSearch(query);
                    inspectionRequestViewModel.monthSearchApi(monthSearchRequest);
                } else if (position == 3) {
                    ReopenSearchRequest reopenSearchRequest = new ReopenSearchRequest();
                    reopenSearchRequest.setInspectorId(inspectionRequestViewModel.getInspectorId());
                    reopenSearchRequest.setSearch(query);
                    inspectionRequestViewModel.reOpenSearchApi(reopenSearchRequest);
                } else if (position == 1) {
                    PendingSearchRequest pendingSearchRequest = new PendingSearchRequest();
                    pendingSearchRequest.setInspectorId(inspectionRequestViewModel.getInspectorId());
                    pendingSearchRequest.setSearch(query);
                    inspectionRequestViewModel.pendingSearchApi(pendingSearchRequest);
                } else if (position == 0) {
                    CompleteSearchRequest completeSearchRequest = new CompleteSearchRequest();
                    completeSearchRequest.setInspectorId(inspectionRequestViewModel.getInspectorId());
                    completeSearchRequest.setSearch(query);
                    inspectionRequestViewModel.completeSearchApi(completeSearchRequest);
                }
                binding.progressBar.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        inspectionRequestViewModel.getCompletedInspectionResult().removeObservers(requireActivity());
    }
}

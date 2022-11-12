package com.folkus.ui.login.fragments;

import static androidx.navigation.fragment.FragmentKt.findNavController;
import static com.folkus.ui.login.Constant.position;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.folkus.R;
import com.folkus.databinding.InspectionFragmentBinding;
import com.folkus.ui.login.ActivityHome;
import com.folkus.ui.login.adapter.TabLayoutAdapter;
import com.folkus.ui.login.fragments.inspection.InspectionDetailsFragment;
import com.google.android.material.tabs.TabLayout;

public class InspectionFragment extends Fragment {

    private InspectionFragmentBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = InspectionFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ((ActivityHome) requireActivity()).doAppBarChanges(getString(R.string.title_inspection));
        // ((ActivityHome) requireActivity()).doAppBarChangesForSearch(getString(R.string.title_inspection), true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume11:", "test");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Pending Inspections"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Completed"));
        binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        TabLayoutAdapter adapter = new TabLayoutAdapter(getChildFragmentManager(), binding.tabLayout.getTabCount());
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewPager.setCurrentItem(tab.getPosition());
                if (view.isFocused()) {
                    hiddenKeyboard(view);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (view.isFocused()) {
                    hiddenKeyboard(view);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (view.isFocused()) {
                    hiddenKeyboard(view);
                }
            }
        });

        if (position == 0) {
            binding.viewPager.setCurrentItem(1);
        } else {
            binding.viewPager.setCurrentItem(0);
        }

    }

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

package com.folkus.ui.login.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.folkus.R;
import com.folkus.databinding.FragmentAboutUsBinding;
import com.folkus.databinding.FragmentContactUsBinding;
import com.folkus.ui.login.ActivityHome;

public class ContactUSFragment extends Fragment {

    private FragmentContactUsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentContactUsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((ActivityHome)requireActivity()).doAppBarChanges(getString(R.string.menu_contact_us));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

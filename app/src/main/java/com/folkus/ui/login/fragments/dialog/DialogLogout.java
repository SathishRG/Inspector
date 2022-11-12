package com.folkus.ui.login.fragments.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.folkus.R;
import com.folkus.ui.login.ActivityLogin;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;

public class DialogLogout extends DialogFragment {

    private AppCompatButton btnCancel,btnLogout;
    private UserViewModel userViewModel;

    public DialogLogout() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogLogout newInstance() {
        DialogLogout frag = new DialogLogout();
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
        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(requireContext().getApplicationContext())).get(UserViewModel.class);
        return inflater.inflate(R.layout.logout_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnCancel = (AppCompatButton) view.findViewById(R.id.btnLogoutCancel);
        btnLogout = (AppCompatButton) view.findViewById(R.id.btnLogOut);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userViewModel.doLogout();
            }
        });



        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        userViewModel.logoutNavigation.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    startActivity(new Intent(requireContext(), ActivityLogin.class));
                    requireActivity().finish();
                    dismiss();
                }
            }
        });

        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        userViewModel.logoutNavigation.removeObservers(requireActivity());
    }
}

package com.folkus.ui.login.fragments.dialog;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import static com.folkus.ui.login.ActivityHome.navController;
import static com.folkus.ui.login.Constant.position;
import static com.folkus.ui.login.fragments.SellerDetailsFragment.isCalledOnceSellerApi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;

import com.folkus.R;
import com.folkus.ui.login.ActivityLogin;
import com.folkus.ui.login.UserViewModel;
import com.folkus.ui.login.UserViewModelFactory;
import com.folkus.ui.login.fragments.HomeFragment;
import com.folkus.ui.login.fragments.InspectionFragment;

public class DialogAddInspectorSuccess extends DialogFragment {

    private AppCompatButton btnLogout;
    private UserViewModel userViewModel;
    private TextView txtLogoutMessage;
    private static String message;

    public DialogAddInspectorSuccess() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static DialogAddInspectorSuccess newInstance(String string) {
        message = string;
        DialogAddInspectorSuccess frag = new DialogAddInspectorSuccess();
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
        return inflater.inflate(R.layout.add_inspector_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnLogout = (AppCompatButton) view.findViewById(R.id.btnLogOut);
        txtLogoutMessage = (TextView) view.findViewById(R.id.txtLogoutMessage);
        txtLogoutMessage.setText(message);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.equalsIgnoreCase(getString(R.string.seller_accepted))) {
                    dismiss();
                    position = 1;
                    navController.navigate(R.id.action_sellerInfoFragment_to_nav_inspection_tab2);
                    isCalledOnceSellerApi = true;
//                    findNavController(DialogAddInspectorSuccess.this).
//                            navigate(R.id.action_sellerInfoFragment_to_nav_inspection_tab2,null, new NavOptions.Builder().
//                                    setPopUpTo(findNavController(DialogAddInspectorSuccess.this)
//                                            .getGraph().getStartDestinationId(), true).build());
                } else {
                    dismiss();
                    requireActivity().finish();
                    userViewModel.doLogout();
                }
            }
        });

        userViewModel.logoutNavigation.observe(requireActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
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

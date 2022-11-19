package com.folkus.ui.login;


import static com.folkus.ui.login.InspectionRequestViewModel.exteriorFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.exterior_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.exterior_uriArrayList;
import static com.folkus.ui.login.InspectionRequestViewModel.interiorFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.interior_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.interior_uriArrayList;
import static com.folkus.ui.login.InspectionRequestViewModel.mechanicalFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.mechanical_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.mechanical_uriArrayList;
import static com.folkus.ui.login.InspectionRequestViewModel.powerTrainFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.powerTrain_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.powerTrain_uriArrayList;
import static com.folkus.ui.login.InspectionRequestViewModel.testDriveFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.testDrive_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.testDrive_uriArrayList;
import static com.folkus.ui.login.InspectionRequestViewModel.tiresWheelFirstTimeCalled;
import static com.folkus.ui.login.InspectionRequestViewModel.tiresWheel_picturePathsList;
import static com.folkus.ui.login.InspectionRequestViewModel.tiresWheel_uriArrayList;
import static com.folkus.ui.login.fragments.ProfileFragment.isEditable;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.exteriorComments_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noColourFade_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noExteriorScratches_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noGlassDamaged_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noSideMirrorDamage_;
import static com.folkus.ui.login.fragments.inspection.ExteriorData.noVisibleRust_;
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
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsFrontCarpet;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsFrontSeat;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.dsRearSeat;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.engineLower;
import static com.folkus.ui.login.fragments.inspection.InspectionInteriorImageData.odoMeters;
import static com.folkus.ui.login.fragments.inspection.InteriorData.backSeatCondition_;
import static com.folkus.ui.login.fragments.inspection.InteriorData.frontSeatCondition_;
import static com.folkus.ui.login.fragments.inspection.InteriorData.interiorComments_;
import static com.folkus.ui.login.fragments.inspection.InteriorData.noMajorVisibleDamage_;
import static com.folkus.ui.login.fragments.inspection.InteriorData.noVisibleDamage_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.acRunsHot_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.addMultipleImagesEncode_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.catalyticConverter_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.engineBottomEnd_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.engineLightStartUp_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.engineUpperEnd_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.heaterRunsHot_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.mechanicalComments_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.noAds_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.noSRS_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.uploadVideoExactionEncode_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.uploadVideoStandEncode_;
import static com.folkus.ui.login.fragments.inspection.MechanicalData.uploadVideoStartCarEncode_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.breakingSenese_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.differential_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.exilatorLevel_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.manualTransmission_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.steeringControls_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.testDriveComments_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.transferCase_;
import static com.folkus.ui.login.fragments.inspection.TestDriveData.uploadTestDriveVideoEncode_;
import static com.folkus.ui.login.fragments.inspection.TiresWheelData.anyScratchesOnWheels_;
import static com.folkus.ui.login.fragments.inspection.TiresWheelData.fourTiresCondition_;
import static com.folkus.ui.login.fragments.inspection.TiresWheelData.tiresWheelComments_;
import static com.folkus.ui.login.fragments.inspection.TiresWheelData.treadDepth_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.automaticTransmission_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.differentialOperation_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.dsFrontDoor_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.engineBottomNoise_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.picturePathsList_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.powerComments_;
import static com.folkus.ui.login.fragments.inspection.powerTrainData.transferCaseOperation_;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.folkus.R;
import com.folkus.databinding.ActivityHomeBinding;
import com.folkus.ui.login.fragments.dialog.DialogLogout;
import com.folkus.ui.login.fragments.inspection.MechanicalData;
import com.google.android.material.navigation.NavigationView;

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;
    private UserViewModel userViewModel;
    public static NavController navController;
    private boolean shouldShowNotification = true;
    private ActivityHome menuHost;
    DrawerLayout drawer;
    ActionBarDrawerToggle actionBarDrawerToggle;
    MenuInflater menuInflater1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
//        registerReceiver(receiver, filter);


        userViewModel = new ViewModelProvider(this, new UserViewModelFactory(getApplicationContext())).get(UserViewModel.class);

        setSupportActionBar(binding.appBarHome.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                showLogoutDialog();
                Log.d("TAG", "onMenuItemClick: logout");
                return true;
            }
        });
        userViewModel.setUserDetails();
        userViewModel.profileData.observe(this, loginData -> {
            View mHeaderView = navigationView.getHeaderView(0);
            TextView header = mHeaderView.findViewById(R.id.tvUserNameView);
            header.setText(loginData.getName());
        });
    }

    public void doAppBarChanges(String title) {
        binding.appBarHome.appLogo.setVisibility(View.GONE);
        binding.appBarHome.search.setVisibility(View.GONE);
        binding.appBarHome.toolbarTitle.setText(title);
    }

    public void doAppBarChangesForSearch(String title, boolean hideNotification) {
        binding.appBarHome.appLogo.setVisibility(View.GONE);
        binding.appBarHome.search.setVisibility(View.VISIBLE);
        binding.appBarHome.toolbarTitle.setText(title);
        menuHost.removeMenuProvider(menuProvider);
    }


    @Override
    public boolean onSupportNavigateUp() {
        Log.e("Home", "onSupportNavigateUp: ");
        navController = Navigation.findNavController(ActivityHome.this, R.id.nav_host_fragment_content_home);

        try {
            int id = navController.getCurrentDestination().getId();
            if (id == R.id.nav_home) { // change the fragment id
                drawer.openDrawer(GravityCompat.START);
            }
            if (id == R.id.inspectionDetailsFragment) { // change the fragment id
                clearInsExteriorImagesTempData();
                clearInsInteriorImagesTempData();
                clearPowerTrainTempData();
                clearMechanicalTempData();
                clearTestDriveTempData();
                clearExteriorTempData();
                clearInteriorTempData();
                clearTiresWheelTempData();
                clearMultipleImageAndVideoTempData();
                clearFirstTimeCalledTempData();
            }
            if (id == R.id.nav_profile) {
                isEditable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Navigation.findNavController(this, R.id.nav_host_fragment_content_home).navigateUp();
    }

    private void showLogoutDialog() {
        FragmentManager fm = getSupportFragmentManager();
        DialogLogout editNameDialogFragment = DialogLogout.newInstance();
        editNameDialogFragment.show(fm, "fragment_logout");
        editNameDialogFragment.setCancelable(false);
    }

    public void createMenu() {
        menuHost = ActivityHome.this;
        menuHost.invalidateMenu();
        menuHost.removeMenuProvider(menuProvider);
        menuHost.addMenuProvider(menuProvider, ActivityHome.this, Lifecycle.State.STARTED);
    }

    private final MenuProvider menuProvider = new MenuProvider() {
        @Override
        public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
            menuInflater.inflate(R.menu.home, menu);
            navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                @Override
                public void onDestinationChanged(@NonNull NavController navController, @NonNull NavDestination navDestination, @Nullable Bundle bundle) {
                    Log.e("onDestinationChanged: ", "" + navDestination.getDisplayName());
              /*      if (navDestination.getId() == R.id.sellerInfoFragment) {
                        menu.removeItem(R.id.notification);
                    }
                    if (navDestination.getId() == R.id.notificationFragment) {
                        menu.removeItem(R.id.notification);
                    }
                    if (navDestination.getId() == R.id.navigation) {
                        menu.removeItem(R.id.notification);
                    }
                    if (navDestination.getId() == R.id.nav_inspection_tab) {
                        menu.removeItem(R.id.notification);
                    }*/
                    if (navDestination.getId() == R.id.nav_home || navDestination.getId() == R.id.nav_inspection_request || navDestination.getId() == R.id.nav_add_vehicle) {

                    } else {
                        menu.removeItem(R.id.notification);
                    }
                }
            });
        }

        @Override
        public void onPrepareMenu(@NonNull Menu menu) {
            MenuProvider.super.onPrepareMenu(menu);
            Log.e("onPrepareMenu: ", "called");
        }

        @Override
        public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
            if (menuItem.getItemId() == R.id.notification) {
                navToNotification();
                return true;
            } else {
                return false;
            }
        }
    };

    private void hiddenKeyboard(View v) {
        InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        keyboard.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void navToNotification() {
        navController.navigate(R.id.notificationFragment);
    }

    @Override
    public void onBackPressed() {
        navController = Navigation.findNavController(ActivityHome.this, R.id.nav_host_fragment_content_home);
        try {
            int id = navController.getCurrentDestination().getId();
            if (id == R.id.nav_home) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    this.finish();
                }
            } else {
                navController.popBackStack();
            }

            if (id == R.id.inspectionDetailsFragment) {
                clearInsExteriorImagesTempData();
                clearInsInteriorImagesTempData();
                clearPowerTrainTempData();
                clearMechanicalTempData();
                clearTestDriveTempData();
                clearExteriorTempData();
                clearInteriorTempData();
                clearTiresWheelTempData();
                clearMultipleImageAndVideoTempData();
                clearFirstTimeCalledTempData();
            }

            if (id == R.id.nav_profile) {
                isEditable = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clearFirstTimeCalledTempData() {
        powerTrainFirstTimeCalled = false;
        mechanicalFirstTimeCalled = false;
        tiresWheelFirstTimeCalled = false;
        exteriorFirstTimeCalled = false;
        interiorFirstTimeCalled = false;
        testDriveFirstTimeCalled = false;
    }

    public static void clearMultipleImageAndVideoTempData() {
        powerTrain_picturePathsList.clear();
        powerTrain_uriArrayList.clear();
        mechanical_picturePathsList.clear();
        mechanical_uriArrayList.clear();
        tiresWheel_picturePathsList.clear();
        tiresWheel_uriArrayList.clear();
        exterior_picturePathsList.clear();
        exterior_uriArrayList.clear();
        interior_picturePathsList.clear();
        interior_uriArrayList.clear();
        testDrive_picturePathsList.clear();
        testDrive_uriArrayList.clear();
    }

    public static void clearInsExteriorImagesTempData() {
        dsRearPanel = null;
        dsRearDoor = null;
        dsFrontDoor = null;
        vin = null;
        dsFrontPanel = null;
        underCarriageBack = null;
        underCarriageFront = null;
        underCarriageRightSide = null;
        underCarriageLeftSide = null;
        underCarriage = null;
        rearPassengerSide = null;
        rearDriverSide = null;
        upperSide = null;
        rearSide = null;
        driverSide = null;
        passengerSide = null;
        carFront = null;
    }

    public static void clearInsInteriorImagesTempData() {
        engineLower = null;
        dsRearDoor = null;
        dsRearSeat = null;
        dsFrontCarpet = null;
        dsFrontDoor = null;
        dsFrontSeat = null;
        odoMeters = null;
    }

    public static void clearPowerTrainTempData() {
        engineBottomNoise_ = 0;
        automaticTransmission_ = 0;
        transferCaseOperation_ = 0;
        dsFrontDoor_ = 0;
        differentialOperation_ = 0;
        powerComments_ = null;
        picturePathsList_.clear();
    }

    public static void clearMechanicalTempData() {
        uploadVideoExactionEncode_ = null;
        uploadVideoStandEncode_ = null;
        uploadVideoStartCarEncode_ = null;
        addMultipleImagesEncode_ = null;
        engineUpperEnd_ = 0;
        engineBottomEnd_ = 0;
        catalyticConverter_ = 0;
        heaterRunsHot_ = 0;
        acRunsHot_ = 0;
        engineLightStartUp_ = 0;
        noAds_ = 0;
        noSRS_ = 0;
        MechanicalData.differentialOperation_ = 0;
        mechanicalComments_ = null;
        // picturePathsList_.clear();
    }

    public static void clearTiresWheelTempData() {
        treadDepth_ = 0;
        fourTiresCondition_ = 0;
        anyScratchesOnWheels_ = 0;
        tiresWheelComments_ = null;
    }

    public static void clearExteriorTempData() {
        noVisibleRust_ = 0;
        noColourFade_ = 0;
        noGlassDamaged_ = 0;
        noExteriorScratches_ = 0;
        noSideMirrorDamage_ = 0;
        exteriorComments_ = null;
    }

    public static void clearInteriorTempData() {
        noVisibleDamage_ = 0;
        frontSeatCondition_ = 0;
        backSeatCondition_ = 0;
        noMajorVisibleDamage_ = 0;
        interiorComments_ = null;
    }

    public static void clearTestDriveTempData() {
        automaticTransmission_ = 0;
        manualTransmission_ = 0;
        exilatorLevel_ = 0;
        breakingSenese_ = 0;
        steeringControls_ = 0;
        transferCase_ = 0;
        differential_ = 0;
        testDriveComments_ = null;
        uploadTestDriveVideoEncode_ = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                navController.navigate(R.id.action_global_nav_profile);
                break;
            case R.id.nav_about:
                navController.navigate(R.id.action_global_nav_about);
                break;
        }
        return false;
    }

}

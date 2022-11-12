package com.folkus.ui.login;

import static java.util.Collections.emptyMap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.folkus.R;
import com.folkus.data.remote.response.LoginData;

public class ActivitySplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UserViewModel userViewModel = new ViewModelProvider(this, new UserViewModelFactory(getApplicationContext())).get(UserViewModel.class);
        LoginData profileDetails = userViewModel.getProfileDetails();

        if(profileDetails != null && profileDetails.getInspector_id() > 0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityHome.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(ActivitySplashScreen.this, ActivityLogin.class);
                    startActivity(intent);
                    finish();
                }
            },2000);
        }

    }
}
package com.folkus;

import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.folkus.ui.login.NetworkChangeReceiver;

public class App extends Application {
    private Context context;
    NetworkChangeReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkChangeReceiver();
        registerReceiver(receiver, filter);
    }
}

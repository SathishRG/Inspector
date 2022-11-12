package com.folkus;

import android.app.Application;
import android.content.Context;

import com.folkus.ui.login.NetworkChangeReceiver;

public class App extends Application {
    private Context context;
    NetworkChangeReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

}

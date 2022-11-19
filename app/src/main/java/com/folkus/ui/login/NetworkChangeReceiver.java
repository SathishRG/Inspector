package com.folkus.ui.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkUtil.getConnectivityStatusString(context);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if (status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED) {
                //showDialog();
                Toast.makeText(context, "Please connect internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void showDialog() {
        Context context = new Activity().getApplicationContext();
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Internet Connection");
        alertDialog.setMessage("App required internet connection");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
                        NetworkChangeReceiver receiver = new NetworkChangeReceiver();
                        context.registerReceiver(receiver, filter);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}

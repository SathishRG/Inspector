package com.folkus.comman;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.folkus.R;


public class ProgressButton {

    private ProgressBar progressBar;
    private TextView textView;

    public ProgressButton(Context ct, View view) {
        progressBar = view.findViewById(R.id.progressBar);
        textView = view.findViewById(R.id.textView2);
    }

    public void buttonActivated() {
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);
    }

    public void buttonReverted() {
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }

    public void buttonFinished() {
        progressBar.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
    }
}

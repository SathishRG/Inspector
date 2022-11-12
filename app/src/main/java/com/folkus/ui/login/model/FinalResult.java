package com.folkus.ui.login.model;

import androidx.annotation.Nullable;


/**
 * Authentication result : success (user details) or error message.
 */
public class FinalResult {
    @Nullable
    private Object loginResponse;
    @Nullable
    private Integer error;

    public FinalResult(@Nullable Integer error) {
        this.error = error;
    }

    public FinalResult(@Nullable Object success) {
        this.loginResponse = success;
    }

    @Nullable
    public Object getSuccess() {
        return loginResponse;
    }

    @Nullable
    public Object getError() {
        return error;
    }
}
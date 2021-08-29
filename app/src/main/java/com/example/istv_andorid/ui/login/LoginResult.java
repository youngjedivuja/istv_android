package com.example.istv_andorid.ui.login;

import androidx.annotation.Nullable;

public class LoginResult {
    @Nullable
    private LoggedInUserView success;
    @Nullable
    private String error;

    LoginResult(@Nullable String error) {
        this.error = error;
    }

    LoginResult(@Nullable LoggedInUserView success) {
        this.success = success;
    }

    @Nullable
    LoggedInUserView getSuccess() {
        return success;
    }

    @Nullable
    String getError() {
        return error;
    }
}

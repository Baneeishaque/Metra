package com.example.metra;

import android.Manifest;

public abstract class ReceiveSmsPermissionActivity extends SinglePermissionActivity {

    @Override
    protected String passPermission() {
        return Manifest.permission.RECEIVE_SMS;
    }
}

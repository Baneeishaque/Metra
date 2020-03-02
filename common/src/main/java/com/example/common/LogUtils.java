package com.example.common;

import android.util.Log;

public class LogUtils {

    public static void debug(String message) {
        Log.d(ApplicationDetails.applicationName, message);
    }
}

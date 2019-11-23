package com.example.metra;

import android.util.Log;

public class LogUtils {

    static void debug(String message) {
        Log.d(ApplicationDetails.applicationName, message);
    }
}

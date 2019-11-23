package com.example.metra;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {

    public static void checkAndRequestPermission(String passedPermission, int permissionRequirementMessage, int passedPermissionRequestID, AppCompatActivity currentActivity) {

//        If the device running is former to Android 6.0, there is no need to call new runtime permission workflows.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        ShouldShowRequestPermissionRationale (String permission) can be called to determine if the user denied this permission previously or not.
            if (ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, Manifest.permission.RECEIVE_SMS)) {
//            If this method returns true, then it’s the perfect time to tell the user exactly why the permission is needed before requesting it again.
                Toast.makeText(currentActivity, currentActivity.getString(permissionRequirementMessage), Toast.LENGTH_LONG).show();
            } else {
//            If the user denies the permission request in the past and chooses the "Never ask again" option in the permission request system dialog, this method will return false. Next time we call requestPermissions, this dialog will not appear for this kind of permission anymore. Instead, it just does nothing.
                Toast.makeText(currentActivity, currentActivity.getString(permissionRequirementMessage), Toast.LENGTH_LONG).show();
            }

            //        Check whether required permission is granted. Call the ContextCompat.checkSelfPermission (Context, String) method, Context current context and String Permission. If the application has the permission, the method returns PackageManager.PERMISSION_GRANTED and the application can proceed with the operation. If the application does not have the permission, the method returns Package Manager.PERMISSION_DENIED and the application have to explicitly ask the user for permission.
            if (ContextCompat.checkSelfPermission(currentActivity, passedPermission) != PackageManager.PERMISSION_GRANTED) {
//            If the application doesn’t have the permission which requires, the application must call requestPermissions(String [ ] permissions, int requestCode) methods to ask the appropriate permissions. Pass permission array and request code as parameters.
                ActivityCompat.requestPermissions(currentActivity, new String[]{passedPermission}, passedPermissionRequestID);
            }
        }
    }
}

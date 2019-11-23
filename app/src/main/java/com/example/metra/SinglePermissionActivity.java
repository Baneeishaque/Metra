package com.example.metra;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public abstract class SinglePermissionActivity extends AppCompatActivity implements PermissionResultActions {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtils.checkAndRequestPermission(passPermission(), passPermissionRequirementText(), passPermissionRequestCode(), this);
    }

    protected abstract int passPermissionRequestCode();

    protected abstract int passPermissionRequirementText();

    protected abstract String passPermission();

    //    If the user has granted or denied the permission request, we have to handle response and execute the functionality according to response. This can be achieved by overriding the OnRequestPermissionsResult() in the Activity where the permission was requested. This method returns the result code for granted or denied permission.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //if the request is cancelled, the results array is empty
        if (requestCode == passPermissionRequestCode()) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //permission was granted
                Toast.makeText(this, "Thanks, " + getString(passPermissionRequirementText()), Toast.LENGTH_LONG).show();
                performPermissionGrantedActions();
            } else {
                //permission was not granted
                Toast.makeText(this, passPermissionRequirementText(), Toast.LENGTH_LONG).show();
                performPermissionDeniedActions();
            }
        } else {
            Toast.makeText(this, "Unexpected value: " + requestCode, Toast.LENGTH_LONG).show();
        }
    }

}

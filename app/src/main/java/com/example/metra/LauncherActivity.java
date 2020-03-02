package com.example.metra;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class LauncherActivity extends AppCompatActivity {

    final int RECEIVE_SMS_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReceiveSMS();
        } else {
            moveToMainScreen();
        }
    }

    private void getPermissionToReceiveSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {
                Toast.makeText(this, R.string.read_sms_permission_requirement, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, R.string.read_sms_permission_requirement, Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.RECEIVE_SMS}, RECEIVE_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == RECEIVE_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Receive SMS permission granted", Toast.LENGTH_SHORT).show();
                checkForPin();
            } else {
                Toast.makeText(this, "Receive SMS permission denied", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        } else {
            Toast.makeText(this, "Unexpected value: " + requestCode, Toast.LENGTH_LONG).show();
        }
    }

    private void moveToMainScreen() {

        startActivity(new Intent(this, ListMessagesActivity.class));
        this.finish();
    }

    private void checkForPin() {

        // Storing data into SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(ApplicationDetails.applicationName, MODE_PRIVATE);
        String pin = sharedPreferences.getString("pin", "");
        if (pin.isEmpty()) {

            //TODO : Alert Dialog Utils
            //TODO : Input Dialog Utils

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Pin...");

            //TODO : Style the input

            // Set up the input
            final EditText input = new EditText(this);
            // Specify the type of input expected
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint("Pin...");
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {

                //TODO : Pin Number Validation

                if (!input.getText().toString().isEmpty()) {

                    // Creating an Editor object to edit(write to the file)
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    // Storing the key and its value - as the data fetched from edit text
                    editor.putString("pin", input.getText().toString());
                    // Once the changes have been made, we need to commit to apply those changes made, otherwise, it will throw an error
                    editor.apply();

                    moveToMainScreen();

                } else {
                    checkForPin();
                }

            });

            builder.setCancelable(false);
            builder.show();

        } else {

            moveToMainScreen();
        }
    }
}

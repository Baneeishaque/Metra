package com.example.send_otp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessageMethod1(View view) {
        Snackbar.make(view, "Will Come Soon..", Snackbar.LENGTH_LONG)
                .show();
//        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        startActivity(sendIntent);
    }

    public void sendMessageMethod2(View view) {
        // add the phone number in the data
        Uri uri = Uri.parse("smsto:" + "+919446827218");
        Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // add the message at the sms_body extra field
        smsSIntent.putExtra("sms_body", "Test Message");
        startActivity(smsSIntent);
    }

    public void sendMessageMethod3(View view) {
        Snackbar.make(view, "Will Come Soon..", Snackbar.LENGTH_LONG)
                .show();
    }
}

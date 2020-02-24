package com.example.metra;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class SmsForwardTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_forward_test);
    }

    public void sendMessageMethod1(View view) {

        Snackbar.make(view, "Will Come Soon..", Snackbar.LENGTH_LONG)
                .show();
    }

    public void sendMessageMethod2(View view) {

        // add the phone number in the data
        Uri uri = Uri.parse("smsto:" + "+919895624669");
        Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
        // add the message at the sms_body extra field
        smsSIntent.putExtra("sms_body", "Hello");
        smsSIntent.putExtra("address", "+919895624669");
        startActivity(smsSIntent);
    }

    public void sendMessageMethod3(View view) {

        Snackbar.make(view, "Will Come Soon..", Snackbar.LENGTH_LONG)
                .show();
    }
}

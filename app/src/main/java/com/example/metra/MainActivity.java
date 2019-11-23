package com.example.metra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends ReceiveSmsPermissionActivity {

    final int receiveSmsRequestCode = 1;
    Context activityContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Will Come Soon..", Snackbar.LENGTH_LONG)
                        .show();
            }
        });
    }

    @Override
    protected int passPermissionRequestCode() {
        return receiveSmsRequestCode;
    }

    @Override
    protected int passPermissionRequirementText() {
        return R.string.read_sms_permission_requirement;
    }

    @Override
    public void performPermissionGrantedActions() {

    }

    @Override
    public void performPermissionDeniedActions() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.trusted_sources) {
            startActivity(new Intent(this, TrustedSourcesActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onResume() {
//
//        super.onResume();
//        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {
//
//            // App is not default.
//            // Show the "not currently set as the default SMS app" interface
//            new AlertDialog.Builder(this).setTitle("Caution!").setMessage("Metra is not your default SMS application, please correct it...")
//                    // Specifying a listener allows you to take an action before dismissing the dialog.
//                    // The dialog is automatically dismissed when a dialog button is clicked.
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // Continue with operation
//                            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
//                            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
//                            startActivity(intent);
//                        }
//                    })
//
//                    // A null listener allows the button to dismiss the dialog and take no further action.
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ((AppCompatActivity) activityContext).finishAffinity();
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
//    }
}

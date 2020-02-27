package com.example.metra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.buzz.vpn.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrustedAppsActivity extends AppCompatActivity {
    Context activityContext = this;
    private static final int PICK_UNTRUSTED_APPLICATION_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_apps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

//            //TODO : Alert Dialog Utils
//            //TODO : Input Dialog Utils
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
//            builder.setTitle("New Trusted App");
//
//            //TODO : Style the input
//
//            // Set up the input
//            final EditText input = new EditText(activityContext);
//            // Specify the type of input expected
//            //TODO : Pick from Contacts
//            input.setInputType(InputType.TYPE_CLASS_TEXT);
//            input.setHint("App Name.......");
//            builder.setView(input);
//
//            // Set up the buttons
//            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    //TODO : Phone Number Validation
//                    //TODO : Check for already existing number
//                    DatabaseHelper trustedAppsDatabaseHelper = new DatabaseHelper(activityContext);
//                    trustedAppsDatabaseHelper.insertTrustedApp(input.getText().toString());
//                    LogUtils.debug(input.getText().toString() + " Added to Trusted DB...");
////                        TODO : To Activity Utils
//                    startActivity(new Intent(activityContext, TrustedAppsActivity.class));
//                    ((AppCompatActivity) activityContext).finish();
//                }
//            });
//            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                }
//            });
//            builder.show();

            startActivityForResult(new Intent(this, PickApplicationActivity.class), PICK_UNTRUSTED_APPLICATION_REQUEST);
        });

        DatabaseHelper trustedAppsDatabaseHelper = new DatabaseHelper(this);

        //instantiate custom adapter
        TrustedAppsListAdapter adapter = new TrustedAppsListAdapter(trustedAppsDatabaseHelper.getAllTrustedApps(), this);

        //handle listView and assign adapter
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_trusted_apps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.launch_vpn) {

            startActivity(new Intent(this, MainActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        String application = data.getStringExtra("result");
        // Check which request we're responding to
        if (requestCode == PICK_UNTRUSTED_APPLICATION_REQUEST) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                //TODO : Check for already existing app name
                DatabaseHelper trustedAppsDatabaseHelper = new DatabaseHelper(activityContext);
                trustedAppsDatabaseHelper.insertTrustedApp(application);
                LogUtils.debug(application + " Added to Trusted DB...");
                //TODO : To Activity Utils
                startActivity(new Intent(activityContext, TrustedAppsActivity.class));
                ((AppCompatActivity) activityContext).finish();

                LogUtils.debug("Picked Application is : " + application);
            }
        }
    }
}

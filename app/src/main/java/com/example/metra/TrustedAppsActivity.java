package com.example.metra;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.buzz.vpn.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrustedAppsActivity extends AppCompatActivity {
    Context activityContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_apps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO : Alert Dialog Utils
                //TODO : Input Dialog Utils

                AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
                builder.setTitle("New Trusted App");

                //TODO : Style the input

                // Set up the input
                final EditText input = new EditText(activityContext);
                // Specify the type of input expected
                //TODO : Pick from Contacts
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("App Name.......");
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO : Phone Number Validation
                        //TODO : Check for already existing number
                        DatabaseHelper trustedAppsDatabaseHelper = new DatabaseHelper(activityContext);
                        trustedAppsDatabaseHelper.insertTrustedApp(input.getText().toString());
                        LogUtils.debug(input.getText().toString() + " Added to Trusted DB...");
//                        TODO : To Activity Utils
                        startActivity(new Intent(activityContext, TrustedAppsActivity.class));
                        ((AppCompatActivity) activityContext).finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
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
}

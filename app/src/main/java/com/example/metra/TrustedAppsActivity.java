package com.example.metra;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buzz.vpn.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TrustedAppsActivity extends AppCompatActivity {

    Context activityContext = this;
    private static final int PICK_UNTRUSTED_APPLICATION_REQUEST = 1;
    ApplicationsRecyclerViewAdapterWithDelete communicationOriginsRecyclerViewAdapter;
    private ArrayList<String> communicationOrigins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_apps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            startActivityForResult(new Intent(this, PickApplicationActivity.class), PICK_UNTRUSTED_APPLICATION_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        communicationOrigins.clear();
        DatabaseHelper trustedAppsDatabaseHelper = new DatabaseHelper(this);
        ArrayList<TrustedApp> trustedApps = trustedAppsDatabaseHelper.getAllTrustedApps();
        for (TrustedApp trustedApp : trustedApps) {
            communicationOrigins.add(trustedApp.getApp_name());
        }
        communicationOriginsRecyclerViewAdapter = new ApplicationsRecyclerViewAdapterWithDelete(activityContext, communicationOrigins);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(communicationOriginsRecyclerViewAdapter);

        communicationOriginsRecyclerViewAdapter.SetOnItemClickListener((view, position, communicationOrigin) -> {

            Intent intent = getPackageManager().getLaunchIntentForPackage(communicationOrigin);
            if (intent != null) {

                startActivity(intent);
            } else {

                Toast.makeText(this, communicationOrigin + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
            }
        });

        communicationOriginsRecyclerViewAdapter.SetOnDeleteButtonClickListener((position, trustedApp) -> {

            //TODO : Confirmation
            trustedAppsDatabaseHelper.deleteTrustedAppByName(communicationOrigins.get(position));
            communicationOrigins.remove(position);
            communicationOriginsRecyclerViewAdapter.updateList(communicationOrigins);
        });
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

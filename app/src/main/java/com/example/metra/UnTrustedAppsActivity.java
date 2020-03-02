package com.example.metra;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.buzz.vpn.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class UnTrustedAppsActivity extends AppCompatActivity {

    Context activityContext = this;
    private static final int PICK_UNTRUSTED_APPLICATION_REQUEST = 1;
    ApplicationsRecyclerViewAdapterWithDelete communicationOriginsRecyclerViewAdapter;
    private ArrayList<String> communicationOrigins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_untrusted_apps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {

            startActivityForResult(new Intent(this, PickApplicationActivity.class), PICK_UNTRUSTED_APPLICATION_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        communicationOrigins.clear();
        DatabaseHelper trustedAppsDatabaseHelper = new DatabaseHelper(this);
        ArrayList<UnTrustedApp> unTrustedApps = trustedAppsDatabaseHelper.getAllTrustedApps();
        for (UnTrustedApp unTrustedApp : unTrustedApps) {
            communicationOrigins.add(unTrustedApp.getApp_name());
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

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(this.getComponentName()));

        //changing edit text color
        EditText searchEdit = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEdit.setTextColor(Color.WHITE);
        searchEdit.setHintTextColor(Color.WHITE);
        searchEdit.setBackgroundColor(Color.TRANSPARENT);
        searchEdit.setHint("Search");

        InputFilter[] fArray = new InputFilter[2];
        fArray[0] = new InputFilter.LengthFilter(40);
        fArray[1] = (source, start, end, dest, dstart, dend) -> {

            for (int i = start; i < end; i++) {

                if (!Character.isLetterOrDigit(source.charAt(i)))
                    return "";
            }
            return null;
        };
        searchEdit.setFilters(fArray);

        View v = searchView.findViewById(androidx.appcompat.R.id.search_plate);
        v.setBackgroundColor(Color.TRANSPARENT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                ArrayList<String> filterList = new ArrayList<>();

                if (s.length() > 0) {

                    for (int i = 0; i < communicationOrigins.size(); i++) {

                        if (communicationOrigins.get(i).toLowerCase().contains(s.toLowerCase())) {

                            filterList.add(communicationOrigins.get(i));
                            communicationOriginsRecyclerViewAdapter.updateList(filterList);
                        }
                    }
                } else {

                    communicationOriginsRecyclerViewAdapter.updateList(communicationOrigins);
                }
                return false;
            }
        });
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

        // Check which request we're responding to
        if (requestCode == PICK_UNTRUSTED_APPLICATION_REQUEST) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                String application = data.getStringExtra("result");

                //TODO : Check for already existing app name
                DatabaseHelper trustedAppsDatabaseHelper = new DatabaseHelper(activityContext);
                trustedAppsDatabaseHelper.insertTrustedApp(application);
                LogUtils.debug(application + " Added to Trusted DB...");

                //TODO : To Activity Utils
                startActivity(new Intent(activityContext, UnTrustedAppsActivity.class));
                ((AppCompatActivity) activityContext).finish();

                LogUtils.debug("Picked Application is : " + application);
            }
        }
    }
}

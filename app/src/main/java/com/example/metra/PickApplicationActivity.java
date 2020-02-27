package com.example.metra;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

public class PickApplicationActivity extends AppCompatActivity {

    Context activityContext = this;
    ApplicationsRecyclerViewAdapter communicationOriginsRecyclerViewAdapter;
    private ArrayList<String> communicationOrigins = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_application);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        communicationOrigins.clear();
        ApkInfoExtractor apkInfoExtractor = new ApkInfoExtractor(this);
        communicationOrigins.addAll(apkInfoExtractor.GetAllInstalledApkInfo());
        communicationOriginsRecyclerViewAdapter = new ApplicationsRecyclerViewAdapter(activityContext, communicationOrigins);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(communicationOriginsRecyclerViewAdapter);

        communicationOriginsRecyclerViewAdapter.SetOnItemClickListener((view, position, communicationOrigin) -> {

//            Intent intent = getPackageManager().getLaunchIntentForPackage(communicationOrigin);
//            if (intent != null) {
//
//                startActivity(intent);
//            } else {
//
//                Toast.makeText(this, communicationOrigin + " Error, Please Try Again.", Toast.LENGTH_LONG).show();
//            }

            Intent returnIntent = new Intent();
            returnIntent.putExtra("result", communicationOrigin);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_search_only, menu);

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
}

package com.example.metra;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputFilter;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.CommunicationOrigin;
import com.example.common.DatabaseHelper;
import com.example.common.LogUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class ToBeSecureCommunicationOriginsActivity extends AppCompatActivity {

    Context activityContext = this;

    private static final int INSERT_CONTACT_REQUEST = 2;
    FloatingActionButton fab_from_contacts;
    FloatingActionButton fab_from_input;

    private ArrayList<CommunicationOrigin> communicationOrigins = new ArrayList<>();
    private boolean isFABOpen;

    private int SELECT_PHONE_NUMBER = 1;
    private CommunicationOriginsRecyclerViewAdapter communicationOriginsRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_secure_communication_origins);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab_from_input = findViewById(R.id.fab_from_input);

        fab_from_contacts = findViewById(R.id.fab_from_contacts);
        fab_from_contacts.setOnClickListener(v -> {

            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, SELECT_PHONE_NUMBER);
        });

        fab.setOnClickListener(view -> {

            if (!isFABOpen) {

                showFABMenu();

            } else {

                closeFABMenu();
            }
        });

        fab_from_input.setOnClickListener(view -> {

            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType(ContactsContract.Contacts.CONTENT_TYPE);
            // Sets the special extended data for navigation
            intent.putExtra("finishActivityOnSaveCompleted", true);
            startActivityForResult(intent, INSERT_CONTACT_REQUEST);
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        communicationOrigins.clear();
        DatabaseHelper trustedSourcesDatabaseHelper = new DatabaseHelper(this);
        communicationOrigins.addAll(trustedSourcesDatabaseHelper.getAllCommunicationOrigins());
        communicationOriginsRecyclerViewAdapter = new CommunicationOriginsRecyclerViewAdapter(activityContext, communicationOrigins);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(communicationOriginsRecyclerViewAdapter);

        communicationOriginsRecyclerViewAdapter.SetOnItemClickListener((view, position, communicationOrigin) -> {

        });

        communicationOriginsRecyclerViewAdapter.SetOnDeleteButtonClickListener((communicationOrigin, position) -> {

            //TODO : Confirmation
            trustedSourcesDatabaseHelper.deleteCommunicationOriginById(communicationOrigins.get(position));
            communicationOrigins.remove(position);
            communicationOriginsRecyclerViewAdapter.updateList(communicationOrigins);
        });
    }

    private void showFABMenu() {

        isFABOpen = true;
        fab_from_contacts.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fab_from_input.animate().translationY(-getResources().getDimension(R.dimen.standard_105));
    }

    private void closeFABMenu() {

        isFABOpen = false;
        fab_from_contacts.animate().translationY(0);
        fab_from_input.animate().translationY(0);
    }

    private void addPhoneNumberToDb(String name, String phoneNumber) {

        DatabaseHelper trustedSourcesDatabaseHelper = new DatabaseHelper(activityContext);
        trustedSourcesDatabaseHelper.insertCommunicationOrigin(name, phoneNumber);

        LogUtils.debug(name + " Added to Trusted DB...");

        //TODO : To Activity Utils
        startActivity(new Intent(activityContext, ToBeSecureCommunicationOriginsActivity.class));
        ((AppCompatActivity) activityContext).finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Check which request we're responding to
        if (requestCode == SELECT_PHONE_NUMBER) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                // Get the URI and query the content provider for the phone number
                Uri contactUri = Objects.requireNonNull(data).getData();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER};
                Cursor cursor = this.getContentResolver().query(Objects.requireNonNull(contactUri), projection,
                        null, null, null);

                // If the cursor returned is valid, get the phone number
                if (cursor != null && cursor.moveToFirst()) {

                    int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
                    addPhoneNumberToDb(cursor.getString(nameIndex), cursor.getString(numberIndex));
                }
                Objects.requireNonNull(cursor).close();
            }
        } else if (requestCode == INSERT_CONTACT_REQUEST) {

            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                Toast.makeText(this, "Added Successfully...", Toast.LENGTH_LONG).show();

//                ContentResolver cr = getContentResolver();
//                Cursor cursor = cr.query(Objects.requireNonNull(Objects.requireNonNull(data).getData()), null, null, null, null);
//                if (cursor != null && cursor.moveToFirst()) {
//                    String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
//                    String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
//                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
//                        //Has phoneNumber
//                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
//                        while (pCur != null && pCur.moveToNext()) {
//                            String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            Log.d("SteveMoretz", "NAME : " + name + " phoneNo : " + phoneNo);
//                        }
//                        if (pCur != null) {
//                            pCur.close();
//                        }
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "User canceled adding contacts", Toast.LENGTH_SHORT).show();
//                }
//                if (cursor != null) {
//                    cursor.close();
//                }
            }
        }
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

                ArrayList<CommunicationOrigin> filterList = new ArrayList<>();

                if (s.length() > 0) {

                    for (int i = 0; i < communicationOrigins.size(); i++) {

                        if (communicationOrigins.get(i).getName().toLowerCase().contains(s.toLowerCase()) || communicationOrigins.get(i).getPhoneNumber().toLowerCase().contains(s.toLowerCase())) {

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

package com.example.metra;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrustedSourcesActivity extends AppCompatActivity {

    Context activityContext = this;

    FloatingActionButton fab_from_contacts;
    FloatingActionButton fab_from_input;
    private boolean isFABOpen;
    private int SELECT_PHONE_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_sources);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab_from_contacts = findViewById(R.id.fab_from_contacts);
        fab_from_input = findViewById(R.id.fab_from_input);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!isFABOpen) {

                    showFABMenu();

                } else {

                    closeFABMenu();
                }

            }
        });

        fab_from_input.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //TODO : Alert Dialog Utils
                //TODO : Input Dialog Utils

                AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
                builder.setTitle("New Trusted Source");

                //TODO : Style the input
                //TODO : Pick from Contacts

                // Set up the input
                final EditText input = new EditText(activityContext);
                // Specify the type of input expected
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                input.setHint("Phone Number...");
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO : Phone Number Validation
                        //TODO : Check for already existing number

                        addPhoneNumberToDb(input.getText().toString());
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

        fab_from_contacts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_PICK);
                i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(i, SELECT_PHONE_NUMBER);
            }
        });

        DatabaseHelper trustedSourcesDatabaseHelper = new DatabaseHelper(this);

        //instantiate custom adapter
        TrustedSourcesListAdapter adapter = new TrustedSourcesListAdapter(trustedSourcesDatabaseHelper.getAllTrustedSources(), this);

        //handle listView and assign adapter
        ListView listView = findViewById(R.id.list_view);
        listView.setAdapter(adapter);
    }

    private void addPhoneNumberToDb(String phoneNumber) {

        DatabaseHelper trustedSourcesDatabaseHelper = new DatabaseHelper(activityContext);
        trustedSourcesDatabaseHelper.insertTrustedSource(phoneNumber);

        LogUtils.debug(phoneNumber + " Added to Trusted DB...");

        //TODO : To Activity Utils
        startActivity(new Intent(activityContext, TrustedSourcesActivity.class));
        ((AppCompatActivity) activityContext).finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Get the URI and query the content provider for the phone number
        Uri contactUri = data.getData();
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER};
        Cursor cursor = this.getContentResolver().query(contactUri, projection,
                null, null, null);

        // If the cursor returned is valid, get the phone number
        if (cursor != null && cursor.moveToFirst()) {

            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            addPhoneNumberToDb(cursor.getString(numberIndex));
        }
        cursor.close();
    }
}

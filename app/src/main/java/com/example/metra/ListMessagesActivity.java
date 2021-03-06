package com.example.metra;

import android.Manifest;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.text.InputFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.common.DatabaseHelper;
import com.example.common.LogUtils;
import com.example.common.Sms;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

public class ListMessagesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    private SmsInboxRecyclerViewAdapter smsInboxRecyclerViewAdapter;
    private ArrayList<Sms> smsArrayList = new ArrayList<>();

    static ListMessagesActivity inst;
    static boolean active = false;
    private static final int READ_CONTACTS_PERMISSION_REQUEST_CODE = 2;
    final int READ_SMS_PERMISSION_REQUEST_CODE = 1;
    Context activityContext = this;

    public static ListMessagesActivity instance() {
        return inst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_messages);
        findViews();
        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> startActivity(new Intent(activityContext, SendMessageActivity.class)));

        //TODO : Pre check for service
        startService(new Intent(this, QuickResponseService.class));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            getPermissionToReadSMS();

        } else {

            refreshSmsInbox();
        }
    }

    public void getPermissionToReadSMS() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {

                Toast.makeText(this, "Please allow permission! - Read SMS", Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(this, "Please allow permission! - Read SMS", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    public void refreshSmsInbox() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            getPermissionToReadContacts();

        } else {

            ContentResolver contentResolver = getContentResolver();
            Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), new String[]{"date", "address", "body"}, null, null, "date DESC LIMIT 5");
            int indexAddress = Objects.requireNonNull(smsInboxCursor).getColumnIndex("address");
            int indexBody = smsInboxCursor.getColumnIndex("body");
            if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;

            smsArrayList.clear();
            do {

                smsArrayList.add(new Sms(getContactName(smsInboxCursor.getString(indexAddress), this), smsInboxCursor.getString(indexBody)));

            } while (smsInboxCursor.moveToNext());

            smsInboxRecyclerViewAdapter = new SmsInboxRecyclerViewAdapter(activityContext, smsArrayList);
            recyclerView.setHasFixedSize(true);

            // use a linear layout manager
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            recyclerView.setAdapter(smsInboxRecyclerViewAdapter);

            smsInboxRecyclerViewAdapter.SetOnItemClickListener((view, position, model) -> {
            });

            smsInboxRecyclerViewAdapter.SetOnForwardButtonClickListener(model -> {

                // add the phone number in the data
                Uri uri = Uri.parse("smsto:" + model.getAddress());
                Intent smsSIntent = new Intent(Intent.ACTION_SENDTO, uri);
                // add the message at the sms_body extra field
                smsSIntent.putExtra("address", model.getAddress());
                smsSIntent.putExtra("sms_body", model.getMessage());
                startActivity(smsSIntent);
            });

            smsInboxCursor.close();

            DatabaseHelper trustedAppsDatabaseHelper = new DatabaseHelper(this);
            smsArrayList.addAll(0, trustedAppsDatabaseHelper.getAllMessages());
        }


    }

    private void getPermissionToReadContacts() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {

            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {

                Toast.makeText(this, "Need to read contacts...", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(this, "Need to read contacts...", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == READ_SMS_PERMISSION_REQUEST_CODE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();

            } else {

                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }

        } else if (requestCode == READ_CONTACTS_PERMISSION_REQUEST_CODE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                refreshSmsInbox();

            } else {

                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        } else {

            Toast.makeText(this, "Unexpected value: " + requestCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {

        super.onStop();
        active = false;
    }

    @Override
    public void onStart() {

        super.onStart();
        active = true;
        inst = this;
    }

    public void updateInbox(String sender, String smsMessage) {

        smsArrayList.add(0, new Sms(sender, smsMessage));
        smsInboxRecyclerViewAdapter.updateList(smsArrayList);
    }

    @Override
    protected void onResume() {

        super.onResume();
        checkDefaultSMSApplication();
    }

    private void checkDefaultSMSApplication() {

        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(getPackageName())) {

            // App is not default.
            // Show the "not currently set as the default SMS app" interface
            new AlertDialog.Builder(this).setTitle("Caution!").setMessage("MeTra is not your default SMS application, please correct it...")
                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with operation
                            Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, getPackageName());
                            startActivity(intent);
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finishAffinity();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    private void findViews() {

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_sms_inbox, menu);

        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));

        SearchManager searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(this.getComponentName()));

        //changing edittext color
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

                ArrayList<Sms> filterList = new ArrayList<Sms>();

                if (s.length() > 0) {

                    for (int i = 0; i < smsArrayList.size(); i++) {

                        if (smsArrayList.get(i).getAddress().toLowerCase().contains(s.toLowerCase()) || smsArrayList.get(i).getMessage().toLowerCase().contains(s.toLowerCase())) {

                            filterList.add(smsArrayList.get(i));
                            smsInboxRecyclerViewAdapter.updateList(filterList);
                        }
                    }

                } else {

                    smsInboxRecyclerViewAdapter.updateList(smsArrayList);
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.trusted_sources) {

            startActivity(new Intent(this, ToBeSecureCommunicationOriginsActivity.class));

        } else if (item.getItemId() == R.id.trusted_apps) {

            startActivity(new Intent(this, TrustedAppsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public String getContactName(final String phoneNumber, Context context) {


        String contactName = phoneNumber;

        try {
            Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER + " = ?", new String[]{phoneNumber}, null);

            if (cursor != null && cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            }

            if (cursor != null) {
                cursor.close();
            }

            LogUtils.debug("Contact : " + contactName);

        } catch (Exception e) {

            LogUtils.debug("E : " + e.getLocalizedMessage());
        }
        return contactName;
    }
}

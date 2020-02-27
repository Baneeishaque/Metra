package com.example.metra;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ToBeSecureCommunicationOriginsActivity extends AppCompatActivity {

    Context activityContext = this;
    FloatingActionButton fab_from_contacts;
    private RecyclerView recyclerView;
    private ArrayList<CommunicationOrigin> communicationOrigins = new ArrayList<>();
    private int SELECT_PHONE_NUMBER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_be_secure_communication_origins);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_from_contacts = findViewById(R.id.fab_from_contacts);
        fab_from_contacts.setOnClickListener(v -> {

            Intent i = new Intent(Intent.ACTION_PICK);
            i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
            startActivityForResult(i, SELECT_PHONE_NUMBER);
        });

        findViews();
        refreshSmsInbox();
    }

    private void addPhoneNumberToDb(String name, String phoneNumber) {

        DatabaseHelper trustedSourcesDatabaseHelper = new DatabaseHelper(activityContext);
        trustedSourcesDatabaseHelper.insertCommunicationOrigin(name, phoneNumber);

        LogUtils.debug(phoneNumber + " Added to Trusted DB...");

        //TODO : To Activity Utils
        startActivity(new Intent(activityContext, ToBeSecureCommunicationOriginsActivity.class));
        ((AppCompatActivity) activityContext).finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Get the URI and query the content provider for the phone number
        Uri contactUri = data.getData();
        String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER};
        Cursor cursor = this.getContentResolver().query(contactUri, projection,
                null, null, null);

        // If the cursor returned is valid, get the phone number
        if (cursor != null && cursor.moveToFirst()) {

            int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER);
            addPhoneNumberToDb(cursor.getString(nameIndex), cursor.getString(numberIndex));
        }
        cursor.close();
    }

//    public void getPermissionToReadSMS() {
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
//
//            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
//
//                Toast.makeText(this, "Please allow permission! - Read SMS", Toast.LENGTH_SHORT).show();
//
//            } else {
//
//                Toast.makeText(this, "Please allow permission! - Read SMS", Toast.LENGTH_SHORT).show();
//            }
//            requestPermissions(new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST_CODE);
//        }
//    }

    public void refreshSmsInbox() {

//        ContentResolver contentResolver = getContentResolver();
//        Cursor smsInboxCursor = contentResolver.query(Uri.parse("content://sms/inbox"), new String[]{"date", "address", "body"}, null, null, "date DESC LIMIT 5");
//        int indexAddress = Objects.requireNonNull(smsInboxCursor).getColumnIndex("address");
//        int indexBody = smsInboxCursor.getColumnIndex("body");
//        if (indexBody < 0 || !smsInboxCursor.moveToFirst()) return;

        communicationOrigins.clear();
//        do {
//
//            communicationOrigins.add(new CommunicationOrigin("Name", "Phone Number"));
//
//        } while (smsInboxCursor.moveToNext());

        DatabaseHelper trustedSourcesDatabaseHelper = new DatabaseHelper(this);
//        ArrayList<CommunicationOrigin> trustedSources = trustedSourcesDatabaseHelper.getAllCommunicationOrigins();
        communicationOrigins.addAll(trustedSourcesDatabaseHelper.getAllCommunicationOrigins());
//        for (CommunicationOrigin trustedSource : trustedSources
//        ) {
//            communicationOrigins.add(new CommunicationOrigin("Name", trustedSource.getPhoneNumber()));
//        }
        //    private Toolbar toolbar;
        //    private FloatingActionButton fab;
        //
        CommunicationOriginsRecyclerViewAdapter communicationOriginsRecyclerViewAdapter = new CommunicationOriginsRecyclerViewAdapter(activityContext, communicationOrigins);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(communicationOriginsRecyclerViewAdapter);

        communicationOriginsRecyclerViewAdapter.SetOnItemClickListener((view, position, communicationOrigin) -> {

        });

        communicationOriginsRecyclerViewAdapter.SetOnDeleteButtonClickListener((communicationOrigin, position) -> {
//                TODO : Confirmation
            trustedSourcesDatabaseHelper.deleteCommunicationOriginById(communicationOrigins.get(position));
            communicationOrigins.remove(position);
            communicationOriginsRecyclerViewAdapter.updateList(communicationOrigins);
        });

//        smsInboxCursor.close();
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//
//        if (requestCode == READ_SMS_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
//                refreshSmsInbox();
//
//            } else {
//
//                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
//                finishAffinity();
//            }
//        } else {
//
//            Toast.makeText(this, "Unexpected value: " + requestCode, Toast.LENGTH_LONG).show();
//        }
//    }
//
//    @Override
//    public void onStop() {
//
//        super.onStop();
//        active = false;
//    }
//
//    @Override
//    public void onStart() {
//
//        super.onStart();
//        active = true;
//        inst = this;
//    }
//
//    public void updateInbox(String sender, String smsMessage) {
//
//        communicationOrigins.add(0, new Sms(sender, smsMessage));
//        communicationOriginsRecyclerViewAdapter.updateList(communicationOrigins);
//    }
//
//    @Override
//    protected void onResume() {
//
//        super.onResume();
//        checkDefaultSMSApplication();
//    }

//    private void checkDefaultSMSApplication() {
//
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
//                            finishAffinity();
//                        }
//                    })
//                    .setIcon(android.R.drawable.ic_dialog_alert)
//                    .show();
//        }
//    }

    private void findViews() {

//        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
//        fab = findViewById(R.id.fab);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        super.onCreateOptionsMenu(menu);
////        getMenuInflater().inflate(R.menu.menu_search, menu);
////
////        // Retrieve the SearchView and plug it into SearchManager
////        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
////
////        SearchManager searchManager = (SearchManager) this.getSystemService(SEARCH_SERVICE);
////        searchView.setSearchableInfo(Objects.requireNonNull(searchManager).getSearchableInfo(this.getComponentName()));
////
////        //changing edittext color
////        EditText searchEdit = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
////        searchEdit.setTextColor(Color.WHITE);
////        searchEdit.setHintTextColor(Color.WHITE);
////        searchEdit.setBackgroundColor(Color.TRANSPARENT);
////        searchEdit.setHint("Search");
////
////        InputFilter[] fArray = new InputFilter[2];
////        fArray[0] = new InputFilter.LengthFilter(40);
////        fArray[1] = new InputFilter() {
////
////            @Override
////            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
////
////                for (int i = start; i < end; i++) {
////
////                    if (!Character.isLetterOrDigit(source.charAt(i)))
////                        return "";
////                }
////                return null;
////            }
////        };
////        searchEdit.setFilters(fArray);
////
////        View v = searchView.findViewById(androidx.appcompat.R.id.search_plate);
////        v.setBackgroundColor(Color.TRANSPARENT);
////
////        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
////
////            @Override
////            public boolean onQueryTextSubmit(String s) {
////                return false;
////            }
////
////            @Override
////            public boolean onQueryTextChange(String s) {
////
////                ArrayList<Sms> filterList = new ArrayList<Sms>();
////
////                if (s.length() > 0) {
////
////                    for (int i = 0; i < communicationOrigins.size(); i++) {
////
////                        if (communicationOrigins.get(i).getAddress().toLowerCase().contains(s.toLowerCase()) || communicationOrigins.get(i).getMessage().toLowerCase().contains(s.toLowerCase())) {
////
////                            filterList.add(communicationOrigins.get(i));
////                            communicationOriginsRecyclerViewAdapter.updateList(filterList);
////                        }
////                    }
////
////                } else {
////
////                    communicationOriginsRecyclerViewAdapter.updateList(communicationOrigins);
////                }
////                return false;
////            }
////        });
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        if (item.getItemId() == R.id.trusted_sources) {
//
//            startActivity(new Intent(this, TrustedSourcesActivity.class));
//
//        } else if (item.getItemId() == R.id.trusted_apps) {
//
//            startActivity(new Intent(this, TrustedAppsActivity.class));
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

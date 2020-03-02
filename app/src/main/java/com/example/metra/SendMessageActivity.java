package com.example.metra;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.common.ApplicationDetails;
import com.example.common.DatabaseHelper;

import java.util.Objects;

public class SendMessageActivity extends AppCompatActivity {

    final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;

    Context activityContext = this;

    EditText editTextPhoneNumber, editTextMessageBody;

    boolean securityFlag = false;

    String existingSender;

    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        editTextPhoneNumber = findViewById(R.id.edit_text_phone_number);
        editTextMessageBody = findViewById(R.id.edit_text_message);
        buttonSend = findViewById(R.id.button_send);

        buttonSend.setOnClickListener(v -> {

            if (ContextCompat.checkSelfPermission(activityContext, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

                getPermissionToSendSMS();

            } else {

                sendSMS();
            }
        });

        handleIncomingData();
    }

    private void sendSMS() {

        String messageContent = editTextMessageBody.getText().toString();

        if (!messageContent.isEmpty()) {

            DatabaseHelper messagesDatabaseHelper = new DatabaseHelper(this);

            existingSender = messagesDatabaseHelper.checkMessage(messageContent);

            if (!existingSender.isEmpty()) {

                securityFlag = true;
            }
        }

        if (securityFlag) {

            new AlertDialog.Builder(this).setTitle("Caution!").setMessage("Trying to forward a message from trusted source - " + existingSender + ", Continue?")

                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Enter Pin...");

                        // Set up the input
                        final EditText input = new EditText(this);
                        // Specify the type of input expected
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        input.setHint("Pin...");
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", (dialog2, which2) -> {

                            // Storing data into SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences(ApplicationDetails.applicationName, MODE_PRIVATE);
                            String pin = sharedPreferences.getString("pin", "");

                            if (input.getText().toString().equals(pin)) {

//                                openContextMenu(buttonSend);

                                registerForContextMenu(buttonSend);
                                openContextMenu(buttonSend);
                                unregisterForContextMenu(buttonSend);

                            } else {

                                Toast.makeText(this, "Invalid Pin...", Toast.LENGTH_LONG).show();
                            }

                        });

                        builder.setCancelable(false);
                        builder.show();
                    })

                    .setNegativeButton(android.R.string.no, (dialog, which) -> {
                    })

                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {

            sendIt();
        }
    }

    private void sendIt() {

        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(editTextPhoneNumber.getText().toString(), null, editTextMessageBody.getText().toString(), null, null);

        Toast.makeText(activityContext, "Message sent!", Toast.LENGTH_SHORT).show();

        editTextMessageBody.setText("");
    }

    private void getPermissionToSendSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
                Toast.makeText(this, "Please allow permission! - Send SMS", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please allow permission! - Send SMS", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Send SMS permission granted", Toast.LENGTH_SHORT).show();
                sendSMS();
            } else {
                Toast.makeText(this, "Send SMS permission denied", Toast.LENGTH_SHORT).show();
                finishAffinity();
            }
        } else {
            Toast.makeText(this, "Unexpected value: " + requestCode, Toast.LENGTH_LONG).show();
        }
    }

    private void handleIncomingData() {

        // Get intent & action
        Intent intent = getIntent();
        String action = intent.getAction();

        if (Objects.equals(action, Intent.ACTION_SENDTO)) {

            editTextMessageBody.setText(intent.getStringExtra("sms_body"));
            editTextPhoneNumber.setText(intent.getStringExtra("address"));
        }

        //TODO : Code for other forward methods
//        String type = intent.getType();
//        if (Intent.ACTION_SEND.equals(action) && type != null) {
//            if ("text/plain".equals(type)) {
//                // Handle text being sent
//                String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
//                if (sharedText != null) {
//                    // Update UI to reflect text being shared
//                    LogUtils.debug("Received : " + sharedText);
//                }
//            }
//        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Forward Via. MSG");
        menu.add(0, v.getId(), 0, "Forward Via. N/W");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle() == "Forward Via. MSG") {

            sendIt();

        } else if (item.getTitle() == "Forward Via. N/W") {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, editTextMessageBody.getText().toString());
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);

            editTextMessageBody.setText("");
        }
        return super.onContextItemSelected(item);
    }
}

package com.example.metra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.Objects;

//<!-- SMS Receiver -->
//a receiver which will be triggered whenever the device receives an SMS
//This is a broadcast receiver class which will be triggered whenever user device receives the SMS.

// an Intent broadcast.
public class SmsReceiver extends BroadcastReceiver {

    // This method is called when the BroadcastReceiver is receiving
    @Override
    public void onReceive(Context context, Intent intent) {

        if (Objects.equals(intent.getAction(), "android.provider.Telephony.SMS_RECEIVED")) {

            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {

                    Object[] pdusObjects = (Object[]) bundle.get("pdus");
                    for (Object pdusObject : pdusObjects != null ? pdusObjects : new Object[0]) {

                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObject, bundle.getString("format"));
                        String senderAddress = currentMessage.getDisplayOriginatingAddress();
                        String messageContent = currentMessage.getDisplayMessageBody();
                        LogUtils.debug("Received SMS: " + messageContent + ", Sender: " + senderAddress);

                        if (ListMessagesActivity.active) {
                            ListMessagesActivity inst = ListMessagesActivity.instance();
//                            inst.updateInbox("\nFrom: " + senderAddress + "\n" + messageContent + "\n");
                            inst.updateInbox(senderAddress, messageContent);
                        }

                        // if the SMS is not from our trusted sources, ignore the message
                        DatabaseHelper trustedSourcesDatabaseHelper = new DatabaseHelper(context);

                        if (trustedSourcesDatabaseHelper.checkSmsSender(senderAddress.toLowerCase())) {

                            DatabaseHelper messagesDatabaseHelper = new DatabaseHelper(context);

                            messagesDatabaseHelper.insertMessage(senderAddress, messageContent);

                            Toast.makeText(context, "SMS added to Metra Database...", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } catch (Exception e) {

                LogUtils.debug("Exception: " + e.getMessage());
            }
        }
    }
}

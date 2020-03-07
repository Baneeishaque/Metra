package com.example.metra;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.common.ApplicationDetails;

public class EnterPinActivity extends AppCompatActivity {


    EditText editTextPin;
    Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pin);

//        setTitle("Enter Pin...");

        editTextPin = findViewById(R.id.edit_text_pin);
        buttonSend = findViewById(R.id.button_send);

        buttonSend.setOnClickListener(v -> {

            SharedPreferences sharedPreferences = getSharedPreferences(ApplicationDetails.applicationName, MODE_PRIVATE);
            String pin = sharedPreferences.getString("pin", "");

            if (editTextPin.getText().toString().equals(pin)) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", "OK");
                setResult(Activity.RESULT_OK, returnIntent);
                finish();

            } else {

                Toast.makeText(this, "Invalid Pin...", Toast.LENGTH_LONG).show();
                editTextPin.setText("");
            }
        });
    }
}

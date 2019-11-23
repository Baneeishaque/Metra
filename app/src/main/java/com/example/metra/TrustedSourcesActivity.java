package com.example.metra;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrustedSourcesActivity extends AppCompatActivity {

    Context activityContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trusted_sources);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO : Alert Dialog Utils
                //TODO : Input Dialog Utils

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(activityContext);
                builder.setTitle("New Trusted Source");

                //TODO : Style the input

                // Set up the input
                final EditText input = new EditText(activityContext);
                // Specify the type of input expected
                //TODO : Pick from Contacts
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                input.setHint("Phone Number...");
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtils.convertPixelsToDp(96, activityContext));
//                int marginDp=DisplayUtils.convertPixelsToDp(128,activityContext);
//                lp.setMargins(marginDp,marginDp,marginDp,marginDp);
//                input.setLayoutParams(lp);
                builder.setView(input);

//                builder.setView(R.layout.add_trusted_soiurce);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO : Phone Number Validation
                        //TODO : Check for already existing number
                        TrustedSourcesDatabaseHelper trustedSourcesDatabaseHelper = new TrustedSourcesDatabaseHelper(activityContext);
                        trustedSourcesDatabaseHelper.insertTrustedSource(input.getText().toString());
                        LogUtils.debug(input.getText().toString() + " Added to Trusted DB...");
//                        TODO : To Activity Utils
                        startActivity(new Intent(activityContext, TrustedSourcesActivity.class));
                        ((AppCompatActivity) activityContext).finish();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        TrustedSourcesDatabaseHelper trustedSourcesDatabaseHelper = new TrustedSourcesDatabaseHelper(this);

        //instantiate custom adapter
        TrustedSourcesListAdapter adapter = new TrustedSourcesListAdapter(trustedSourcesDatabaseHelper.getAllTrustedSources(), this);

        //handle listView and assign adapter
        ListView lView = findViewById(R.id.list_view);
        lView.setAdapter(adapter);
    }

}

package com.buzz.vpn;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;

//import com.google.firebase.analytics.FirebaseAnalytics;


public class WelcomeActivity extends AppCompatActivity {

    TextView tv_welcome_status, tv_welcome_app;
    String StringGetAppURL, StringGetConnectionURL, AppDetails, FileDetails;
    SharedPreferences SharedAppDetails;
    TextView tv_welcome_title, tv_welcome_description, tv_welcome_size, tv_welcome_version;

    int Random;
//    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        StringGetAppURL = "https://raw.githubusercontent.com/gayanvoice/android-vpn-client-ics-openvpn/images/appdetails.json";
        StringGetConnectionURL = "https://raw.githubusercontent.com/gayanvoice/android-vpn-client-ics-openvpn/images/filedetails.json";
        //StringGetConnectionURL = "https://gayankuruppu.github.io/buzz/connection.html";

        Typeface RobotoMedium = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");
        Typeface RobotoRegular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface RobotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");

        tv_welcome_status = findViewById(R.id.tv_welcome_status);
        tv_welcome_app = findViewById(R.id.tv_welcome_app);
        tv_welcome_title = findViewById(R.id.tv_welcome_title);
        tv_welcome_description = findViewById(R.id.tv_welcome_description);
        tv_welcome_size = findViewById(R.id.tv_welcome_size);
        tv_welcome_version = findViewById(R.id.tv_welcome_version);

        tv_welcome_title.setTypeface(RobotoMedium);
        tv_welcome_description.setTypeface(RobotoRegular);
        tv_welcome_size.setTypeface(RobotoMedium);
        tv_welcome_version.setTypeface(RobotoMedium);


        startAnimation(WelcomeActivity.this, R.id.ll_welcome_loading, R.anim.slide_up_800, true);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation(WelcomeActivity.this, R.id.ll_welcome_details, R.anim.slide_up_800, true);
            }
        }, 1000);


        tv_welcome_status.setTypeface(RobotoMedium);
        tv_welcome_app.setTypeface(RobotoBold);

        Button btn_welcome_update = findViewById(R.id.btn_welcome_update);
        Button btn_welcome_later = findViewById(R.id.btn_welcome_later);

        btn_welcome_update.setTypeface(RobotoMedium);
        btn_welcome_later.setTypeface(RobotoMedium);

        btn_welcome_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gayanvoice/android-vpn-client-ics-openvpn")));
                    /*
                    The following lines of code load the PlayStore

                    Bundle params = new Bundle();
                    params.putString("device_id", App.device_id);
                    params.putString("click", "play");
                    mFirebaseAnalytics.logEvent("app_param_click", params);

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.buzz.vpn"));
                    startActivity(intent);
                    */
                } catch (ActivityNotFoundException activityNotFound) {
                    // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.buzz.vpn")));
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/gayanvoice/android-vpn-client-ics-openvpn")));

                } catch (Exception e) {
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("exception", "WA1" + e.toString());
//                    mFirebaseAnalytics.logEvent("app_param_error", params);
                }
            }

        });

        btn_welcome_later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);

            }

        });

        if (!Data.isConnectionDetails) {
            if (!Data.isAppDetails) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getAppDetails();
                    }
                }, 2000);
            }
        }

    }

    void getAppDetails() {

        tv_welcome_status.setText("GETTING APP DETAILS");

        AppDetails = loadJSONFromAsset("appdetails.json");
        Data.isAppDetails = true;
        getFileDetails();

//        RequestQueue queue = Volley.newRequestQueue(WelcomeActivity.this);
//        queue.getCache().clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, StringGetAppURL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String Response) {
//                        Log.e("Response", Response);
//                        AppDetails = Response;
//                        Data.isAppDetails = true;
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                Bundle params = new Bundle();
////                params.putString("device_id", App.device_id);
////                params.putString("exception", "WA2" + error.toString());
////                mFirebaseAnalytics.logEvent("app_param_error", params);
//
//                Data.isAppDetails = false;
//            }
//        });
//
//        queue.add(stringRequest);
//        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
//            @Override
//            public void onRequestFinished(Request<String> request) {
//                if (Data.isAppDetails) {
//                    getFileDetails();
//                } else {
//                    tv_welcome_status.setText("CONNECTION INTERRUPTED");
//                }
//            }
//        });
    }

    void getFileDetails() {

        tv_welcome_status.setText("GETTING CONNECTION DETAILS");

        FileDetails = loadJSONFromAsset("filedetails.json");
        Data.isConnectionDetails = true;

        final int min = 0;
        final int max = 4;
        Random = new Random().nextInt((max - min) + 1) + min;

        String Ads = "NULL", cuVersion = "NULL", upVersion = "NULL", upTitle = "NULL", upDescription = "NULL", upSize = "NULL";
        String ID = "NULL", FileID = "NULL", File = "NULL", City = "NULL", Country = "NULL", Image = "NULL",
                IP = "NULL", Active = "NULL", Signal = "NULL";
        String BlockedApps = "NULL";

        try {
            JSONObject jsonResponse = new JSONObject(AppDetails);
            Ads = jsonResponse.getString("ads");
        } catch (Exception e) {
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("exception", "WA4" + e.toString());
//                    mFirebaseAnalytics.logEvent("app_param_error", params);
        }

        try {
            JSONObject jsonResponse = new JSONObject(AppDetails);
            JSONArray jsonArray = jsonResponse.getJSONArray("update");
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            upVersion = jsonObject.getString("version");
            upTitle = jsonObject.getString("title");
            upDescription = jsonObject.getString("description");
            upSize = jsonObject.getString("size");
        } catch (Exception e) {
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("exception", "WA5" + e.toString());
//                    mFirebaseAnalytics.logEvent("app_param_error", params);
        }

        try {
            JSONObject json_response = new JSONObject(AppDetails);
            JSONArray jsonArray = json_response.getJSONArray("free");
            JSONObject json_object = jsonArray.getJSONObject(Random);
            ID = json_object.getString("id");
            FileID = json_object.getString("file");
            City = json_object.getString("city");
            Country = json_object.getString("country");
            Image = json_object.getString("image");
            IP = json_object.getString("ip");
            Active = json_object.getString("active");
            Signal = json_object.getString("signal");
        } catch (Exception e) {
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("exception", "WA5" + e.toString());
//                    mFirebaseAnalytics.logEvent("app_param_error", params);
        }

        try {
            JSONObject json_response = new JSONObject(FileDetails);
            JSONArray jsonArray = json_response.getJSONArray("ovpn_file");
            JSONObject json_object = jsonArray.getJSONObject(Integer.parseInt(FileID));
            FileID = json_object.getString("id");
//            File = json_object.getString("file");
            File = "client\n" +
                    "dev tun\n" +
                    "proto udp\n" +
                    "remote 51.15.140.208 1194\n" +
                    "remote free-05.protectednetgroup.com 1194\n" +
                    "resolv-retry infinite\n" +
                    "nobind\n" +
                    "ca [inline]\n" +
                    "tls-client\n" +
                    "tls-auth [inline] 1\n" +
                    "persist-tun\n" +
                    "persist-key\n" +
                    "cipher AES-128-CBC\n" +
                    "engine dynamic\n" +
                    "mute-replay-warnings\n" +
                    "auth-user-pass\n" +
                    "remote-cert-tls server\n" +
                    "comp-lzo adaptive\n" +
                    "reneg-sec 31557600\n" +
                    "explicit-exit-notify 2\n" +
                    "verb 3\n" +
                    "<ca>\n" +
                    "-----BEGIN CERTIFICATE-----\n" +
                    "MIIGSDCCBDCgAwIBAgIJAKtrUdxSHFdEMA0GCSqGSIb3DQEBCwUAMHUxCzAJBgNV\n" +
                    "BAYTAkExMQswCQYDVQQIEwJBMTELMAkGA1UEBxMCQTExCzAJBgNVBAoTAkExMQsw\n" +
                    "CQYDVQQLEwJBMTELMAkGA1UEAxMCQTExCzAJBgNVBCkTAkExMRgwFgYJKoZIhvcN\n" +
                    "AQkBFglhMUBhMS5jb20wHhcNMTgwNDExMjIyODM5WhcNMjgwNDA4MjIyODM5WjB1\n" +
                    "MQswCQYDVQQGEwJBMTELMAkGA1UECBMCQTExCzAJBgNVBAcTAkExMQswCQYDVQQK\n" +
                    "EwJBMTELMAkGA1UECxMCQTExCzAJBgNVBAMTAkExMQswCQYDVQQpEwJBMTEYMBYG\n" +
                    "CSqGSIb3DQEJARYJYTFAYTEuY29tMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIIC\n" +
                    "CgKCAgEAwAnw2O7Ye3q+X9WvHVVwvVxhPH8uutwCsADBSR33/Nj5EVl27x9thHqi\n" +
                    "6ItNawt5lbxTQf0SaebNGLldmVXki7dv2Jo0fbQjDKkx+4vwy9PckKzkRBVGVQvn\n" +
                    "+0AB9ild6+XmKigOTJkxSHMKZLAW5lDIEU+Qwtc33DibS0kuRSLaOnTb5Oa5YN7h\n" +
                    "JL2/GmXw4nt2JYk4sXpsb1CnsDXqOXP2Zkbao2tBGDSTLyCyoR8SaVYQhqM40Snn\n" +
                    "0SfkQKhKZJYWlKnAr77X+O4uZp4YkWinbWEh3Oylpni5xRQ9g2Gdr2HDDdv6OMC0\n" +
                    "ShZoJsShZeSH9LqsYtlDBgc/A0xRnXRwZKXnMEblSKDv1GkdD8HHtrgdkRNJsQry\n" +
                    "EKCp9IdrhpKE0bvk7aXg0LLKCGQnQnepePrJrbDQoeCFFi8hEtzH3y/P3t14gXEF\n" +
                    "sLh7jRR1GAZJf5Hzv1IcCrKM05+905+yysDMTMboRr4UKrDyRlECT5zyTUdxjkdH\n" +
                    "fmrg+8h8HjF2CbdDjAzwgH8v5WrFMz+FLs8yLaUeVKy6D2bHqCYtuo2rmFG9qFnD\n" +
                    "ONZSxgKT1kchBwevvrpngnP0ghhYL03OW9ixoaefYvKFGxzu/XOjRBsqd/3AsjGA\n" +
                    "aQF5Jt0o7Y0X3/G8zh0iQHMuCyF07ZHXJ1dF+vLPij761g7F4qUCAwEAAaOB2jCB\n" +
                    "1zAdBgNVHQ4EFgQUAvsORXYuy5Tr0fSe06GM8AIMa8AwgacGA1UdIwSBnzCBnIAU\n" +
                    "AvsORXYuy5Tr0fSe06GM8AIMa8CheaR3MHUxCzAJBgNVBAYTAkExMQswCQYDVQQI\n" +
                    "EwJBMTELMAkGA1UEBxMCQTExCzAJBgNVBAoTAkExMQswCQYDVQQLEwJBMTELMAkG\n" +
                    "A1UEAxMCQTExCzAJBgNVBCkTAkExMRgwFgYJKoZIhvcNAQkBFglhMUBhMS5jb22C\n" +
                    "CQCra1HcUhxXRDAMBgNVHRMEBTADAQH/MA0GCSqGSIb3DQEBCwUAA4ICAQCFr1lf\n" +
                    "zxydcVk8/G9ua2GgneWF/z5Y0hzg6Q75W4279pTq88N+fx+d445e62axVGJk40aI\n" +
                    "PpGLbp+xt24w6A46yMaIBnhGBYwlqZJFHfUBg7q7FzqKEMAMggz9oclbpDn6tJUa\n" +
                    "BCzhTX7KvgWmStyntCFwhxyMFuzo93N7fHaGgFmoDZ/rfJMyHCP7F9YiotlwKWfZ\n" +
                    "gLO/MvrlPJ57XYqvt1R7zQc6SicghvIp+iV8vjPCIaOu2b9QkfiUpl3cJYWaDuT/\n" +
                    "fG0hGa08bq/xtlTIs70J1jY0rteH1SNfxOLvsizZQaetf/P22meZ59LKiYj8cohe\n" +
                    "NprvRN84hGACxdyp+eykkG/QO4EJ3J+8UuQqU3itAzhXqnyy2AZjj3H5a1s76CFW\n" +
                    "OQqCRvo/xQfJyAKhotiHvECOdQcbAic41e0SH4T8Q655Nv+X/9j32bL+vfMSTmW+\n" +
                    "vyS43dckgCQ7GmI6nCy2j2OsuyOBd9QcXxXPwBqyuv0bdxl1r9nFRDRCua2Ofea2\n" +
                    "4mL9Ug7KBKByFoR4rpwuvVULqMp8ABTZIZBiK2nG0wgvQACOhfA3oFy8Q8c2Kzb3\n" +
                    "5a5TZ8FT0DuYDYAF8kOKTtsxxe4uibVPgODuu2qyKXdEt8ImwW44qB9UFvVWBr4j\n" +
                    "qrqv6TR1m4quYDcqTe2f/3scFFMz5GYB+h2PAQ==\n" +
                    "-----END CERTIFICATE-----\n" +
                    "</ca>\n" +
                    "<tls-auth>\n" +
                    "#\n" +
                    "# 2048 bit OpenVPN static key\n" +
                    "#\n" +
                    "-----BEGIN OpenVPN Static key V1-----\n" +
                    "61b70a41007fd4117c58391cd12cb3ab\n" +
                    "1134e44ef67e99c416dbf6fc2afa30ae\n" +
                    "531e34c7580987d93e4803980bd5b244\n" +
                    "c6da3704626e8875cb1121452424e190\n" +
                    "a47d681afbf77233199206637c08e3cd\n" +
                    "3f3d3c6d91a0dec674f865796e6fc16d\n" +
                    "5a7afc32aefe38335e5fb2e24591c8ec\n" +
                    "b4c4292019045e138647aac799a7a3ba\n" +
                    "6cc41ba68c9717e1a7385f6e82332e30\n" +
                    "285b2810b743b9cc72693ab1544a5cb4\n" +
                    "b0fe4fa5a029d0c8178f2f416450eac4\n" +
                    "a7a2b6fac1fd19272ac79a62992ef7a6\n" +
                    "8f634d3e0387dca931e89fa951d8deb0\n" +
                    "a0a71144055313fb88a403de2863e38e\n" +
                    "1776a134afb514aec8c0947ce588ab79\n" +
                    "36e05dba1767847882b2dfe425f2e853\n" +
                    "-----END OpenVPN Static key V1-----\n" +
                    "</tls-auth>";

        } catch (Exception e) {
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("exception", "WA6" + e.toString());
//                    mFirebaseAnalytics.logEvent("app_param_error", params);
        }


        // save details
        EncryptData En = new EncryptData();
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            cuVersion = pInfo.versionName;
            if (cuVersion.isEmpty()) {
                cuVersion = "0.0.0";
            }

            SharedAppDetails = getSharedPreferences("app_details", 0);
            SharedPreferences.Editor Editor = SharedAppDetails.edit();
            Editor.putString("ads", Ads);
            Editor.putString("up_title", upTitle);
            Editor.putString("up_description", upDescription);
            Editor.putString("up_size", upSize);
            Editor.putString("up_version", upVersion);
            Editor.putString("cu_version", cuVersion);
            Editor.apply();
        } catch (Exception e) {
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("exception", "WA7" + e.toString());
//                    mFirebaseAnalytics.logEvent("app_param_error", params);
        }

        try {
            SharedAppDetails = getSharedPreferences("connection_data", 0);
            SharedPreferences.Editor Editor = SharedAppDetails.edit();
            Editor.putString("id", ID);
            Editor.putString("file_id", FileID);
            Editor.putString("file", En.encrypt(File));
            Editor.putString("city", City);
            Editor.putString("country", Country);
            Editor.putString("image", Image);
            Editor.putString("ip", IP);
            Editor.putString("active", Active);
            Editor.putString("signal", Signal);
            Editor.apply();
        } catch (Exception e) {
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("exception", "WA8" + e.toString());
//                    mFirebaseAnalytics.logEvent("app_param_error", params);
        }

        try {
            SharedAppDetails = getSharedPreferences("app_values", 0);
            SharedPreferences.Editor Editor = SharedAppDetails.edit();
            Editor.putString("app_details", En.encrypt(AppDetails));
            Editor.putString("file_details", En.encrypt(FileDetails));
            Editor.apply();
        } catch (Exception e) {
//                    Bundle params = new Bundle();
//                    params.putString("device_id", App.device_id);
//                    params.putString("exception", "WA9" + e.toString());
//                    mFirebaseAnalytics.logEvent("app_param_error", params);
        }

        tv_welcome_title.setText(upTitle);
        tv_welcome_description.setText(upDescription);
        tv_welcome_size.setText(upSize);
        tv_welcome_version.setText(upVersion);

        if (Data.isConnectionDetails) {
            if (cuVersion.equals(upVersion)) {
                finish();
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            } else {
                startAnimation(WelcomeActivity.this, R.id.ll_welcome_loading, R.anim.fade_out_500, false);
                Handler handlerData = new Handler();
                handlerData.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startAnimation(WelcomeActivity.this, R.id.ll_update_details, R.anim.slide_up_800, true);
                    }
                }, 1000);
            }
        } else {
            tv_welcome_status.setText("CONNECTION INTERRUPTED");
        }

//        RequestQueue queue = Volley.newRequestQueue(WelcomeActivity.this);
//        queue.getCache().clear();
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, StringGetConnectionURL,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String Response) {
//                        FileDetails = Response;
//                        Data.isConnectionDetails = true;
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
////                Bundle params = new Bundle();
////                params.putString("device_id", App.device_id);
////                params.putString("exception", "WA3" + error.toString());
////                mFirebaseAnalytics.logEvent("app_param_error", params);
//
//                Data.isConnectionDetails = false;
//            }
//        });
//        queue.add(stringRequest);
//        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
//            @Override
//            public void onRequestFinished(Request<String> request) {
//
//                final int min = 0;
//                final int max = 4;
//                Random = new Random().nextInt((max - min) + 1) + min;
//
//                String Ads = "NULL", cuVersion = "NULL", upVersion = "NULL", upTitle = "NULL", upDescription = "NULL", upSize = "NULL";
//                String ID = "NULL", FileID = "NULL", File = "NULL", City = "NULL", Country = "NULL", Image = "NULL",
//                        IP = "NULL", Active = "NULL", Signal = "NULL";
//                String BlockedApps = "NULL";
//
//                try {
//                    JSONObject jsonResponse = new JSONObject(AppDetails);
//                    Ads = jsonResponse.getString("ads");
//                } catch (Exception e) {
////                    Bundle params = new Bundle();
////                    params.putString("device_id", App.device_id);
////                    params.putString("exception", "WA4" + e.toString());
////                    mFirebaseAnalytics.logEvent("app_param_error", params);
//                }
//
//                try {
//                    JSONObject jsonResponse = new JSONObject(AppDetails);
//                    JSONArray jsonArray = jsonResponse.getJSONArray("update");
//                    JSONObject jsonObject = jsonArray.getJSONObject(0);
//                    upVersion = jsonObject.getString("version");
//                    upTitle = jsonObject.getString("title");
//                    upDescription = jsonObject.getString("description");
//                    upSize = jsonObject.getString("size");
//                } catch (Exception e) {
////                    Bundle params = new Bundle();
////                    params.putString("device_id", App.device_id);
////                    params.putString("exception", "WA5" + e.toString());
////                    mFirebaseAnalytics.logEvent("app_param_error", params);
//                }
//
//                try {
//                    JSONObject json_response = new JSONObject(AppDetails);
//                    JSONArray jsonArray = json_response.getJSONArray("free");
//                    JSONObject json_object = jsonArray.getJSONObject(Random);
//                    ID = json_object.getString("id");
//                    FileID = json_object.getString("file");
//                    City = json_object.getString("city");
//                    Country = json_object.getString("country");
//                    Image = json_object.getString("image");
//                    IP = json_object.getString("ip");
//                    Active = json_object.getString("active");
//                    Signal = json_object.getString("signal");
//                } catch (Exception e) {
////                    Bundle params = new Bundle();
////                    params.putString("device_id", App.device_id);
////                    params.putString("exception", "WA5" + e.toString());
////                    mFirebaseAnalytics.logEvent("app_param_error", params);
//                }
//
//                try {
//                    JSONObject json_response = new JSONObject(FileDetails);
//                    JSONArray jsonArray = json_response.getJSONArray("ovpn_file");
//                    JSONObject json_object = jsonArray.getJSONObject(Integer.valueOf(FileID));
//                    FileID = json_object.getString("id");
//                    File = json_object.getString("file");
//                } catch (Exception e) {
////                    Bundle params = new Bundle();
////                    params.putString("device_id", App.device_id);
////                    params.putString("exception", "WA6" + e.toString());
////                    mFirebaseAnalytics.logEvent("app_param_error", params);
//                }
//
//
//                // save details
//                EncryptData En = new EncryptData();
//                try {
//                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
//                    cuVersion = pInfo.versionName;
//                    if (cuVersion.isEmpty()) {
//                        cuVersion = "0.0.0";
//                    }
//
//                    SharedAppDetails = getSharedPreferences("app_details", 0);
//                    SharedPreferences.Editor Editor = SharedAppDetails.edit();
//                    Editor.putString("ads", Ads);
//                    Editor.putString("up_title", upTitle);
//                    Editor.putString("up_description", upDescription);
//                    Editor.putString("up_size", upSize);
//                    Editor.putString("up_version", upVersion);
//                    Editor.putString("cu_version", cuVersion);
//                    Editor.apply();
//                } catch (Exception e) {
////                    Bundle params = new Bundle();
////                    params.putString("device_id", App.device_id);
////                    params.putString("exception", "WA7" + e.toString());
////                    mFirebaseAnalytics.logEvent("app_param_error", params);
//                }
//
//                try {
//                    SharedAppDetails = getSharedPreferences("connection_data", 0);
//                    SharedPreferences.Editor Editor = SharedAppDetails.edit();
//                    Editor.putString("id", ID);
//                    Editor.putString("file_id", FileID);
//                    Editor.putString("file", En.encrypt(File));
//                    Editor.putString("city", City);
//                    Editor.putString("country", Country);
//                    Editor.putString("image", Image);
//                    Editor.putString("ip", IP);
//                    Editor.putString("active", Active);
//                    Editor.putString("signal", Signal);
//                    Editor.apply();
//                } catch (Exception e) {
////                    Bundle params = new Bundle();
////                    params.putString("device_id", App.device_id);
////                    params.putString("exception", "WA8" + e.toString());
////                    mFirebaseAnalytics.logEvent("app_param_error", params);
//                }
//
//                try {
//                    SharedAppDetails = getSharedPreferences("app_values", 0);
//                    SharedPreferences.Editor Editor = SharedAppDetails.edit();
//                    Editor.putString("app_details", En.encrypt(AppDetails));
//                    Editor.putString("file_details", En.encrypt(FileDetails));
//                    Editor.apply();
//                } catch (Exception e) {
////                    Bundle params = new Bundle();
////                    params.putString("device_id", App.device_id);
////                    params.putString("exception", "WA9" + e.toString());
////                    mFirebaseAnalytics.logEvent("app_param_error", params);
//                }
//
//                tv_welcome_title.setText(upTitle);
//                tv_welcome_description.setText(upDescription);
//                tv_welcome_size.setText(upSize);
//                tv_welcome_version.setText(upVersion);
//
//                if (Data.isConnectionDetails) {
//                    if (cuVersion.equals(upVersion)) {
//                        finish();
//                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
//                    } else {
//                        startAnimation(WelcomeActivity.this, R.id.ll_welcome_loading, R.anim.fade_out_500, false);
//                        Handler handlerData = new Handler();
//                        handlerData.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                startAnimation(WelcomeActivity.this, R.id.ll_update_details, R.anim.slide_up_800, true);
//                            }
//                        }, 1000);
//                    }
//                } else {
//                    tv_welcome_status.setText("CONNECTION INTERRUPTED");
//                }
//            }
//        });
    }

    public void startAnimation(Context ctx, int view, int animation, boolean show) {
        final View Element = findViewById(view);
        if (show) {
            Element.setVisibility(View.VISIBLE);
        } else {
            Element.setVisibility(View.INVISIBLE);
        }
        Animation anim = AnimationUtils.loadAnimation(ctx, animation);
        Element.startAnimation(anim);
    }

    public String loadJSONFromAsset(String jsonFile) {

        String json = null;
        try {
            InputStream is = getAssets().open(jsonFile);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}

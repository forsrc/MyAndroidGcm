package com.forsrc.gcm.myandroidgcm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.forsrc.gcm.R;
import com.raxdenstudios.gcm.GCMHelper;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    TextView textView;
    public static MainActivity instance = null;


    public static final String BR_TEXT = "update_textview_action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        MainActivity.instance = this;

        textView = (TextView) findViewById(R.id.textView);
        textView.setTextIsSelectable(true);
        textView.append("\n------------------\n");

        final Context context = this;
        GCMHelper.getInstance().registerPlayServices(this,
                getString(R.string.project_number),
                new GCMHelper.OnGCMRegisterListener() {
                    @Override
                    public void onGooglePlayServicesNotSupported() {
                        Toast.makeText(getApplicationContext(), "GooglePlayServicesNotSupported",
                                Toast.LENGTH_SHORT).show();
                        textView.append("--> GooglePlayServicesNotSupported\n");
                        textView.append("------------------\n");
                    }

                    @Override
                    public void onDeviceRegistered(String registrationId) {
                        Toast.makeText(getApplicationContext(), registrationId,
                                Toast.LENGTH_SHORT).show();
                        textView.append("registrationId: " + registrationId + "\n");
                        textView.append("------------------\n");
                        GcmUtils.sendNotification(context, "registrationId: " + registrationId);
                        MainActivity.updateText(registrationId);
                    }

                    @Override
                    public void onDeviceNotRegistered(String message) {
                        Toast.makeText(getApplicationContext(), message,
                                Toast.LENGTH_SHORT).show();
                        textView.append("NotRegistered: " + message + "\n");
                        textView.append("------------------\n");
                        GcmUtils.sendNotification(context, "NotRegistered: " + message);
                    }
                });

        updateTextView();

        GcmUtils.broadcast(context, BR_TEXT, "message", "hello world, broadcast.");
    }

    BroadcastReceiver broadcastReceiver = null;
    private void updateTextView() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getExtras().getString("message");
                textView.append("message: " + message + "\n");
                textView.append("------------------\n");

            }
        };


        GcmUtils.registerReceiver(this, broadcastReceiver, BR_TEXT);
    }

    protected void onDestroy() {
        GcmUtils.unregisterReceiver(this, broadcastReceiver);
        super.onDestroy();
    };

    public Handler getHandler() {
        return handler;
    }

    public void updateTextView(String message) {
        textView.append("message: " + message + "\n");
        textView.append("------------------\n");
    }
    public static void updateTextView(final MainActivity mainActivity, final String message) {
        if (mainActivity == null) {
            return;
        }
        mainActivity.getHandler().post(new Runnable() {
            @Override
            public void run() {
                mainActivity.updateTextView(message);
            }
        });
    }

    public static void updateText(final String message) {
        if (MainActivity.instance == null) {
            return;
        }
        MainActivity.instance.getHandler().post(new Runnable() {
            @Override
            public void run() {
                MainActivity.instance.updateTextView(message);
            }
        });
    }
}

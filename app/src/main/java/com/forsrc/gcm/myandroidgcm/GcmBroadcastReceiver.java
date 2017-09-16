package com.forsrc.gcm.myandroidgcm;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.raxdenstudios.gcm.GCMHelper;
import com.raxdenstudios.gcm.model.GCMessage;



public class GcmBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = GcmBroadcastReceiver.class.getSimpleName();

    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        Log.d(TAG, "[onReceive] handling notification with extras: " + extras != null?extras.toString():"empty");

        if (extras == null) {
            return;
        }

        Object msg = extras.get("gcm.notification.body");
        if (msg != null) {
            Toast.makeText(context, "message: " + msg, Toast.LENGTH_SHORT).show();
            GcmUtils.sendNotification(context, msg.toString());

            GcmUtils.broadcast(context, MainActivity.BR_TEXT, "message", msg.toString());
        }

        if(extras.containsKey(GCMessage.class.getSimpleName())) {
            GCMessage message = (GCMessage)extras.getParcelable(GCMessage.class.getSimpleName());
            if(message != null && message.getMessage() != null && GCMHelper.getInstance().consumeGCMMessage(context, message.getId()) != null) {
                int defaults = -1;
                if(message.isSound()) {
                    defaults |= 1;
                }

                if(message.isVibrate()) {
                    defaults |= 2;
                }

                Log.d(TAG, "[onReceive] notification with id " + message.getId() + " sended!");
                // NotificationUtils.sendNotification(context, extras, message.getId(), com.raxdenstudios.gcm.R.drawable.ic_notification, message.getTitle(), message.getSubtitle(), message.getMessage(), message.getTicker(), defaults);
                Toast.makeText(context, "message: " + message.getMessage(), Toast.LENGTH_SHORT).show();
                GcmUtils.sendNotification(context, message.getMessage());

                GcmUtils.broadcast(context, MainActivity.BR_TEXT, "message", message.getMessage());
            }
        }

    }


}
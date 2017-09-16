package com.forsrc.gcm.myandroidgcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.raxdenstudios.commons.util.Utils;
import com.raxdenstudios.gcm.GCMHelper;
import com.raxdenstudios.gcm.model.GCMessage;




public class GcmIntentService  extends IntentService {
    private static final String TAG = GcmIntentService.class.getSimpleName();

    public GcmIntentService() {
        super(GcmIntentService.class.getSimpleName());
    }

    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "[onHandleIntent]");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        Log.d(TAG, "[onHandleIntent] messageType received: " + messageType);
        if(!extras.isEmpty()) {
            Log.d(TAG, "[onHandleIntent] extras received: " + extras.toString());
            if("send_error".equals(messageType)) {
                this.sendNotification("send_error", extras);
            } else if("deleted_messages".equals(messageType)) {
                this.sendNotification("deleted_messages", extras);
            } else if("gcm".equals(messageType)) {
                this.sendNotification("gcm", extras);
            }
        } else {
            Log.d(TAG, "[onHandleIntent] extras is empty!");
        }

        WakefulBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void sendNotification(String action, Bundle extras) {
        if("gcm".equals(action)) {
            GCMessage broadcastIntent = this.parseGCMessage(extras);
            if(broadcastIntent != null) {
                GCMHelper.getInstance().addGCMessage(this, broadcastIntent);
                extras.putParcelable(GCMessage.class.getSimpleName(), broadcastIntent);
            }
        }

        Log.d(TAG, "[sendNotification] sending notification to " + action + " with extras: " + extras.toString());
        Intent broadcastIntent1 = new Intent();
        broadcastIntent1.setAction(Utils.getPackageName(this.getApplicationContext()) + "." + action);
        broadcastIntent1.putExtras(extras);
        this.sendOrderedBroadcast(broadcastIntent1, (String)null);
    }

    protected GCMessage parseGCMessage(Bundle extras) {
        return GCMHelper.getInstance().buildGCMessage(this, extras);
    }
}
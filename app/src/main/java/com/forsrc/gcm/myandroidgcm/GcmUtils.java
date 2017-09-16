package com.forsrc.gcm.myandroidgcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.NotificationCompat;

import com.forsrc.gcm.R;

public class GcmUtils {

    public static void sendNotification(Context context, String message) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent =
                PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("SNS GCM Notification")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message);

        mBuilder.setContentIntent(contentIntent);
        manager.notify(0, mBuilder.build());
    }

    public static void registerReceiver(Context context, BroadcastReceiver broadcastReceiver, String name){
        IntentFilter filter = new IntentFilter(name);
        context.registerReceiver(broadcastReceiver, filter);
    }

    public static void broadcast(Context context, String name, String key, String value){
        Intent intent = new Intent(name);
        intent.putExtra(key, value);
        context.sendBroadcast(intent);
        /*
        boolean alarmRunning = (PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if(alarmRunning == false) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 1000, pendingIntent);
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 100, pendingIntent);
        }
        */
    }

    public static void unregisterReceiver(Context context, BroadcastReceiver broadcastReceiver){
        context.unregisterReceiver(broadcastReceiver);
    }
}
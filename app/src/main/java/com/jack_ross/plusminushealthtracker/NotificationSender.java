package com.jack_ross.plusminushealthtracker;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

/**
 * Class for sending out the actual notification to the user
 */
public class NotificationSender extends BroadcastReceiver {

    /**
     * Called when receiving an intent broadcast
     *
     * @param context
     * @param intent
     */
    public void onReceive(Context context, Intent intent) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Testing this");
        //Acquire the lock
        wl.acquire();


        NotificationCompat.Builder n = new NotificationCompat.Builder(context)
            .setContentTitle("Activity Saved")
            .setContentText("Your activity was successfully saved!")
            //.setSmallIcon(R.mipmap.ic_launcher)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
                /*
            .setContentIntent(pIntent)
            .setAutoCancel(true)
            .addAction(R.drawable.icon, "Call", pIntent)
            .addAction(R.drawable.icon, "More", pIntent)
            .addAction(R.drawable.icon, "And more", pIntent).build()*/
            ;

        n.build();


        NotificationManager notificationManager =
            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, n.build());
        wl.release();
    }

    public void setAlarm(Context context) {
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationSender.class);
        intent.putExtra("onetime", Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        // Every 3 hours
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * 60 * 3, pi);
    }
}

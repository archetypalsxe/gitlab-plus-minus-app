package com.jack_ross.plusminushealthtracker;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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
    }
}

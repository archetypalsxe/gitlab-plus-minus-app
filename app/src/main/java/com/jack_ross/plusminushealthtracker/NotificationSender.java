package com.jack_ross.plusminushealthtracker;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import java.util.Calendar;

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

        PendingIntent pendingIntent = PendingIntent.getActivity(
            context,
            0,
            new Intent(context, MainActivity.class),
            0
        );

        PendingIntent addActivityIntent = PendingIntent.getActivity(
            context, 0, new Intent(context, AddActivity.class), 0
        );

        long[] vibrate = {500,1000};
        NotificationCompat.Builder n = new NotificationCompat.Builder(context)
            .setContentTitle("Friendly Reminder")
            .setContentText("Don't forget to log your activities!")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .setVibrate(vibrate)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .addAction(android.R.drawable.ic_dialog_alert, "Add Activity", addActivityIntent)
            .setAutoCancel(true)
            ;

        NotificationManagerCompat notificationManager =
            NotificationManagerCompat.from(context);

        this.setAlarm(context);

        notificationManager.notify(1, n.build());

        wl.release();
    }

    /**
     * Cancel any alarms that we may have out there
     *
     * @param context
     */
    public void cancelAlarm(Context context) {
        Intent intent = new Intent(context, NotificationSender.class);
        PendingIntent sender = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    /**
     * Set up a repeating alarm
     *
     * @param context
     */
    public void setAlarm(Context context) {
        this.cancelAlarm(context);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationSender.class);
        intent.putExtra("onetime", Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        );
        // Every 3 hours
        long interval = 1000 * 60 * 60 * 3;

        Calendar cutOff = Calendar.getInstance();
        cutOff.set(Calendar.HOUR_OF_DAY, 18);
        cutOff.set(Calendar.MINUTE, 0);

        long startTime;

        if(Calendar.getInstance().before(cutOff)) {
            startTime = System.currentTimeMillis() + interval;
        } else {
            Calendar startDate = Calendar.getInstance();
            startDate.set(Calendar.HOUR_OF_DAY, 8);
            startDate.set(Calendar.MINUTE, 0);
            startDate.add(Calendar.DAY_OF_MONTH, 1);
            startTime = startDate.getTimeInMillis();
        }

        // 30 seconds for debugging
        //startTime = System.currentTimeMillis() + (1000 * 30);

        am.set(
            AlarmManager.RTC_WAKEUP,
            startTime,
            pi
        );

        /*
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime,
            interval,
            pi
        );
        */
    }
}

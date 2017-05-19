package com.jack_ross.plusminushealthtracker;

import android.app.AlarmManager;
import android.app.Notification;
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

    public static final int ACTIVITY = 1;
    public static final int WEIGHTS = 2;

    public static final int STATE_CODE_NORMAL = 1;
    public static final int STATE_CODE_WEIGHT_SAVED = 2;
    public static final int STATE_CODE_APP_OPENED = 3;

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

        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(context);
        int type = intent.getExtras().getInt("type");
        notificationManager.notify(type, this.getNotification(context, type));

        this.setAlarm(context, type, this.STATE_CODE_NORMAL);
        wl.release();
    }

    /**
     * Cancel any alarms that we may have out there
     *
     * @param context
     */
    public void cancelAlarm(Context context, int type, int stateCode) {
        Intent intent = new Intent(context, NotificationSender.class);
        PendingIntent sender = PendingIntent.getBroadcast(
            context,
            type,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        );
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    /**
     * Set up a repeating alarm
     *
     * @param context Context
     * @param type int
     * @param stateCode int
     */
    public void setAlarm(Context context, int type, int stateCode) {
        // If the app was opened, don't set an alarm if it's already set
        if(stateCode == this.STATE_CODE_APP_OPENED) {
            if(this.isAlarmSet(context, type)) {
                return;
            }
        }
        this.cancelAlarm(context, type, stateCode);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationSender.class);
        intent.putExtra("onetime", Boolean.FALSE);
        intent.putExtra("type", type);
        PendingIntent pi = PendingIntent.getBroadcast(
            context,
            type,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        );

        long startTime;
        switch (type) {
            case 2:
                startTime = this.getWeightAlarmTime(stateCode);
                break;
            case 1:
            default:
                startTime = this.getActivityAlarmTime();
        }

        am.set(
            AlarmManager.RTC_WAKEUP,
            startTime,
            pi
        );
    }

    /**
     * Get the time that we should set for an activity alarm
     *
     * @return long
     */
    protected long getActivityAlarmTime() {
        Calendar cutOff = Calendar.getInstance();
        cutOff.set(Calendar.HOUR_OF_DAY, 18);
        cutOff.set(Calendar.MINUTE, 0);

        long startTime;

        if(Calendar.getInstance().before(cutOff)) {
            // Every 3 hours
            startTime = System.currentTimeMillis() + (1000 * 60 * 60 * 3);
        } else {
            Calendar startDate = Calendar.getInstance();
            startDate.set(Calendar.HOUR_OF_DAY, 8);
            startDate.set(Calendar.MINUTE, 0);
            startDate.add(Calendar.DAY_OF_MONTH, 1);
            startTime = startDate.getTimeInMillis();
        }

        // For debugging
        startTime = System.currentTimeMillis() + (1000 * 80);

        return startTime;
    }

    /**
     * Get the time that we should set for a weight alarm
     *
     * @param stateCode int
     * @return long
     */
    protected long getWeightAlarmTime(int stateCode) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.HOUR_OF_DAY, 7);
        startDate.set(Calendar.MINUTE, 30);

        if(
            Calendar.getInstance().after(startDate) ||
            stateCode == this.STATE_CODE_WEIGHT_SAVED
        ) {
            startDate.add(Calendar.DAY_OF_MONTH, 1);
        }

        // For debugging
        return System.currentTimeMillis() + (1000 * 30);

        //return startDate.getTimeInMillis();
    }

    /**
     * Builds the notification that we should be displaying for a provided type
     *
     * @param type int
     * @return Notification
     */
    protected Notification getNotification(Context context, int type) {
        switch (type) {
            case 2:
                return this.getWeightNotification(context, type);
            case 1:
            default:
                return this.getActivityNotification(context, type);
        }
    }

    /**
     * Get a notification specific to adding activities
     *
     * @param context Context
     * @return Notification
     */
    protected Notification getActivityNotification(Context context, int type) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                type,
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
        return n.build();
    }

    /**
     * Get the notification for weights
     *
     * @param context Context
     * @return Notification
     */
    protected Notification getWeightNotification(Context context, int type) {
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                type,
                new Intent(context, MainActivity.class),
                0
        );

        PendingIntent addActivityIntent = PendingIntent.getActivity(
                context, 0, new Intent(context, AddWeight.class), 0
        );

        long[] vibrate = {1500,1000};
        NotificationCompat.Builder n = new NotificationCompat.Builder(context)
                .setContentTitle("Friendly Reminder")
                .setContentText("Don't forget to log your weight!")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(pendingIntent)
                .setVibrate(vibrate)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .addAction(android.R.drawable.ic_dialog_alert, "Add Weight", addActivityIntent)
                .setAutoCancel(true)
                ;
        return n.build();
    }

    /**
     * Checks to see if an alarm has already been set or not
     * @param type int
     * @return boolean
     */
    protected boolean isAlarmSet(Context context, int type) {
        Intent intent = new Intent(context, NotificationSender.class);
        return (PendingIntent.getBroadcast(context, type, intent, PendingIntent.FLAG_NO_CREATE)
                != null);
    }
}

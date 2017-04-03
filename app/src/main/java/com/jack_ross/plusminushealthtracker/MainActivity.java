package com.jack_ross.plusminushealthtracker;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    private NotificationSender alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = this.getApplicationContext();
        this.alarm = new NotificationSender();
        this.alarm.setRepeatingAlarm(context);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when we want to show the screen for adding a new activity to the database
     *
     * @param view View
     */
    public void showAddActivity(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    /**
     * Called when we want to show the screen for adding a weight to the database
     *
     * @param view View
     */
    public void showAddWeight(View view) {
        Intent intent = new Intent(this, AddWeight.class);
        startActivity(intent);
    }

    /**
     * Show the screen for displaying activities
     *
     * @param view View
     */
    public void showViewActivities(View view) {
        // @TODO Combine into centralized place

        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(
                "activities",
                new String[]{
                        "dateTime",
                        "userId",
                        "description",
                        "weight"
                },
                "userId = ? AND dateTime >= ?",
                new String[]{
                        "1",
                        this.getUnixTimeStamp(0, true)
                },
                null,
                null,
                "dateTime DESC"
        );

        String activities = "";
        int total = 0;
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        while (cursor.moveToNext()) {
            total += Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("weight")));
            String date = this.convertTimestamp(cursor.getString(cursor.getColumnIndexOrThrow("dateTime")));
            activities += date + ": ";
            activities += cursor.getString(cursor.getColumnIndexOrThrow("weight")) + " ";
            activities += cursor.getString(cursor.getColumnIndexOrThrow("description")) + "<br/>";
        }

        activities = "<b>Daily Total: "+ String.valueOf(total) +"</b><br/>"+ activities;
        activities += "<b>Yesterday's Total: "+ this.getDaysTotal(-1, database);
        activities += "<br/>Two Days Ago's Total: "+ this.getDaysTotal(-2, database);
        activities += "<br/>Three Days Ago's Total: "+ this.getDaysTotal(-3, database) +"</b>";
        intent.putExtra(EXTRA_MESSAGE, activities);

        startActivity(intent);
    }

    /**
     * Gets a unix timestamp of the start of the current day
     *
     * @TODO Centralize
     * @param dayDifference int
     * @param startOfDay boolean
     * @return String
     */
    protected String getUnixTimeStamp(int dayDifference, boolean startOfDay) {
        Calendar calendar = Calendar.getInstance();
        if(!startOfDay) {
            dayDifference++;
        }
        calendar.add(Calendar.DAY_OF_MONTH, dayDifference);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(!startOfDay) {
            calendar.add(Calendar.SECOND, -1);
        }
        /*
            Log.d("Debug", "Day Difference: "+ String.valueOf(dayDifference));
            Log.d("Debug", "Start Of Day"+ String.valueOf(startOfDay));
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
            Log.d("Debug", sdf.format(calendar.getTime()));
         */
        return String.valueOf(calendar.getTimeInMillis() / 1000);
    }

    /**
     * Converts a provided timestamp into a display for users
     *
     * @TODO Centralize
     * @param timeStamp String
     * @return String
     */
    protected String convertTimestamp(String timeStamp) {
        Date date = new Date(Integer.parseInt(timeStamp) * 1000L);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy 'at' h:mm a");
        return dateFormat.format(date);
    }

    /**
     * Returns the activity total from yesterday
     *
     * @TODO Centralize
     * @param daysDifference int
     * @param database Database
     * @return String
     */
    protected String getDaysTotal(int daysDifference, SQLiteDatabase database) {
        Cursor cursor = database.query(
                "activities",
                new String[]{
                        "SUM(weight) weight"
                },
                "userId = ? AND dateTime BETWEEN ? AND ?",
                new String[]{
                        "1",
                        this.getUnixTimeStamp(daysDifference, true),
                        this.getUnixTimeStamp(daysDifference, false)
                },
                null,
                null,
                "dateTime DESC"
        );
        if(cursor.moveToFirst() && cursor.getCount() > 0) {
            String weight = cursor.getString(cursor.getColumnIndexOrThrow("weight"));
            if(weight != null) {
                return weight;
            }
        }

        return "No activities";
    }
}
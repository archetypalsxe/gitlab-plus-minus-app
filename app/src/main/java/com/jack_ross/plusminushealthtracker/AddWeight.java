package com.jack_ross.plusminushealthtracker;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Class for controlling the page to add weights to the database
 */
public class AddWeight extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    /**
     * Save the weight that the user has entered
     *
     * @param view
     */
    public void saveWeight(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        Long currentTime = System.currentTimeMillis() / 1000;
        values.put(
                "dateTime",
                currentTime.toString()
        );
        values.put("userId", 1);
        values.put("weight", this.getWeight());

        this.clearFields();

        database.insert("userWeights", null, values);

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, this.getWeightsDisplay(getApplicationContext()));
        startActivity(intent);
    }

    /**
     * Build an intent object for displaying weights
     * @param context
     * @return String
     */
    public String getWeightsDisplay (Context context) {
        SQLiteDatabase database = this.getDatabase(context);
        String activities = "<b>Daily Average: " + this.getDaysAverage(0, database);
        activities += "<br>Yesterday's Average: " + this.getDaysAverage(-1, database);
        activities += "<br/>Two Days Ago's Average: " + this.getDaysAverage(-2, database);
        activities += "<br/>Three Days Ago's Average: " + this.getDaysAverage(-3, database) + "</b>";
        return activities;
    }

    /**
     * Get a readable database
     *
     * @param context
     * @return SQLiteDatabase
     */
    protected SQLiteDatabase getDatabase(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        return dbHelper.getReadableDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_add);
    }

    /**
     * Retrieves the weight that the user has entered
     *
     * @return double
     */
    protected double getWeight() {
        EditText weight = (EditText) findViewById(R.id.weight);
        return Double.parseDouble(weight.getText().toString());
    }

    /**
     * Clear the form fields so that if the user hits the back key, they are not pre-filled in
     */
    protected void clearFields() {
        EditText weight = (EditText) findViewById(R.id.weight);
        weight.setText("");
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
     * Returns the daily average
     *
     * @TODO Centralize
     * @param daysDifference int
     * @param database Database
     * @return String
     */
    protected String getDaysAverage(int daysDifference, SQLiteDatabase database) {
        Cursor cursor = database.query(
                "userWeights",
                new String[]{
                        "AVG(weight) weight"
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
            if(cursor.isNull(cursor.getColumnIndexOrThrow("weight"))) {
                return "No weight recorded";
            } else {
                return cursor.getString(cursor.getColumnIndexOrThrow("weight"));
            }
        }

        return "No weight recorded";
    }
}
package com.jack_ross.plusminushealthtracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
    }

    /**
     * Called to validate, and then save an activity in the database
     *
     * @param view View
     */
    public void saveActivity(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("dateTime", "2016-11-01 20:50:05");
        values.put("userId", 1);
        values.put("description", "Testing to see if this works");
        values.put("weight", -2);

        long rowId;
        rowId = database.insert("activities", null, values);
    }
}

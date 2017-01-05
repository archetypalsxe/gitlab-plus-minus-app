package com.jack_ross.plusminushealthtracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

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

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = "Row ID: " + rowId;
        intent.putExtra(EXTRA_MESSAGE, message);

        Cursor cursor = database.query(
                "activities",
                new String[]{
                    "dateTime",
                    "userId",
                    "description",
                    "weight"
                },
                "userId = ?",
                new String[]{"1"},
                null,
                null,
                "dateTime DESC"
        );

        String activities = "";
        while(cursor.moveToNext()) {
            activities += "Item: " + cursor.getString(cursor.getColumnIndexOrThrow("description")) + "\n";
        }

        intent.putExtra(EXTRA_MESSAGE, activities);

        startActivity(intent);
    }
}

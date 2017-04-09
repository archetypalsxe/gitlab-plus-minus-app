package com.jack_ross.plusminushealthtracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

/**
 * Class for controlling the page to add weights to the database
 */
public class AddWeight extends AppCompatActivity {

    /**
     * Save the weight that the user has entered
     *
     * @param view
     */
    public void saveWeight(View view) {
        DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        Long currentTime = System.currentTimeMillis() / 1000;
        values.put(
                "dateTime",
                currentTime.toString()
        );
        values.put("userId", 1);
        values.put("weight", this.getWeight());

        this.clearFields();

        long rowId = database.insert("userWeights", null, values);
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
}
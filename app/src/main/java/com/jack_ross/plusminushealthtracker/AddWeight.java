package com.jack_ross.plusminushealthtracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Class for controlling the page to add weights to the database
 */
public class AddWeight extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weight_add);
    }
}
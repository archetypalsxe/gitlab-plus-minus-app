package com.jack_ross.plusminushealthtracker;

import android.provider.BaseColumns;

import java.util.Iterator;

/**
 * Contract for the database table for storing users' weights
 */
public class UserWeights extends DatabaseContract.TableDefinitions implements BaseColumns {

    public static final String TABLE_NAME = "userWeights";

    /**
     * Set all of the columns in the instance variable columns
     */
    protected void declareColumns() {
        this.columns.add(new ColumnDefinition("dateTime", "TEXT"));
        this.columns.add(new ColumnDefinition("userId", "INTEGER"));
        this.columns.add(new ColumnDefinition("weight", "FLOAT"));
        for(Iterator<ColumnDefinition> i = this.columns.iterator(); i.hasNext(); ) {
            ColumnDefinition column = i.next();
        }
    }

    /**
     * Retrieves the name of this table
     *
     * @return String
     */
    protected String getTableName() {
        return TABLE_NAME;
    }

    protected String getId() {
        return _ID;
    }
}

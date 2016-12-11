package com.jack_ross.plusminushealthtracker;

import android.provider.BaseColumns;
import android.util.Log;

import java.util.Iterator;

/**
 * Contract for the database table Activities
 *
 * @TODO Clean up with garbage. Move into namespacing and whatnot
 */
public class Activities extends DatabaseContract.TableDefinitions implements BaseColumns {

    public static final String TABLE_NAME = "activities";

    /**
     * Set all of the columns in the instance variable columns
     */
    protected void declareColumns() {
        this.columns.add(new ColumnDefinition("dateTime", "TEXT"));
        this.columns.add(new ColumnDefinition("userId", "INTEGER"));
        this.columns.add(new ColumnDefinition("description", "TEXT"));
        this.columns.add(new ColumnDefinition("weight", "INTEGER"));
        // @TODO Remove logging
        for(Iterator<ColumnDefinition> i = this.columns.iterator(); i.hasNext(); ) {
            ColumnDefinition column = i.next();
            Log.w("Columns", column.name);
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
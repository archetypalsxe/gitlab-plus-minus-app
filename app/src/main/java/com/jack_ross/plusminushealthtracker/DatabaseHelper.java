package com.jack_ross.plusminushealthtracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Class for managing the database
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Constructor
     *
     * @param context
     */
    public DatabaseHelper(Context context) {
       super(context, DatabaseContract.DATABASE_NAME, null, DatabaseContract.DATABASE_VERSION);
    }

    /**
     * Creates a new database
     *
     * @param database
     */
    public void onCreate(SQLiteDatabase database) {
        /**
         * @TODO This could be done better
         */
        DatabaseContract.TableDefinitions activityTable = new Activities();
        String createSql = activityTable.getCreateTable();
        database.execSQL(createSql);
    }

    /**
     * Upgrading the database to a new version
     *
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }

    /**
     * Downgrading the database to a previous version
     *
     * @param database
     * @param oldVersion
     * @param newVersion
     */
    public void onDowngrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }

}

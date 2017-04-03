package com.jack_ross.plusminushealthtracker;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Class for defining the database that will be used
 */
public final class DatabaseContract {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "PlusMinusFitness.db";
    public final ArrayList<TableDefinitions> tableDefinitions = new ArrayList<TableDefinitions>();

    /**
     * Class for getting all the contracts of the tables/database
     */
    public DatabaseContract() {
    }

    public static abstract class TableDefinitions {

        /**
         * A list of all of the column definitions for this table
         */
        public ArrayList<ColumnDefinition> columns = new ArrayList<ColumnDefinition>();

        /* @TODO Need a function for getting all of the column names */

        /**
         * Classes must implement a declaration of the columns that are used. Should set the
         * columns instance variable
         */
        protected abstract void declareColumns();

        /**
         * Should return the table name of the table being created
         *
         * @return String
         */
        protected abstract String getTableName();

        protected abstract String getId();

        /**
         * Constructor. Sets up the object for use
         */
        public TableDefinitions() {
            this.declareColumns();
        }

        /**
         * Generate the SQL in order to create this table
         *
         * @return String
         */
        public final String getCreateTable() {
            String sql = "CREATE TABLE " + this.getTableName() + " (";
            sql += this.getId() + " INTEGER PRIMARY KEY, ";

            Iterator<ColumnDefinition> iterator = this.columns.iterator();
            if (iterator.hasNext()) {
                while(iterator.hasNext()) {
                    ColumnDefinition column = iterator.next();
                    sql += column.name +" "+ column.dataType;
                    // If we are on the last element, don't add a trailing comma
                    if(iterator.hasNext()) {
                        sql += ", ";
                    } else {
                        sql += ") ";
                    }
                }
            }

            return sql;
        }

        /**
         * Retrieves the SQL for deleting the table
         *
         * @return String
         */
        public final String getDeleteTable() {
            return "DROP TABLE IF EXISTS " + this.getTableName();
        }
    }

}
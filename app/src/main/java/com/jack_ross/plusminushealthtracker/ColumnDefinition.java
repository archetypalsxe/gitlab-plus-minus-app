package com.jack_ross.plusminushealthtracker;

/**
 * Used for defining columns
 *
 * @TODO Make this better. Namespacing and whatnot
 */
public class ColumnDefinition {

    /**
     * @var String Name of the column
     */
    public String name;

    /**
     * @var String The data type of the column
     */
    public String dataType;

    /**
     * Default constructor. Allows passing in variables to set the instance variables
     *
     * @param name String
     * @param dataType String
     */
    public ColumnDefinition(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }
}
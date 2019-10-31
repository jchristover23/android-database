package com.android.jeremy.databaseproject.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Food app.
 */
public final class MenuContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private MenuContract() {}

    /**
     * Inner class that defines constant values for the menu database table.
     * Each entry in the table represents a single food.
     */
    public static final class MenuEntry implements BaseColumns {

        /** Name of database table for menu */
        public final static String TABLE_NAME = "menu";

        /**
         * Unique ID number for the food (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the customer.
         *
         * Type: TEXT
         */
        public final static String COLUMN_CUST_NAME ="name";

        /**
         * Customer name.
         *
         * Type: TEXT
         */
        public final static String COLUMN_ADDITIONAL_FOOD = "addon";

        /**
         * Chili preference.
         *
         * The only possible values are {@link #CHILI_NOPREF}, {@link #CHILI_YES},
         * or {@link #CHILI_NO}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_FOOD_CHILIPREF = "chili_pref";

        /**
         * quantity of the food.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_FOOD_QTY = "quantity";

        /**
         * Possible values for chili condition
         */
        public static final int CHILI_NOPREF = 0;
        public static final int CHILI_YES = 1;
        public static final int CHILI_NO = 2;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_FOOD);
    }

    public static final String CONTENT_AUTHORITY = "com.android.jeremy.databaseproject";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_FOOD = "food";

}
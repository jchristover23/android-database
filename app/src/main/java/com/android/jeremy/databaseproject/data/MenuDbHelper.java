package com.android.jeremy.databaseproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database helper for Database app. Manages database creation and version management.
 */
public class MenuDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MenuDbHelper.class.getSimpleName();

    /** Name of the database file */
    private static final String DATABASE_NAME = "food.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link MenuDbHelper}.
     *
     * @param context of the app
     */
    public MenuDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_MENU_TABLE =  "CREATE TABLE " + MenuContract.MenuEntry.TABLE_NAME + " ("
                + MenuContract.MenuEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MenuContract.MenuEntry.COLUMN_CUST_NAME + " TEXT NOT NULL, "
                + MenuContract.MenuEntry.COLUMN_ADDITIONAL_FOOD + " TEXT, "
                + MenuContract.MenuEntry.COLUMN_FOOD_CHILIPREF + " INTEGER NOT NULL, "
                + MenuContract.MenuEntry.COLUMN_FOOD_QTY + " INTEGER NOT NULL DEFAULT 1);";

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_MENU_TABLE);
    }

    /**
     * This is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
    }
}




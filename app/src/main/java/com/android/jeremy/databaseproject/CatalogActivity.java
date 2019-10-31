package com.android.jeremy.databaseproject;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.jeremy.databaseproject.data.MenuContract;
import com.android.jeremy.databaseproject.data.MenuDbHelper;


/**
 * Displays list of food that were ordered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {



    /** Database helper that will provide us access to the database */
    private MenuDbHelper mDbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new MenuDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the food ordered database.
     */
    private void displayDatabaseInfo() {
        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Define a projection that specifies which columns from the database
        String[] projection = {
                MenuContract.MenuEntry._ID,
                MenuContract.MenuEntry.COLUMN_CUST_NAME,
                MenuContract.MenuEntry.COLUMN_ADDITIONAL_FOOD,
                MenuContract.MenuEntry.COLUMN_FOOD_CHILIPREF,
                MenuContract.MenuEntry.COLUMN_FOOD_QTY };

        // Perform a query on the food table
        Cursor cursor = db.query(
                MenuContract.MenuEntry.TABLE_NAME,   // The table to query
                projection,            // The columns to return
                null,                  // The columns for the WHERE clause
                null,                  // The values for the WHERE clause
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // The sort order

        TextView displayView = (TextView) findViewById(R.id.text_view_pet);

        try {
            //
            // The food table contains <number of rows in Cursor>.
            // _id | name | add on food | chili preference | quantity
            //
            // In the while loop below, iterate through the rows of the cursor and display
            // the information from each column in this order.
            displayView.setText("You have " + cursor.getCount() + " order(s) history.\n\n");
            displayView.append(MenuContract.MenuEntry._ID + " | " +
                    MenuContract.MenuEntry.COLUMN_CUST_NAME + " | " +
                    MenuContract.MenuEntry.COLUMN_ADDITIONAL_FOOD + " | " +
                    MenuContract.MenuEntry.COLUMN_FOOD_CHILIPREF + " | " +
                    MenuContract.MenuEntry.COLUMN_FOOD_QTY + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(MenuContract.MenuEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_CUST_NAME);
            int addonColumnIndex = cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_ADDITIONAL_FOOD);
            int chiliColumnIndex = cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_FOOD_CHILIPREF);
            int qtyColumnIndex = cursor.getColumnIndex(MenuContract.MenuEntry.COLUMN_FOOD_QTY);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentAddon = cursor.getString(addonColumnIndex);
                int currentChili = cursor.getInt(chiliColumnIndex);
                int currentQty = cursor.getInt(qtyColumnIndex);
                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " | " +
                        currentName + " | " +
                        currentAddon + " | " +
                        currentChili + " | " +
                        currentQty+"\n"+"------------------------------------------------------------"));
            }
        } finally {
            cursor.close();
        }
    }

    /**
     * Helper method to insert hardcoded pet data into the database. For debugging purposes only.
     */
    private void insertOrder() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys
        ContentValues values = new ContentValues();
        values.put(MenuContract.MenuEntry.COLUMN_CUST_NAME, "ABC");
        values.put(MenuContract.MenuEntry.COLUMN_ADDITIONAL_FOOD, "Potato");
        values.put(MenuContract.MenuEntry.COLUMN_FOOD_CHILIPREF, MenuContract.MenuEntry.CHILI_YES);
        values.put(MenuContract.MenuEntry.COLUMN_FOOD_QTY, 4);


        long newRowId = db.insert(MenuContract.MenuEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // Adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertOrder();
                displayDatabaseInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                return true;
        }
        return super.onOptionsItemSelected(item);


    }
}
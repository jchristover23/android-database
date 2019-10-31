package com.android.jeremy.databaseproject;


import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.jeremy.databaseproject.data.MenuContract;
import com.android.jeremy.databaseproject.data.MenuDbHelper;

/**
 * Allows user to create a new order query.
 */
public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter customer name */
    private EditText mNameEditText;

    /** EditText field to add on */
    private EditText mAddonEditText;

    /** EditText field to enter the QTY */
    private EditText mQtyEditText;

    /** EditText field to enter the chili preference */
    private Spinner mChiliPrefSpinner;

    /**
     * Chili Preference of the food. The possible valid values are in the MenuContract.java file:
     * {@link com.android.jeremy.databaseproject.data.MenuContract.MenuEntry#CHILI_NOPREF}, {@link com.android.jeremy.databaseproject.data.MenuContract.MenuEntry#CHILI_NO}, or
     * {@link com.android.jeremy.databaseproject.data.MenuContract.MenuEntry#CHILI_NO}.
     */
    private int mChili = MenuContract.MenuEntry.CHILI_NOPREF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_customer_name);
        mAddonEditText = (EditText) findViewById(R.id.edit_addon);
        mQtyEditText = (EditText) findViewById(R.id.edit_qty);
        mChiliPrefSpinner = (Spinner) findViewById(R.id.chili_preference);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the chili preference of the food.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter SpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_chili_preference, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mChiliPrefSpinner.setAdapter(SpinnerAdapter);

        // Set the integer mSelected to the constant values
        mChiliPrefSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.chili_yes))){
                        mChili = MenuContract.MenuEntry.CHILI_YES;
                    } else if (selection.equals(getString(R.string.chili_no))) {
                        mChili = MenuContract.MenuEntry.CHILI_NO;
                    } else {
                        mChili = MenuContract.MenuEntry.CHILI_NOPREF;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mChili = MenuContract.MenuEntry.CHILI_NOPREF;
            }
        });
    }

    /**
     * Get user input from editor and save new order query into database.
     */
    private void insertOrder() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String addonString = mAddonEditText.getText().toString().trim();
        String qtyString = mQtyEditText.getText().toString().trim();
        int qty = Integer.parseInt(qtyString);

        // Create database helper
        MenuDbHelper mDbHelper = new MenuDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and order attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(MenuContract.MenuEntry.COLUMN_CUST_NAME, nameString);
        values.put(MenuContract.MenuEntry.COLUMN_ADDITIONAL_FOOD, addonString);
        values.put(MenuContract.MenuEntry.COLUMN_FOOD_CHILIPREF, mChili);
        values.put(MenuContract.MenuEntry.COLUMN_FOOD_QTY, qty);

        // Insert a new row for food in the database, returning the ID of that new row.
        long newRowId = db.insert(MenuContract.MenuEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving order", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Order placed with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save order to database
                insertOrder();
                Intent a = new Intent(EditorActivity.this, CatalogActivity.class);
                startActivity(a);
                // Exit activity
                //finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
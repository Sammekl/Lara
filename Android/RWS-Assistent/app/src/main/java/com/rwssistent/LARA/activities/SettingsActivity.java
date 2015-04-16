package com.rwssistent.LARA.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
//import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;

import com.rwssistent.LARA.R;
import com.rwssistent.LARA.helpers.PreferenceHelper;
import com.rwssistent.LARA.utils.Constants;

/**
 * Created by Samme on 19-3-2015.
 */

public class SettingsActivity extends ActionBarActivity {

    private EditText longitudeInput;
    private EditText latitudeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        longitudeInput = (EditText) findViewById(R.id.longitude_input);
        latitudeInput = (EditText) findViewById(R.id.latitude_input);
        this.getCoordinates();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.overridePendingTransition(R.anim.left_slide_in, R.anim.left_slide_out);
    }

    // ============================================
    // Public Methods
    // ============================================

    /**
     * Invoked when save button is clicked.
     *
     * @param view the save button
     */
    public void doSave(View view) {
        this.saveCoordinates();
        onBackPressed();
    }

    // ============================================
    // Private Methods
    // ============================================

    /**
     * Get the saved coordinates from the preference files
     */
    private void getCoordinates() {
        String longitude = PreferenceHelper.readPreference(this, Constants.PREF_LONGITUDE_NAME, null, Constants.PREF_FILE_NAME);
        if (longitude != null && !longitude.isEmpty()) {
            longitudeInput.setText(longitude);
        }
        String latitude = PreferenceHelper.readPreference(this, Constants.PREF_LATITUDE_NAME, null, Constants.PREF_FILE_NAME);
        if (latitude != null && !latitude.isEmpty()) {
            latitudeInput.setText(latitude);
        }
    }

    /**
     * Save the preferences in the SharedPreferences
     */
    private void saveCoordinates() {
        String longitude = "";
        String latitude = "";

        if (longitudeInput != null && longitudeInput.getText() != null && !longitudeInput.getText().toString().equals("")) {
            longitude = longitudeInput.getText().toString();
        }

        if (latitudeInput != null && latitudeInput.getText() != null && !latitudeInput.getText().toString().equals("")) {
            latitude = latitudeInput.getText().toString();
        }
        PreferenceHelper.savePreference(this, Constants.PREF_LONGITUDE_NAME, longitude, Constants.PREF_FILE_NAME);
        PreferenceHelper.savePreference(this, Constants.PREF_LATITUDE_NAME, latitude, Constants.PREF_FILE_NAME);
    }
}

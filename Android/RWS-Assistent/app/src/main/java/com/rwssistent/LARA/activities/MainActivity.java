package com.rwssistent.LARA.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.rwssistent.LARA.R;
import com.rwssistent.LARA.helpers.PreferenceHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.utils.Constants;
import com.rwssistent.LARA.utils.LaraService;

import org.w3c.dom.Text;


public class MainActivity extends BaseActivity {

    private TextView maxSpeed;
    private TextView numOfLanes;
    private TextView roadName;
    private TextView speedUnit;

    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.getTextViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationService();
        this.getLocationFromPreferences();
        LaraService.getRoadData(getActivity(), longitude, latitude);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // ============================================
    // Public Methods
    // ============================================

    public void displayValues(Highway highway) {
        if(highway.getMaxSpeed() > 0) {
            maxSpeed.setText(String.valueOf(highway.getMaxSpeed()));
            maxSpeed.setTextSize(80);
            speedUnit.setVisibility(View.VISIBLE);
        } else {
            maxSpeed.setText(R.string.no_maxspeed_found);
        }
        if (highway.getLanes() > 0) {
            numOfLanes.setText(String.valueOf(highway.getLanes()) + " " + this.getResources().getString(R.string.lanes));
            numOfLanes.setVisibility(View.VISIBLE);
        }
        roadName.setText(String.valueOf(highway.getRoadName()));
    }
    public void startLocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
//                LaraService.getRoadData(getActivity(), latitude, longitude);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Latitude","enable");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Latitude","disable");
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, locationListener);
    }

    // ============================================
    // Private Methods
    // ============================================

    private MainActivity getActivity() {
        return this;
    }

    private void getTextViews() {
        maxSpeed = (TextView) findViewById(R.id.maxspeed);
        numOfLanes = (TextView) findViewById(R.id.lanes);
        roadName = (TextView) findViewById(R.id.roadName);
        speedUnit = (TextView) findViewById(R.id.speedUnit);
    }

    private void getLocationFromPreferences() {
        String longitudePref = PreferenceHelper.readPreference(this, Constants.PREF_LONGITUDE_NAME, null, Constants.PREF_FILE_NAME);
        if(longitudePref != null && !longitudePref.isEmpty()) {
            longitude = Double.parseDouble(longitudePref);
        }
        String latitudePref = PreferenceHelper.readPreference(this, Constants.PREF_LATITUDE_NAME, null, Constants.PREF_FILE_NAME);
        if(latitudePref != null && !latitudePref.isEmpty()) {
            latitude = Double.parseDouble(latitudePref);
        }
    }
}

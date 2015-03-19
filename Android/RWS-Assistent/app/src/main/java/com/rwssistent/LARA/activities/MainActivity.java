package com.rwssistent.LARA.activities;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

import com.rwssistent.LARA.R;
import com.rwssistent.LARA.utils.LaraService;


public class MainActivity extends BaseActivity {

    private TextView currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentLocation = (TextView) findViewById(R.id.current_location);
        startLocationService();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // ============================================
    // Public Methods
    // ============================================
    public void startLocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                currentLocation.setText("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
                LaraService.getRoadData(getActivity(), location.getLongitude(), location.getLatitude());
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


}
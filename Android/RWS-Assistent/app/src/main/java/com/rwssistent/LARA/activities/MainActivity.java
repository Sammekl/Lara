package com.rwssistent.LARA.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rwssistent.LARA.R;
import com.rwssistent.LARA.helpers.PreferenceHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;
import com.rwssistent.LARA.utils.Constants;
import com.rwssistent.LARA.utils.LaraService;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    private TextView maxSpeed;
    private TextView numOfLanes;
    private TextView roadName;
    private TextView speedUnit;
    private TextView currentLocation;

    private double longitude;
    private double latitude;
    private LaraService laraService;

    private List<Node> allNodes;
    private List<Highway> allHighways;

    private Location pollLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        this.getTextViews();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_lara_action);

        ImageView img= (ImageView) findViewById(R.id.imageViewSpeed);
        img.setImageResource(R.drawable.verkeersbord);

        laraService = new LaraService();

        this.getLocationFromPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            this.startActivity(new Intent(this, SettingsActivity.class));
            this.overridePendingTransition(R.anim.right_slide_in, R.anim.right_slide_out);
        }
        return super.onOptionsItemSelected(item);
    }
    // ============================================
    // Public Methods
    // ============================================

    public void displayValues(Highway highway) {
        if (highway != null) {
            if (highway.getMaxSpeed() > 0) {
                maxSpeed.setText(String.valueOf(highway.getMaxSpeed()));
                speedUnit.setVisibility(View.VISIBLE);
            } else {
                maxSpeed.setText(R.string.unknown_maxspeed);
            }
//            if (highway.getLanes() > 0) {
//                numOfLanes.setText(String.valueOf(highway.getLanes()) + " " + this.getResources().getString(R.string.lanes));
//                numOfLanes.setVisibility(View.VISIBLE);
//            }
            if (highway.getRoadName() != null && !highway.getRoadName().equals("")) {
                roadName.setText(String.valueOf(highway.getRoadName()));
            }
        } else {
            // TODO Error laten zien (geen weg gevonden). Geef ook een optie om naar locatie/instellingen te gaan (van de telefoon)
        }
    }

    public void startLocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            boolean firstRun = true;

            @Override
            public void onLocationChanged(Location location) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
                currentLocation.setText("Lat: " + latitude + " | Long: " + longitude);

                if (firstRun) {
                    setPollLocation(location);
                    laraService.getHighwayData(getActivity(), latitude, longitude);
                    firstRun = false;
                }
                pollNearestHighway(longitude, latitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Latitude", "enable");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Latitude", "disable");
            }
        };
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);
    }

    /**
     * Set from RoadTask
     *
     * @param nodes all the nodes found within the radius
     */
    public void setAllNodes(List<Node> nodes) {
        if (allNodes != null) {
            this.allNodes.clear();
        }
        this.allNodes = nodes;
    }

    /**
     * Set from RoadTask
     *
     * @param highways all the highways found within the radius
     */
    public void setAllHighways(List<Highway> highways) {
        if (allHighways != null) {
            this.allHighways.clear();
        }
        this.allHighways = highways;
    }

    // ============================================
    // Private Methods
    // ============================================

    /**
     * Poll the nearest highway with the current location
     * @param latitude  current latitude
     * @param longitude current longitude
     */
    private void pollNearestHighway(double longitude, double latitude) {
        if (allNodes != null) {
            Node node = laraService.pollNearestNode(longitude, latitude, allNodes);
            if (node != null) {
                // If current location is 800+ meters away from the original pollLocation :
                if (laraService.distanceFromPollLocation(pollLocation.getLatitude(), pollLocation.getLongitude(),
                        latitude, longitude) > 0.8) {
                    Log.e(getClass().getSimpleName(), "pollNearestHighway > verder dan 800m!");
                    pollLocation.setLatitude(latitude);
                    pollLocation.setLongitude(longitude);
                    laraService.getHighwayData(this, latitude, longitude);
                }
                if (allHighways != null) {
                    Highway highway = laraService.pollNearestHighway(this, node, allHighways);
                    if (highway != null) {
                        displayValues(highway);
                    }
                } else {
                    Log.e(getClass().getSimpleName(), "pollNearestHighway > Geen highways gevonden");
                }
            }
        } else {
            Log.e(getClass().getSimpleName(), "pollNearestHighway > Geen nodes gevonden");
        }
    }

    private void getTextViews() {
        maxSpeed = (TextView) findViewById(R.id.maxspeed);
//        numOfLanes = (TextView) findViewById(R.id.lanes);
        roadName = (TextView) findViewById(R.id.roadName);
        speedUnit = (TextView) findViewById(R.id.speedUnit);
        currentLocation = (TextView) findViewById(R.id.current_location);
    }

    /**
     * Test method used for custom location
     */
    private void getLocationFromPreferences() {
        String latitudePref = PreferenceHelper.readPreference(this, Constants.PREF_LATITUDE_NAME, null, Constants.PREF_FILE_NAME);
        if (latitudePref != null && !latitudePref.isEmpty()) {
            latitude = Double.parseDouble(latitudePref);
        }
        String longitudePref = PreferenceHelper.readPreference(this, Constants.PREF_LONGITUDE_NAME, null, Constants.PREF_FILE_NAME);
        if (longitudePref != null && !longitudePref.isEmpty()) {
            longitude = Double.parseDouble(longitudePref);
        }
    }

    private MainActivity getActivity() {
        return this;
    }

    public void setPollLocation(Location pollLocation) {
        this.pollLocation = pollLocation;
    }
}

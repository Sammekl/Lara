package com.rwssistent.LARA.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rwssistent.LARA.R;
import com.rwssistent.LARA.exceptions.LaraException;
import com.rwssistent.LARA.helpers.HighwayHelper;
import com.rwssistent.LARA.helpers.PreferenceHelper;
import com.rwssistent.LARA.helpers.TestHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;
import com.rwssistent.LARA.utils.Constants;
import com.rwssistent.LARA.utils.LaraService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private TextView maxSpeed;
    private TextView roadName;
    private TextView speedUnit;

    private double longitude;
    private double latitude;
    private LaraService laraService;

    private List<Node> allNodes;
    private List<Highway> allHighways;
    private List<Node> allNodesFromAllHighwaysFromCurrentNode;
    private List<Highway> currentHighways;
    private List<Highway> previousHighways;

    private Node currentNode;
    private Node previousNode;
    private Highway previousHighway;

    private Location pollLocation;
    LocationManager locationManager;
    LocationListener locationListener;

    private ProgressDialog progressDialog;

    private int testIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        this.getTextViews();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_lara_action);

        ImageView img = (ImageView) findViewById(R.id.imageViewSpeed);
        img.setImageResource(R.drawable.verkeersbord);

        laraService = new LaraService();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //this.getLocationFromPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationService();
        Log.i(getClass().getSimpleName(), "Activity resumed. LocationManager started polling.");
    }

    @Override
    protected void onPause() {
        Log.i(getClass().getSimpleName(), "Activity paused. LocationManager stopped polling.");
        locationManager.removeUpdates(locationListener);
        super.onPause();
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

    public void doInfo(View view) {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Info");
        String msg = "";
        msg += "Previous highways:\n";
        if (previousHighways != null) {
            for (Highway highway : previousHighways) {
                msg += highway.getRoadName() + "\n";
            }
        }
        msg += "\nCurrent highways:\n";
        if (currentHighways != null) {
            for (Highway highway : currentHighways) {
                msg += highway.getRoadName() + "\n";
            }
        }
        adb.setMessage(msg);
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        adb.show();
    }

    public void displayValues(Highway highway) {
        HighwayHelper highwayHelper = new HighwayHelper();

        if (highway != null) {
            if (highway.getMaxSpeed() > 0) {
                String maxspeed = "";
                if (highwayHelper.ConditionalValid(highway)) {
                    maxspeed = String.valueOf(highway.getMaxSpeedConditional());
                } else {
                    maxspeed = String.valueOf(highway.getMaxSpeed());
                }
                maxSpeed.setText(maxspeed);
                speedUnit.setVisibility(View.VISIBLE);
            } else {
                maxSpeed.setText(R.string.unknown_maxspeed);
                speedUnit.setVisibility(View.INVISIBLE);
            }
            if (highway.getRoadName() != null && !highway.getRoadName().equals("")) {
                roadName.setVisibility(View.VISIBLE);
                roadName.setText(String.valueOf(highway.getRoadName()));
                if (highway.getRoadName().length() > 21) {
                    roadName.setTextSize(20);
                } else {
                    roadName.setTextSize(30);
                }
            }
            // TODO RoadRef
        } else {
            // TODO Error laten zien (geen weg gevonden). Geef ook een optie om naar locatie/instellingen te gaan (van de telefoon)
        }
    }

    public void startLocationService() {
        locationListener = new LocationListener() {

            boolean firstRun = true;

            @Override
            public void onLocationChanged(Location location) {
                List<Node> testNodes = null;
                // Uncomment de volgende regel om te testen met de TestHelper.
//                testNodes = TestHelper.getMultipleHighways();
                if (testNodes != null) {
                    if (testIndex >= testNodes.size()) {
                        Log.d(getClass().getSimpleName(), "Alle nodes in testNodes zijn geweest.");
                        return;
                    }
                    latitude = testNodes.get(testIndex).getLat();
                    longitude = testNodes.get(testIndex).getLon();
                    testIndex++;

                    location = new Location("");
                    location.setLatitude(latitude);
                    location.setLongitude(longitude);

                } else {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                dismissProgressDialog();
                if (firstRun) {
                    // Used for pollLocation
                    setPollLocation(location);
                    laraService.getHighwayData(getActivity(), latitude, longitude);
                    firstRun = false;
                }

                pollNearestHighway(latitude, longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("Status changed", "Provider: " + provider);
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("Provider", "enable");
            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("Provider", "disable");
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 0, locationListener);

        showLoadingProgressDialog();
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
     *
     * @param latitude  current latitude
     * @param longitude current longitude
     */
    private void pollNearestHighway(double latitude, double longitude) {
        if (allNodesFromAllHighwaysFromCurrentNode == null) {
            allNodesFromAllHighwaysFromCurrentNode = new ArrayList<>();
        }
        Node nearestNode;
        try {
            if (allNodes != null) {
                if (currentNode == null) {
                    nearestNode = laraService.pollNearestNode(allNodes, latitude, longitude);
                    allNodesFromAllHighwaysFromCurrentNode = laraService.getAllNodesFromAllHighwaysFromCurrentNode(nearestNode, allHighways, allNodes);
                    currentNode = nearestNode;
                }
                // Als er voorheen al een node is gevonden, set de previousNode met deze waarde en haal de hele weg op.
                else {
                    previousNode = currentNode;
                    nearestNode = laraService.pollNearestNode(allNodesFromAllHighwaysFromCurrentNode, latitude, longitude);
                    if (nearestNode != previousNode) {
                        currentNode = nearestNode;
                    }
                    allNodesFromAllHighwaysFromCurrentNode = laraService.getAllNodesFromAllHighwaysFromCurrentNode(nearestNode, allHighways, allNodes);
                }

                currentHighways = laraService.getAllHighwaysFromNode(currentNode, allHighways);
                if (currentHighways == null || currentHighways.get(0) == null) {
                    throw new LaraException("currentHighways is null of is een lege lijst.");
                }
                if (previousNode == null) {
                    Log.d(getClass().getSimpleName(), "Geen previousNode, toon de eerste in currentHighways: '" + currentHighways.get(0).getRoadName() + "'");
                    displayValues(currentHighways.get(0));
                    previousHighway = currentHighways.get(0);
                } else {
                    previousHighways = laraService.getAllHighwaysFromNode(previousNode, allHighways);
                    for (Highway currentHighway : currentHighways) {
                        if (currentHighway.getId() == previousHighway.getId()) {
                            Log.d(getClass().getSimpleName(), "CurrentHighway '"  + currentHighway.getRoadName() + "' is zelfde als previousHighway, deze wordt nu getoond.");
                            displayValues(currentHighway);
                        } else if (currentHighways.contains(previousHighway)) {
                            Log.d(getClass().getSimpleName(), "Één van de huidige wegen is zelfde als previousHighway, toon de previous Highway: '" + previousHighway.getRoadName() + "'");
                            displayValues(previousHighway);
                            break;
                        } else if (previousHighways.contains(currentHighway)) {
                            Log.d(getClass().getSimpleName(), "Één van de previous wegen is zelfde als huidige weg, toon deze: '" + currentHighway.getRoadName() + "'");
                            displayValues(currentHighway);
                            previousHighway = currentHighway;
                            break;
                        }
                    }
                }
            }
        } catch (LaraException le) {
            Log.e(getClass().getSimpleName(), "pollNearestHighway(): " + le.getMessage());
        }

        // If current location is 800+ meters away from the original pollLocation :
        if (laraService.distanceFromPollLocation(pollLocation.getLatitude(), pollLocation.getLongitude(),
                latitude, longitude) > 0.8) {
            Log.d(getClass().getSimpleName(), "pollNearestHighway > verder dan 800m!");
            pollLocation.setLatitude(latitude);
            pollLocation.setLongitude(longitude);
            laraService.getHighwayData(this, latitude, longitude);
        }
    }

    private void getTextViews() {
        maxSpeed = (TextView) findViewById(R.id.maxspeed);
        roadName = (TextView) findViewById(R.id.roadName);
        speedUnit = (TextView) findViewById(R.id.speedUnit);
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

    private void setPollLocation(Location pollLocation) {
        this.pollLocation = pollLocation;
    }

    private void showLoadingProgressDialog() {
        this.showProgressDialog(this.getString(R.string.dialog_get_location));
    }

    private void showProgressDialog(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
        }

        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}

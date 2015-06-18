package com.rwssistent.LARA.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rwssistent.LARA.R;
import com.rwssistent.LARA.exceptions.LaraException;
import com.rwssistent.LARA.helpers.TestHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.LaraLocation;
import com.rwssistent.LARA.model.Node;
import com.rwssistent.LARA.model.NodeBearing;
import com.rwssistent.LARA.utils.LaraService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private TextView maxSpeed;
    private TextView roadName;
    private TextView speedUnit;
    private TextView infoText;

    private String vehicleTypeFromPrefs;
    private String speedUnitFromPrefs;
    private String maxspeed = "";

    private int oldSpeed = 0;
    private double currentSpeed = 0;

    private double longitude;
    private double latitude;
    private LaraService laraService;

    private List<Node> allNodes;
    private List<Highway> allHighways;

    private Node currentNode;
    private LaraLocation currentLocation;

    private Highway previousHighway;

    private Location pollLocation;
    LocationManager locationManager;
    LocationListener locationListener;

    private ProgressDialog progressDialog;

    MediaPlayer mediaPlayer = null;

    SharedPreferences prefs;
    Boolean firstTime;

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
        img.setImageResource(R.drawable.im_verkeersbord);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        laraService = new LaraService();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //this.getLocationFromPreferences();

        getPreferences();
        getTextViews();
        speedUnit.setText(speedUnitFromPrefs);
    }

    /**
     * Start de locatieService
     */
    @Override
    protected void onResume() {
        super.onResume();

        firstTime = prefs.getBoolean("firstTime", true); //see if it's run before, default no
        if (firstTime) {
            this.showDisclaimer();
        } else {
            startLocationService();
        }
        Log.i(getClass().getSimpleName(), "Activity resumed. LocationManager started polling.");

        getPreferences();
        getTextViews();
        speedUnit.setText(speedUnitFromPrefs);

    }

    /**
     * Verwijder de locationUpdates
     */
    @Override
    protected void onPause() {
        Log.i(getClass().getSimpleName(), "Activity paused. LocationManager stopped polling.");
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
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

    /**
     * Display de values van de highway
     *
     * @param highway de highway die getoond moet worden
     */
    public void displayValues(Highway highway) {
        if (highway != null) {
            if (highway.getMaxSpeed() > 0) {
                if (highway.getMaxSpeed() > 90 && vehicleTypeFromPrefs.equals("Aanhangwagen")) {
                    maxspeed = "90";
                } else if (highway.getMaxSpeed() > 80 && vehicleTypeFromPrefs.equals("Bus")) {
                    maxspeed = "80";
                } else if (laraService.getConditionalValid(highway)) {
                    maxspeed = String.valueOf(highway.getMaxSpeedConditional());
                } else {
                    maxspeed = String.valueOf(highway.getMaxSpeed());
                }

                if (speedUnitFromPrefs.equals("MPH")) {
                    maxspeed = convertToMPH(maxspeed);
                }

                if (oldSpeed != Integer.valueOf(maxspeed)) {

                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            playSound(Integer.valueOf(maxspeed));
                        }
                    };
                    thread.run();
                    oldSpeed = Integer.valueOf(maxspeed);
                }

                maxSpeed.setText(maxspeed);
                speedUnit.setVisibility(View.VISIBLE);
                if((currentSpeed - 5) > Integer.valueOf(maxspeed)) {
                    infoText.setVisibility(View.VISIBLE);
                } else {
                    infoText.setVisibility(View.INVISIBLE);
                }
            } else {
                maxSpeed.setText(R.string.unknown_maxspeed);
                speedUnit.setVisibility(View.INVISIBLE);
                oldSpeed = 0;
            }
            if (highway.getRoadName() != null) {
                roadName.setVisibility(View.VISIBLE);
                roadName.setText(String.valueOf(highway.getRoadName()));
                if (highway.getRoadName().length() > 21) {
                    roadName.setTextSize(20);
                } else {
                    roadName.setTextSize(30);
                }
            } else {
                roadName.setVisibility(View.INVISIBLE);
            }
        }
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

    /**
     * Start de locatieService die elke x aantal ms een locatieupdate vraagt
     */
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
                if (currentLocation != null) {
                    currentSpeed = laraService.getSpeed(currentLocation.getLat(), currentLocation.getLon(), currentLocation.getTime(), latitude, longitude, Calendar.getInstance().get(Calendar.MILLISECOND));
                }
                currentLocation = new LaraLocation(latitude, longitude, location.getAccuracy(), Calendar.getInstance().get(Calendar.MILLISECOND));
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
        if (locationIsOn()) {
            showLoadingProgressDialog();
        }
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
        boolean highwayIsSet = false;
        Node nearestNode;
        try {
            if (allNodes != null) {
                if (currentNode == null) {
                    nearestNode = laraService.pollNearestNode(allNodes, latitude, longitude);
                    currentNode = nearestNode;

                    // Display
                    Highway highwayToDisplay = laraService.getEntireHighway(currentNode, allHighways);
                    displayValues(highwayToDisplay);
                    previousHighway = highwayToDisplay;
                    return;
                }
                // get Current node
                List<Node> allNodesFromCurrentNode = laraService.getAllNextNodesFromCurrentNode(currentNode, allHighways, allNodes);

                // Bereken de bearing met alle nodes in allNodesFromCurrentNode
                // + sla ze op in een lijst van type NodeBearing = Node node, double bearing.
                List<NodeBearing> nodeWithBearings = new ArrayList<>();
                for (Node node : allNodesFromCurrentNode) {
                    nodeWithBearings.add(new NodeBearing(node,
                            laraService.rumbLineBearing(currentNode.getLat(), currentNode.getLon(), node.getLat(), node.getLon())));
                }
                // Bereken de bearing tussen vorige node (currentNode) en de nieuwe locatie
                double bearingBetweenLastNodeAndLocation = laraService.rumbLineBearing(currentNode.getLat(), currentNode.getLon(),
                        currentLocation.getLat(), currentLocation.getLon());
                Log.i(getClass().getSimpleName(), "bearingBetweenLastNodeAndLocation: " + bearingBetweenLastNodeAndLocation);

                // Zoek de node waarvan de bearing het meest in de buurt komt van bearingBetweenLastNodeAndLocation
                Node nearestBearingNode = laraService.giveNearestBearingNode(bearingBetweenLastNodeAndLocation, nodeWithBearings);
                Log.i(getClass().getSimpleName(), "nearestBearingNode: " + nearestBearingNode.getId());

                List<Highway> allHighwaysFromNode = laraService.getAllHighwaysFromNode(nearestBearingNode, allHighways);
                if (allHighwaysFromNode == null || allHighwaysFromNode.size() == 0) {
                    throw new LaraException("allHighwaysFromNode is null");
                }
                // Zet currentNode met value van nearestBearingNode
                if (laraService.distanceBetweenLocations(currentLocation.getLat(), currentLocation.getLon(), currentNode.getLat(), currentNode.getLon()) >
                        laraService.distanceBetweenLocations(currentNode.getLat(), currentNode.getLon(), nearestBearingNode.getLat(), nearestBearingNode.getLon())) {

                    // Is het verschil tussen currentNode + nearestBearingNode en currentNode + currentLocation meer dan 20? Zoek opnieuw naar dichtstbijzijnde node.
                    double bearingBetweenLastNodeAndNextNode = laraService.rumbLineBearing(currentNode.getLat(), currentNode.getLon(), nearestBearingNode.getLat(), nearestBearingNode.getLon());

                    if ((bearingBetweenLastNodeAndLocation - bearingBetweenLastNodeAndNextNode) > 30
                            || (bearingBetweenLastNodeAndNextNode - bearingBetweenLastNodeAndLocation) > 30) {
                        Log.e(getClass().getSimpleName(), "De bearing naar de volgende node is te groot. Waarschijnlijk is de gebruiker op de verkeerde weg. Zoek een nieuwe node");
                        currentNode = laraService.pollNearestNode(allNodes, currentLocation.getLat(), currentLocation.getLon());

                        // Display
                        List<Highway> allHighwaysFromNewNode = laraService.getAllHighwaysFromNode(currentNode, allHighways);
                        for (Highway highway : allHighwaysFromNewNode) {
                            if (highway.getRoadName().equals(previousHighway.getRoadName())) {
                                Log.i(getClass().getSimpleName(), "Gevonden highway is het zelfde als de previous highway: " + highway.getRoadName());
                                displayValues(highway);
                                highwayIsSet = true;
                                break;
                            }
                        }
                        if (!highwayIsSet && allHighwaysFromNewNode.size() > 0) {
                            displayValues(allHighwaysFromNewNode.get(0));
                            previousHighway = allHighwaysFromNewNode.get(0);
                        }
                        return;
                    }

                    currentNode = nearestBearingNode;
                    Log.e(getClass().getSimpleName(), "Het aantal meters naar de nearestNode is gereden. Zet de currentNode met value van nearestBearingNode");
                }
                highwayIsSet = false;
                for (Highway highway : allHighwaysFromNode) {
                    if (highway.getRoadName().equals(previousHighway.getRoadName())) {
                        Log.i(getClass().getSimpleName(), "Gevonden highway is het zelfde als de previous highway: " + highway.getRoadName());
                        displayValues(highway);
                        highwayIsSet = true;
                        break;
                    }
                }
                if (!highwayIsSet) {
                    displayValues(allHighwaysFromNode.get(0));
                    previousHighway = allHighwaysFromNode.get(0);
                }

            }
        } catch (LaraException le) {
            Log.e(getClass().getSimpleName(), "pollNearestHighway(): " + le.getMessage());
        }

        // If current location is 800+ meters away from the original pollLocation :
        if (laraService.distanceBetweenLocations(pollLocation.getLatitude(), pollLocation.getLongitude(),
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
        infoText = (TextView) findViewById(R.id.info);

    }

    private void getPreferences() {
        vehicleTypeFromPrefs = prefs.getString(getString(R.string.vehicle_type_key), "Auto");
        speedUnitFromPrefs = prefs.getString(getString(R.string.speed_unit_key), "KM/H");
    }

    private String convertToMPH(String speedString) {

        double speed = Double.valueOf(speedString);
        double speedInMPH = speed * 0.6215;
        double roundedMPH = Math.round(speedInMPH);

        return String.valueOf((int) roundedMPH);
    }

    /**
     * Test method used for custom location
     */
    private void getLocationFromPreferences() {
        // String latitudePref = PreferenceHelper.readPreference(this, Constants.PREF_LATITUDE_NAME, null, Constants.PREF_FILE_NAME);
        //  if (latitudePref != null && !latitudePref.isEmpty()) {
        latitude = 52.06506;
        // }
        //String longitudePref = PreferenceHelper.readPreference(this, Constants.PREF_LONGITUDE_NAME, null, Constants.PREF_FILE_NAME);
        //if (longitudePref != null && !longitudePref.isEmpty()) {
        longitude = 5.30319;
        // }
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

    private void showDisclaimer() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.firstTime_Dialog_Title);
        builder.setMessage(R.string.firstTime_Dialog_Message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean("firstTime", false); //set to has run
                edit.apply(); //apply
                startLocationService();
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.show();

        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }

    private void playSound(int speed) {

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }

        if (speedUnitFromPrefs.equals("KM/H")) {

            if (speed == 15) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_15);
            }

            if (speed == 30) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_30);
            }

            if (speed == 50) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_50);
            }

            if (speed == 60) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_60);
            }

            if (speed == 70) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_70);
            }

            if (speed == 80) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_80);
            }

            if (speed == 90) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_90);
            }

            if (speed == 100) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_100);
            }

            if (speed == 120) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_120);
            }

            if (speed == 130) {
                mediaPlayer = MediaPlayer.create(this, R.raw.kmh_130);
            }
        }

        if (speedUnitFromPrefs.equals("MPH")) {

            if (speed == 9) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_9);
            }

            if (speed == 19) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_19);
            }

            if (speed == 31) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_31);
            }

            if (speed == 37) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_37);
            }

            if (speed == 44) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_44);
            }

            if (speed == 50) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_50);
            }

            if (speed == 56) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_56);
            }

            if (speed == 62) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_62);
            }

            if (speed == 75) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_75);
            }

            if (speed == 81) {
                mediaPlayer = MediaPlayer.create(this, R.raw.mph_81);
            }

        }

        mediaPlayer.start();

    }

    private boolean locationIsOn() {
        try {
            if (Settings.Secure.getInt(this.getContentResolver(), Settings.Secure.LOCATION_MODE) == 0) {
                Log.e(getClass().getSimpleName(), "Location is off, prompt to go to settings");
//                Toast.makeText(MainActivity.this, R.string.no_location_content, Toast.LENGTH_LONG).show();
                showLocationPrompt();
                return false;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void showLocationPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.no_location_title);
        builder.setMessage(R.string.no_location_content);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        });
        builder.setCancelable(false);
        AlertDialog dialog = builder.show();

        TextView messageView = (TextView) dialog.findViewById(android.R.id.message);
        messageView.setGravity(Gravity.CENTER);
    }
}

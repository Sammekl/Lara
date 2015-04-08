package com.rwssistent.LARA.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rwssistent.LARA.R;
import com.rwssistent.LARA.helpers.PreferenceHelper;
import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.utils.Constants;
import com.rwssistent.LARA.utils.LaraService;

import org.w3c.dom.Text;


public class MainActivity extends ActionBarActivity {

    private TextView maxSpeed;
    private TextView numOfLanes;
    private TextView roadName;
    private TextView speedUnit;
    private TextView currentLocation;

    private double longitude;
    private double latitude;
    private LaraService laraService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setActionBar();
        this.getTextViews();
//        this.drawMaxSpeedCircle();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationService();
        this.getLocationFromPreferences();
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
        if (highway.getRoadName() != null && highway.getRoadName() != "") {
            roadName.setText(String.valueOf(highway.getRoadName()));
        }
    }

    private void drawMaxSpeedCircle() {
        ImageView drawingImageView = (ImageView) this.findViewById(R.id.DrawingImageView);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        paint.setStrokeWidth(30);
        float radius = 40;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawCircle(height / 2, width / 2, radius, paint);

        drawingImageView.setImageBitmap(bitmap);

    }

    public void startLocationService() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
                currentLocation.setText("Long: " + longitude + " | Lat: " + latitude);
                laraService.getRoadData(getActivity(), latitude, longitude);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
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
        currentLocation = (TextView) findViewById(R.id.current_location);
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

    private void setActionBar() {
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00ADEF"));
        ActionBar currentActionBar = getSupportActionBar();
        currentActionBar.setBackgroundDrawable(colorDrawable);
    }
}

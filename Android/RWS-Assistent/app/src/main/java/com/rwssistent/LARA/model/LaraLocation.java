package com.rwssistent.LARA.model;

/**
 * Created by Samme on 4-6-2015.
 */
public class LaraLocation {

    private double lat;
    private double lon;
    private float accuracy;


    public LaraLocation() {

    }

    public LaraLocation(double lat, double lon, float accuracy) {
        this.lat = lat;
        this.lon = lon;
        this.accuracy = accuracy;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(float accuracy) {
        this.accuracy = accuracy;
    }

}

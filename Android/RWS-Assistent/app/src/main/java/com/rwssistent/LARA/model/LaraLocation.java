package com.rwssistent.LARA.model;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Samme on 4-6-2015.
 */
public class LaraLocation {

    private double lat;
    private double lon;
    private float accuracy;
    private Date time;

    public LaraLocation() {

    }

    public LaraLocation(double lat, double lon, float accuracy, Date time) {
        this.lat = lat;
        this.lon = lon;
        this.accuracy = accuracy;
        this.time = time;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}

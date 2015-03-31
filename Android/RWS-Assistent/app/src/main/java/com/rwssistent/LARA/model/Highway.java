package com.rwssistent.LARA.model;

/**
 * Created by Samme on 19-3-2015.
 */
public class Highway {

    private int lanes;
    private int maxSpeed;
    private int maxSpeedConditional;
    private String roadName;
    private String roadType;

    public Highway() {

    }

    public Highway(int lanes, int maxSpeed, int maxSpeedConditional, String roadName) {
        this.lanes = lanes;
        this.maxSpeed = maxSpeed;
        this.maxSpeedConditional = maxSpeedConditional;
        this.roadName = roadName;
    }

    public int getLanes() {
        return lanes;
    }

    public void setLanes(int lanes) {
        this.lanes = lanes;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public int getMaxSpeedConditional() {
        return maxSpeedConditional;
    }

    public void setMaxSpeedConditional(int maxSpeedConditional) {
        this.maxSpeedConditional = maxSpeedConditional;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }
}

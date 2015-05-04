package com.rwssistent.LARA.model;

import java.sql.Time;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Samme on 19-3-2015.
 */
public class Highway {

    private int lanes;
    private int maxSpeed;
    private int maxSpeedConditional;
    private Calendar maxSpeedConditionalStart;
    private Calendar maxSpeedConditionalEnd;
    private String roadName;
    private String roadType;

    private List<Long> nodes;

    public Highway() {

    }

    public Highway(int lanes, int maxSpeed, int maxSpeedConditional, String roadName, List<Long> nodes) {
        this.lanes = lanes;
        this.maxSpeed = maxSpeed;
        this.maxSpeedConditional = maxSpeedConditional;
        this.roadName = roadName;
        this.nodes = nodes;
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

    public Calendar getMaxSpeedConditionalStart() {
        return maxSpeedConditionalStart;
    }

    public void setMaxSpeedConditionalStart(Calendar maxSpeedConditionalStart) {
        this.maxSpeedConditionalStart = maxSpeedConditionalStart;
    }

    public Calendar getMaxSpeedConditionalEnd() {
        return maxSpeedConditionalEnd;
    }

    public void setMaxSpeedConditionalEnd(Calendar maxSpeedConditionalEnd) {
        this.maxSpeedConditionalEnd = maxSpeedConditionalEnd;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getRoadType() {
        return roadType;
    }

    public void setRoadType(String roadType) {
        this.roadType = roadType;
    }

    public List<Long> getNodes() {
        return nodes;
    }

    public void setNodes(List<Long> nodes) {
        this.nodes = nodes;
    }
}

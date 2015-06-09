package com.rwssistent.LARA.model;

/**
 * Created by Samme on 4-6-2015.
 */
public class NodeBearing {

    private Node node;
    private double bearing;

    public NodeBearing() {

    }

    public NodeBearing(Node node, double bearing) {
        this.node = node;
        this.bearing = bearing;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public double getBearing() {
        return bearing;
    }

    public void setBearing(double bearing) {
        this.bearing = bearing;
    }
}

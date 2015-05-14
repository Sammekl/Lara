package com.rwssistent.LARA.model;

/**
 * Created by Samme on 16-4-2015.
 */
public class Node {

    private Long id;
    private double lat;
    private double lon;

    public Node(Long id, double lat, double lon) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
    }

    public Node(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public Node() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}

package com.rwssistent.LARA.utils;

/**
 * Created by Samme on 17-3-2015.
 */
public class Constants {

    public static final String PREFIX = "RWS_";

    // --PREFERENCES-- //
    public static final String PREF_FILE_NAME = PREFIX + "PREFERENCES";
    public static final String PREF_LONGITUDE_NAME = PREFIX + "LONGITUDE";
    public static final String PREF_LATITUDE_NAME = PREFIX + "LATITUDE";
    // -/PREFERENCES-- //


    // --API-- //
    public static final String PREF_URL = "http://overpass-api.de/api/interpreter?data=[out:json];(way(around:20,%s,%s)[highway=motorway];%%3E;);out;";
    public static final String PREF_API_URL = "http://overpass-api.de/api/interpreter?data=";
    public static final String PREF_OUTPUT = "[out:json];";
    public static final String PREF_ROAD_TYPE = "motorway";
    public static final int PREF_SEARCH_RADIUS = 20;
    // -/API-- //


}

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
    public static final String PREF_API_URL = "http://overpass-api.de/api/interpreter?data=[out:json];" +
            "(way(around:30,%s,%s)" +
            "[highway!=cycleway]" +
            "[highway!=footway]" +
            "[highway!=bridleway]" +
            "[highway!=steps]" +
            "[highway!=path]" +
            "[highway];%%3E;);out;";
    public static final String PREF_API_URL2 = "http://overpass-api.de/api/interpreter?data=";
    public static final String PREF_API_OUTPUT = "[out:json];";
    public static final String PREF_API_ROAD_TYPE = "motorway";
    public static final int PREF_API_SEARCH_RADIUS = 20;
    public static final String PREF_API_ELEMENTS = "elements";
    public static final String PREF_API_TYPE = "type";
    public static final String PREF_API_WAY = "way";
    public static final String PREF_API_TAGS = "tags";
    public static final String PREF_API_LANES = "lanes";
    public static final String PREF_API_MAXSPEED = "maxspeed";
    public static final String PREF_API_ROAD_REF = "ref";
    public static final String PREF_API_ROAD_NAME = "name";
    // -/API-- //


}

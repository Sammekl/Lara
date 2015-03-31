package com.rwssistent.LARA.helpers;

import android.util.Log;

import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samme on 23-3-2015.
 */
public class JSONHelper {

    public static List<Highway> getHighwaysFromResult(String result) {
        List<Highway> highways = new ArrayList<>();

        try {
            JSONObject jsonResult = new JSONObject(result);
            JSONArray typeArray = new JSONArray(jsonResult.getString(Constants.PREF_API_ELEMENTS));
            List<JSONObject> jsonObjects = new ArrayList<>();
            for (int i = 0; i < typeArray.length(); i++) {
                JSONObject obj = typeArray.getJSONObject(i);

                if (obj.getString(Constants.PREF_API_TYPE).equals(Constants.PREF_API_WAY)) {
                    jsonObjects.add(obj);
                }
            }
            for (JSONObject jsonWay : jsonObjects) {
                JSONObject tagsObj = jsonWay.getJSONObject(Constants.PREF_API_TAGS);
                Highway highway = new Highway();
                if (tagsObj.has(Constants.PREF_API_LANES)) {
                    highway.setLanes(tagsObj.getInt(Constants.PREF_API_LANES));
                }
                if (tagsObj.has(Constants.PREF_API_MAXSPEED)) {
                    highway.setMaxSpeed(tagsObj.getInt(Constants.PREF_API_MAXSPEED));
                }
                if (tagsObj.has(Constants.PREF_API_ROAD_REF)) {
                    highway.setRoadName(tagsObj.getString(Constants.PREF_API_ROAD_REF));
                } else {
                    highway.setRoadName(tagsObj.getString(Constants.PREF_API_ROAD_NAME));
                }
                highways.add(highway);
            }
        } catch (Exception e) {
            Log.e("JSONHelper", e.getMessage());
        }
        return highways;
    }
}

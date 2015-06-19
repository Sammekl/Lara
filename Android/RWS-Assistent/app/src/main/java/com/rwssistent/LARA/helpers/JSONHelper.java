package com.rwssistent.LARA.helpers;

import android.util.Log;

import com.rwssistent.LARA.model.Highway;
import com.rwssistent.LARA.model.Node;
import com.rwssistent.LARA.utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Samme on 23-3-2015.
 */
public class JSONHelper {

    /**
     * Parse de string bestaande uit JSON naar een lijst met Highways
     *
     * @param result de JSON String die geparst moet worden
     * @return De lijst met Highways
     */
    public static List<Highway> getHighwaysFromResult(String result) {
        List<Highway> highways = new ArrayList<>();
        Long startTime = System.currentTimeMillis();
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


                Highway highway = new Highway();

                if (jsonWay.has(Constants.PREF_API_WAY_ID)) {
                    highway.setId(jsonWay.getLong(Constants.PREF_API_WAY_ID));
                }
                // ==================
                // TAGS
                // ==================
                JSONObject tagsObj = jsonWay.getJSONObject(Constants.PREF_API_WAY_TAGS);
                if (tagsObj.has(Constants.PREF_API_WAY_LANES)) {
                    highway.setLanes(tagsObj.getInt(Constants.PREF_API_WAY_LANES));
                }
                if (tagsObj.has(Constants.PREF_API_WAY_MAXSPEED)) {
                    highway.setMaxSpeed(tagsObj.getInt(Constants.PREF_API_WAY_MAXSPEED));
                }
                if (tagsObj.has(Constants.PREF_API_WAY_MAXSPEED_CONDITIONAL)) {
                    String conditional = tagsObj.getString(Constants.PREF_API_WAY_MAXSPEED_CONDITIONAL);
                    String[] parts = conditional.split("@");

                    //maxspeed
                    parts[0] = parts[0].replace(" ", "");
                    int maxspeed = Integer.parseInt(parts[0]);
                    highway.setMaxSpeedConditional(maxspeed);

                    //timeslot
                    Log.d("JSONHelper", "timeslot: " + parts[1]);
                    parts[1] = parts[1].replace("(", "");
                    parts[1] = parts[1].replace(")", "");
                    parts[1] = parts[1].replace(" ", "");

                    String[] timeslot = parts[1].split("-");

                    Date dateStart = new SimpleDateFormat("HH:mm").parse(timeslot[0]);
                    Calendar calendarStart = Calendar.getInstance();
                    calendarStart.setTime(dateStart);
                    calendarStart.add(Calendar.DATE, 1);
                    highway.setMaxSpeedConditionalStart(calendarStart);

                    Date dateEnd = new SimpleDateFormat("HH:mm").parse(timeslot[1]);
                    Calendar calendarEnd = Calendar.getInstance();
                    calendarEnd.setTime(dateEnd);
                    calendarEnd.add(Calendar.DATE, 1);
                    highway.setMaxSpeedConditionalEnd(calendarEnd);
                }
                // If has ref
                if (tagsObj.has(Constants.PREF_API_WAY_ROAD_REF)) {
                    // if ref + name
                    if (tagsObj.has(Constants.PREF_API_WAY_ROAD_NAME)) {
                        highway.setRoadName(tagsObj.getString(Constants.PREF_API_WAY_ROAD_REF) + " - " + tagsObj.getString(Constants.PREF_API_WAY_ROAD_NAME));
                    } else {
                        highway.setRoadName(tagsObj.getString(Constants.PREF_API_WAY_ROAD_REF));
                    }
                }
                // no ref, just a name
                else if (tagsObj.has(Constants.PREF_API_WAY_ROAD_NAME)) {
                    highway.setRoadName(tagsObj.getString(Constants.PREF_API_WAY_ROAD_NAME));
                } else if (tagsObj.has(Constants.PREF_API_WAY_ROAD_JUNCTION_REF)) {
                    highway.setRoadName(tagsObj.getString(Constants.PREF_API_WAY_ROAD_JUNCTION_REF));
                    if(tagsObj.has(Constants.PREF_API_WAY_ROAD_DESTINATION)) {
                        highway.setRoadName(highway.getRoadName() + " - " + tagsObj.getString(Constants.PREF_API_WAY_ROAD_DESTINATION));
                    }
                } else if (highway.getMaxSpeed() == 0) {
                    continue;
                } else {
                    highway.setRoadName("");
                }

                // ==================
                // NODE
                // ==================
                JSONArray nodesArray = jsonWay.getJSONArray(Constants.PREF_API_WAY_NODES);
                List<Long> nodeIds = new ArrayList<>();
                for (int i = 0; i < nodesArray.length(); i++) {
                    nodeIds.add(nodesArray.getLong(i));
                }
                highway.setNodes(nodeIds);
                highways.add(highway);
            }
        } catch (Exception e) {
            Log.e("JSONHelper", e.getMessage());
        }
        Long timeTaken = System.currentTimeMillis() - startTime;
        Log.d("getHighwaysFromResult", "time taken: " + timeTaken);
        return highways;
    }

    /**
     * Parse de string bestaande uit JSON naar een lijst met Nodes
     *
     * @param result de JSON String die geparst moet worden
     * @return De lijst met Nodes
     */
    public static List<Node> getNodesFromResult(String result) {
        Long startTime = System.currentTimeMillis();
        List<Node> nodes = new ArrayList<>();

        try {
            JSONObject jsonResult = new JSONObject(result);
            JSONArray typeArray = new JSONArray(jsonResult.getString(Constants.PREF_API_ELEMENTS));
            List<JSONObject> jsonObjects = new ArrayList<>();
            for (int i = 0; i < typeArray.length(); i++) {
                JSONObject obj = typeArray.getJSONObject(i);

                if (obj.getString(Constants.PREF_API_TYPE).equals(Constants.PREF_API_NODE)) {
                    jsonObjects.add(obj);
                }
            }
            for (JSONObject jsonNode : jsonObjects) {
                Node node = new Node();
                if (!jsonNode.has(Constants.PREF_API_NODE_ID)) {
                    break;
                } else {
                    node.setId(jsonNode.getLong(Constants.PREF_API_NODE_ID));
                }
                if (jsonNode.has(Constants.PREF_API_NODE_LAT)) {
                    node.setLat(jsonNode.getDouble(Constants.PREF_API_NODE_LAT));
                }
                if (jsonNode.has(Constants.PREF_API_NODE_LON)) {
                    node.setLon(jsonNode.getDouble(Constants.PREF_API_NODE_LON));
                }
                nodes.add(node);
            }
        } catch (Exception e) {
            Log.e("JSONHelper", e.getMessage());
        }
        Long timeTaken = System.currentTimeMillis() - startTime;
        Log.d("NodesFromResult", "time taken: " + timeTaken);
        return nodes;
    }
}
